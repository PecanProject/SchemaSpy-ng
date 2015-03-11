/*
 * This file is a part of the SchemaSpy project (http://schemaspy.sourceforge.net).
 * Copyright (C) 2004, 2005, 2006, 2007, 2008, 2009, 2010 John Currier
 *
 * SchemaSpy is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * SchemaSpy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package schemaspy.view;
  
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import schemaspy.Config;
import schemaspy.model.Database;
import schemaspy.model.ForeignKeyConstraint;
import schemaspy.model.Table;
import schemaspy.model.TableColumn;
import schemaspy.model.TableIndex;
import schemaspy.model.View;
import schemaspy.util.CaseInsensitiveMap;
import schemaspy.util.HtmlEncoder;
import schemaspy.util.LineWriter;

/**
 * The page that contains the details of a specific table or view
 *
 * @author John Currier
 */
public class HtmlTablePage extends HtmlFormatter {
    private static final HtmlTablePage instance = new HtmlTablePage();
    private int columnCounter = 0;
    boolean nocomment=true;
    boolean nofkeys=true;
    private final Map<String, String> defaultValueAliases = new HashMap<String, String>();
    {
        defaultValueAliases.put("CURRENT TIMESTAMP", "now"); // DB2
        defaultValueAliases.put("CURRENT TIME", "now");      // DB2
        defaultValueAliases.put("CURRENT DATE", "now");      // DB2
        defaultValueAliases.put("SYSDATE", "now");           // Oracle
        defaultValueAliases.put("CURRENT_DATE", "now");      // Oracle
    }

    /**
     * Singleton: Don't allow instantiation
     */
    private HtmlTablePage() {
    }

    /**
     * Singleton accessor
     *
     * @return the singleton instance
     */
    public static HtmlTablePage getInstance() {
        return instance;
    }
    public void reset(){
        columnCounter=0;
        nocomment=true;
        nofkeys=true;
    }

    public WriteStats write(Database db, Table table, boolean hasOrphans, File outputDir, WriteStats stats, LineWriter out) throws IOException {
        File diagramsDir = new File(outputDir, "public/images");// diagrams folder
        boolean hasImplied = generateDots(table, diagramsDir, stats);
        writeHeader(db, table, null, hasOrphans, out);///////HTMLFormatter doctype etc       
        writeHeader(table, false, out);
        writeLegend(out);
        out.writeln("<div class='sixteen colums'>");        
        writeMainTable(table, out);
        out.writeln("</div>"); 
        if(nocomment)
            out.writeln("<script>jQuery('.comment').hide()</script>");// if no comment hide comment
        if(nofkeys)
            out.writeln("<script>jQuery('.fkey').hide()</script>");// if no fkeys hide fkeys
        writeCheckConstraints(table, out);
        writeIndexes(table, out);      
        writeView(table, db, out);
        writeDiagram(table, stats, diagramsDir, out);
        out.writeln("</div>"); 
        return stats;
    }

    private void writeHeader(Table table, boolean hasImplied, LineWriter html) throws IOException {
        for (TableColumn column : table.getColumns()) {
            if (column.getComments() != null && column.getComments().length()!=0) 
                nocomment=false;
            if(column.isForeignKey())
                nofkeys=false;
        }
    }

    public void writeMainTable(Table table, LineWriter out) throws IOException {
        HtmlColumnsPage.getInstance().writeMainTableHeader(table.getId() != null, null, out);///////column page

        out.writeln("<tbody valign='top'>");
        Set<TableColumn> primaries = new HashSet<TableColumn>(table.getPrimaryColumns());
        Set<TableColumn> indexedColumns = new HashSet<TableColumn>();
        for (TableIndex index : table.getIndexes()) {
            indexedColumns.addAll(index.getColumns());
        }

        boolean showIds = table.getId() != null;
        for (TableColumn column : table.getColumns()) {
            writeColumn(column, null, primaries, indexedColumns, showIds, out);
        }
        out.writeln("</table>");
    }

