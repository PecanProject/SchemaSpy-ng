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

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import schemaspy.Config;
import schemaspy.Revision;
import schemaspy.model.Database;
import schemaspy.model.Table;
import schemaspy.model.TableColumn;
import schemaspy.util.Dot;
import schemaspy.util.HtmlEncoder;
import schemaspy.util.LineWriter;

public class HtmlFormatter {
    protected final boolean encodeComments       = Config.getInstance().isEncodeCommentsEnabled();
    protected final boolean displayNumRows       = Config.getInstance().isNumRowsEnabled();
    private   final boolean isMetered            = Config.getInstance().isMeterEnabled();

    protected HtmlFormatter() {
    }//

    protected void writeHeader(Database db, Table table, String text, boolean showOrphans, List<String> javascript, LineWriter out) throws IOException {
        out.writeln("<div class='content' style='clear:both;'>");
        out.writeln("<div class='container'>");
        out.writeln("<table width='100%' border='0' cellpadding='0'>");
        out.writeln(" <tr>");
        out.write("  <td class='heading' valign='middle'>");
        out.write("<span class='header'>");
        out.write(getDescription(db, table, text, true));
        out.write("</span>");
        if (table == null && db.getDescription() != null)
            out.write("<span class='description'>" + db.getDescription().replace("\\=", "=") + "</span>");

        String comments = table == null ? null : table.getComments();
        if (comments != null) {
            out.write("<div style='padding: 0px 4px;'>");
            if (encodeComments)
                for (int i = 0; i < comments.length(); ++i)
                    out.write(HtmlEncoder.encodeToken(comments.charAt(i)));
            else
                out.write(comments);
            out.writeln("</div><p>");
        }
        out.writeln("</td>");;
        out.writeln(" </tr>");
        out.writeln("</table>");
    }

    /**
     * Convenience method for all those formatters that don't deal with JavaScript
     */
    protected void writeHeader(Database db, Table table, String text, boolean showOrphans, LineWriter out) throws IOException {
        writeHeader(db, table, text, showOrphans, null, out);
    }
    protected void writeTableOfContents(boolean showOrphans, LineWriter html) throws IOException {
        return;
    }
///////////////////////////////////////////////////////navbar/////////////////////////////////////////////////////////
    protected String getDescription(Database db, Table table, String text, boolean hoverHelp) {
        StringBuilder description = new StringBuilder();
        if (table != null) {
            if (table.isView())
                description.append("View: ");
            else
                description.append("Table: ");
        }
        if (table != null) {
            if (hoverHelp)
                description.append("<span title='Table'>");
            description.append(table.getName());
            if (hoverHelp)
                description.append("</span>");
        }
        if (text != null) {
            description.append(" - ");
            description.append(text);
        }

        return description.toString();
    }

    protected boolean sourceForgeLogoEnabled() {
        return Config.getInstance().isLogoEnabled();
    }
    protected void writeLegend(LineWriter out) throws IOException{
        out.writeln("<div>");
        out.writeln("<span class='legend'>Legend:</span>");
        out.writeln("<span class='legend primaryKey'>Primary key columns</span>");
        out.writeln("<span class='legend indexedColumn'>Indexed columns</span>");
        out.writeln("</div>");

    }
    ////////////////////////////////////////////////////////////////////
    protected void writeFeedMe(LineWriter html) throws IOException {
        return;
    }

    protected void writeExcludedColumns(Set<TableColumn> excludedColumns, Table table, LineWriter html) throws IOException {
        Set<TableColumn> notInDiagram;

        // diagram INCLUDES relationships directly connected to THIS table's excluded columns
        if (table == null) {
            notInDiagram = excludedColumns;
        } else {
            notInDiagram = new HashSet<TableColumn>();
            for (TableColumn column : excludedColumns) {
                if (column.isAllExcluded() || !column.getTable().equals(table)) {
                    notInDiagram.add(column);
                }
            }
        }

        if (notInDiagram.size() > 0) {
            html.writeln("<span class='excludedRelationship'>");
            html.writeln("<br>Excluded from diagram's relationships: ");
            for (TableColumn column : notInDiagram) {
                if (!column.getTable().equals(table)) {
                    html.write("<a href=\"" + getPathToRoot() + "tables/");
                    html.write(column.getTable().getName());
                    html.write(".html\">");
                    html.write(column.getTable().getName());
                    html.write(".");
                    html.write(column.getName());
                    html.writeln("</a>&nbsp;");
                }
            }
            html.writeln("</span>");
        }
    }

    protected void writeInvalidGraphvizInstallation(LineWriter html) throws IOException {
        html.writeln("<br>SchemaSpy was unable to generate a diagram of table relationships.");
        html.writeln("<br>SchemaSpy requires Graphviz " + Dot.getInstance().getSupportedVersions().substring(4) + " from <a href='http://www.graphviz.org' target='_blank'>www.graphviz.org</a>.");
    }

    protected void writeFooter(LineWriter html) throws IOException {
        html.writeln("</div>");
        if (isMetered) {
            html.writeln("<span style='float: right;' title='This link is only on the SchemaSpy sample pages'>");
            html.writeln("<!-- Site Meter -->");
            html.writeln("<script type='text/javascript' src='http://s28.sitemeter.com/js/counter.js?site=s28schemaspy'>");
            html.writeln("</script>");
            html.writeln("<noscript>");
            html.writeln("<a href='http://s28.sitemeter.com/stats.asp?site=s28schemaspy' target='_top'>");
            html.writeln("<img src='http://s28.sitemeter.com/meter.asp?site=s28schemaspy' alt='Site Meter' border='0'/></a>");
            html.writeln("</noscript>");
            html.writeln("<!-- Copyright (c)2006 Site Meter -->");
            html.writeln("</span>");
        }
        html.writeln("</body>");
        html.writeln("</html>");
    }

    /**
     * Override if your output doesn't live in the root directory.
     * If non blank must end with a trailing slash.
     *
     * @return String
     */
    protected String getPathToRoot() {
        return "";
    }

    /**
     * Override and return true if you're the main index page.
     *
     * @return boolean
     */
    protected boolean isMainIndex() {
        return false;
    }

    /**
     * Override and return true if you're the relationships page.
     *
     * @return boolean
     */
    protected boolean isRelationshipsPage() {
        return false;
    }

    /**
     * Override and return true if you're the orphans page.
     *
     * @return boolean
     */
    protected boolean isOrphansPage() {
        return false;
    }

    /**
     * Override and return true if you're the constraints page
     *
     * @return boolean
     */
    protected boolean isConstraintsPage() {
        return false;
    }

    /**
     * Override and return true if you're the anomalies page
     *
     * @return boolean
     */
    protected boolean isAnomaliesPage() {
        return false;
    }

    /**
     * Override and return true if you're the columns page
     *
     * @return boolean
     */
    protected boolean isColumnsPage() {
        return false;
    }
}