    public void writeColumn(TableColumn column, String tableName, Set<TableColumn> primaries, Set<TableColumn> indexedColumns, boolean showIds, LineWriter out) throws IOException {
        out.writeln("<tr>");
        if (showIds) {
            out.write(" <td class='detail' align='right'>");
            out.write(String.valueOf(column.getId()));
            out.writeln("</td>");
        }
        if (tableName != null) {
            out.write(" <td class='detail'><a href='tables/");
            out.write(tableName);
            out.write(".html'>");
            out.write(tableName);
            out.writeln("</a></td>");
        }
        if (primaries.contains(column))
            out.write(" <td class='primaryKey' title='Primary Key'>");
        else if (indexedColumns.contains(column))
            out.write(" <td class='indexedColumn' title='Indexed'>");
        else
            out.write(" <td class='detail'>");
        out.write(column.getName());
        out.writeln("</td>");
        out.write(" <td class='detail'>");
        out.write(column.getType().toLowerCase());
        out.writeln("</td>");
        out.write(" <td class='detail' align='right'>");
        out.write(column.getDetailedSize());
        out.writeln("</td>");
        out.write(" <td class='detail' align='center'");
        if (column.isNullable())
            out.write(" title='nullable'>&nbsp;&radic;&nbsp;");
        else
            out.write(">&nbsp;&chi;&nbsp;");
        out.writeln("</td>");
        out.write(" <td class='detail' align='center'");
        if (column.isAutoUpdated()) {
            out.write(" title='Automatically updated by the database'>&nbsp;&radic;&nbsp;");
        } else {
            out.write(">&nbsp;&chi;&nbsp;");
        }
        out.writeln("</td>");

        Object defaultValue = column.getDefaultValue();
        if (defaultValue != null || column.isNullable()) {
            Object alias = defaultValueAliases.get(String.valueOf(defaultValue).trim());
            if (alias != null) {
                out.write(" <td class='detail' align='right' title='");
                out.write(String.valueOf(defaultValue));
                out.write("'><i>");
                out.write(alias.toString());
                out.writeln("</i></td>");
            } else {
                out.write(" <td class='detail' align='right'>");
                out.write(String.valueOf(defaultValue));
                out.writeln("</td>");
            }
        } else {
            out.writeln(" <td class='detail'></td>");
        }
        String path = tableName == null ? "" : "tables/";
        out.write(" <td class='detail fkey'>");
        writeRelatives(column, true, path,out);
        out.writeln(" </td>");

        out.write(" <td class='comment detail'>");
        String comments = column.getComments();
        if (comments != null) {
            if (encodeComments)
                for (int i = 0; i < comments.length(); ++i)
                    out.write(HtmlEncoder.encodeToken(comments.charAt(i)));
            else
                out.write(comments);
        }
        out.writeln("</td>");
        out.writeln("</tr>");
    }

    /**
     * Write our relatives
     * @param tableName String
     * @param baseRelative TableColumn
     * @param dumpParents boolean
     * @param out LineWriter
     * @throws IOException
     */
    private void writeRelatives(TableColumn baseRelative, boolean dumpParents, String path, LineWriter out) throws IOException {
        Set<TableColumn> columns = dumpParents ? baseRelative.getParents() : baseRelative.getChildren();
        final int numColumns = columns.size();
        for (TableColumn column : columns) {
            String columnTableName = column.getTable().getName();
            ForeignKeyConstraint constraint = dumpParents ? column.getChildConstraint(baseRelative) : column.getParentConstraint(baseRelative);
            out.write("<%=link_to '");
            out.write(columnTableName);
            out.write(".");
            out.write(column.getName());
            out.writeln("',schemas_path(:partial => '");
            out.write(columnTableName);
            out.write("_table')%>");
        }
    }
    private void writeCheckConstraints(Table table, LineWriter out) throws IOException {
        Map<String, String> constraints = table.getCheckConstraints();
        if (constraints != null && !constraints.isEmpty()) {
            out.writeln("<div class='indent'>");
            out.writeln("<b>Requirements (check constraints):</b>");
            out.writeln("<table class='dataTable' border='1' rules='groups'><colgroup><colgroup>");
            out.writeln("<thead>");
            out.writeln(" <tr>");
            out.writeln("  <th>Constraint</th>");
            out.writeln("  <th class='constraint' style='text-align:left;'>Constraint Name</th>");
            out.writeln(" </tr>");
            out.writeln("</thead>");
            out.writeln("<tbody>");
            for (String name : constraints.keySet()) {
                out.writeln(" <tr>");
                out.write("  <td class='detail'>");
                out.write(HtmlEncoder.encodeString(constraints.get(name).toString()));
                out.writeln("</td>");
                out.write("  <td class='constraint' style='text-align:left;'>");
                out.write(name);
                out.writeln("</td>");
                out.writeln(" </tr>");
            }
            out.writeln("</table></div><p>");
        }
    }

    private void writeIndexes(Table table, LineWriter out) throws IOException {
        boolean showId = table.getId() != null;
        Set<TableIndex> indexes = table.getIndexes();
        if (indexes != null && !indexes.isEmpty()) {
            // see if we've got any strangeness so we can have the correct number of colgroups
            boolean containsAnomalies = false;
            /*for (TableIndex index : indexes) {
                containsAnomalies = index.isUniqueNullable();
                if (containsAnomalies)
                    break;
            }*/

            out.writeln("<div class='indent'>");
            out.writeln("<b>Indexes:</b>");
            out.writeln("<table class='dataTable' border='1' rules='groups'><colgroup><colgroup><colgroup><colgroup>" + (showId ? "<colgroup>" : "") + (containsAnomalies ? "<colgroup>" : ""));
            out.writeln("<thead>");
            out.writeln(" <tr>");
            if (showId)
                out.writeln("  <th>ID</th>");
            out.writeln("  <th>Column(s)</th>");
            out.writeln("  <th>Type</th>");
            out.writeln("  <th>Sort</th>");
            out.writeln("  <th class='constraint' style='text-align:left;'>Constraint Name</th>");
            if (containsAnomalies)
                out.writeln("  <th>Anomalies</th>");
            out.writeln(" </tr>");
            out.writeln("</thead>");
            out.writeln("<tbody>");

            indexes = new TreeSet<TableIndex>(indexes); // sort primary keys first

            for (TableIndex index : indexes) {
                out.writeln(" <tr>");

                if (showId) {
                    out.write("  <td class='detail' align='right'>");
                    out.write(String.valueOf(index.getId()));
                    out.writeln("</td>");
                }

                if (index.isPrimaryKey())
                    out.write("  <td class='primaryKey'>");
                else
                    out.write("  <td class='indexedColumn'>");
                String columns = index.getColumnsAsString();
                if (columns.startsWith("+"))
                    columns = columns.substring(1);
                out.write(columns);
                out.writeln("</td>");

                out.write("  <td class='detail'>");
                out.write(index.getType());
                out.writeln("</td>");

                out.write("  <td class='detail' style='text-align:left;'>");
                Iterator<TableColumn> columnsIter = index.getColumns().iterator();
                while (columnsIter.hasNext()) {
                    TableColumn column = columnsIter.next();
                    if (index.isAscending(column))
                        out.write("<span title='Ascending'>Asc</span>");
                    else
                        out.write("<span title='Descending'>Desc</span>");
                    if (columnsIter.hasNext())
                        out.write("/");
                }
                out.writeln("</td>");

                out.write("  <td class='constraint' style='text-align:left;'>");
                out.write(index.getName());
                out.writeln("</td>");
                out.writeln(" </tr>");
            }
            out.writeln("</table>");
            out.writeln("</div>");
        }
    }

    private void writeView(Table table, Database db, LineWriter out) throws IOException {
        String sql;
        if (table.isView() && (sql = table.getViewSql()) != null) {
            Map<String, Table> tables = new CaseInsensitiveMap<Table>();

            for (Table t : db.getTables())
                tables.put(t.getName(), t);
            for (View v : db.getViews())
                tables.put(v.getName(), v);

            Set<Table> references = new TreeSet<Table>();
            String formatted = Config.getInstance().getSqlFormatter().format(sql, db, references);

            out.writeln("<div class='indent spacer'>");
            out.writeln("  View Definition:");
            out.writeln(formatted);
            out.writeln("</div>");
            out.writeln("<div class='spacer'>&nbsp;</div>");

            if (!references.isEmpty()) {
                out.writeln("<div class='indent'>");
                out.writeln("  Possibly Referenced Tables/Views:");
                out.writeln("  <div class='viewReferences'>");
                out.write("  ");
                for (Table t : references) {
                    out.write("<a href='");
                    out.write(t.getName());
                    out.write(".html'>");
                    out.write(t.getName());
                    out.write("</a>&nbsp;");
                }

                out.writeln("  </div>");
                out.writeln("</div><p/>");
            }

        }
    }

    private boolean generateDots(Table table, File diagramDir, WriteStats stats) throws IOException {
        File oneDegreeDotFile = new File(diagramDir, table.getName() + ".1degree.dot");
        File oneDegreeDiagramFile = new File(diagramDir, table.getName() + ".1degree.png");
        File twoDegreesDotFile = new File(diagramDir, table.getName() + ".2degrees.dot");
        File twoDegreesDiagramFile = new File(diagramDir, table.getName() + ".2degrees.png");
        File impliedDotFile = new File(diagramDir, table.getName() + ".implied2degrees.dot");
        File impliedDiagramFile = new File(diagramDir, table.getName() + ".implied2degrees.png");

        // delete before we start because we'll use the existence of these files to determine
        // if they should be turned into pngs & presented
        oneDegreeDotFile.delete();
        oneDegreeDiagramFile.delete();
        twoDegreesDotFile.delete();
        twoDegreesDiagramFile.delete();
        impliedDotFile.delete();
        impliedDiagramFile.delete();

        if (table.getMaxChildren() + table.getMaxParents() > 0) {
            Set<ForeignKeyConstraint> impliedConstraints;

            DotFormatter formatter = DotFormatter.getInstance();
            LineWriter dotOut = new LineWriter(oneDegreeDotFile, Config.DOT_CHARSET);
            WriteStats oneStats = new WriteStats(stats);
            formatter.writeRealRelationships(table, false, oneStats, dotOut);
            dotOut.close();

            dotOut = new LineWriter(twoDegreesDotFile, Config.DOT_CHARSET);
            WriteStats twoStats = new WriteStats(stats);
            impliedConstraints = formatter.writeRealRelationships(table, true, twoStats, dotOut);
            dotOut.close();

            if (oneStats.getNumTablesWritten() + oneStats.getNumViewsWritten() == twoStats.getNumTablesWritten() + twoStats.getNumViewsWritten()) {
                twoDegreesDotFile.delete(); // no different than before, so don't show it
            }

            if (!impliedConstraints.isEmpty()) {
                dotOut = new LineWriter(impliedDotFile, Config.DOT_CHARSET);
                formatter.writeAllRelationships(table, true, stats, dotOut);
                dotOut.close();
                return true;
            }
        }

        return false;
    }

    private void writeDiagram(Table table, WriteStats stats, File diagramsDir, LineWriter html) throws IOException {
        if (table.getMaxChildren() + table.getMaxParents() > 0) {
            html.writeln("<label for='showone'><input type='checkbox' id='showone' class='showdiagram'>Show One Degree &nbsp;</label>");
            html.writeln("<label for='showtow'><input type='checkbox' id='showtwo' class='showdiagram'>Show Two Degrees</label>");
            html.writeln("<table width='100%' border='0'><tr><td class='container'>");
            if (HtmlTableDiagrammer.getInstance().write(table, diagramsDir, html)) {
                html.writeln("</td></tr></table>");
                writeExcludedColumns(stats.getExcludedColumns(), table, html);
            } else {
                html.writeln("</td></tr></table><p>");
                writeInvalidGraphvizInstallation(html);
            }
        }
    }

}