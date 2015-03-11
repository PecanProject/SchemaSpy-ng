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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import schemaspy.model.Database;
import schemaspy.model.Table;
import schemaspy.util.HtmlEncoder;
import schemaspy.util.LineWriter;

/**
 * The main index that contains all tables and views that were evaluated
 *
 * @author John Currier
 */
public class HtmlMainIndexPage extends HtmlFormatter {
    private static HtmlMainIndexPage instance = new HtmlMainIndexPage();
    private final NumberFormat integerFormatter = NumberFormat.getIntegerInstance();

    /**
     * Singleton: Don't allow instantiation
     */
    private HtmlMainIndexPage() {
    }

    /**
     * Singleton accessor
     *
     * @return the singleton instance
     */
    public static HtmlMainIndexPage getInstance() {
        return instance;
    }

    public void write(Database database, Collection<Table> tables, boolean showOrphansDiagram, LineWriter html) throws IOException {
        Set<Table> byName = new TreeSet<Table>(new Comparator<Table>() {
            public int compare(Table table1, Table table2) {
                return table1.compareTo(table2);
            }
        });
        byName.addAll(tables);

        boolean showIds = false;
        int numViews = 0;
        boolean comments = false;

        for (Table table : byName) {
            if (table.isView())
                ++numViews;
            showIds |= table.getId() != null;
            if (table.getComments() != null)
                comments = true;
        }

        writeHeader(database, byName.size() - numViews, numViews, showIds, showOrphansDiagram, comments, html);

        int numTableCols = 0;
        int numViewCols = 0;
        int numRows = 0;
        for (Table table : byName) {
            writeLineItem(table, showIds, html);
        }
        html.writeln("</table>");
        html.writeln("</div>");
    }

    private void writeHeader(Database db, int numberOfTables, int numberOfViews, boolean showIds, boolean hasOrphans, boolean hasComments, LineWriter html) throws IOException {
        html.writeln("<div class='indent'>");
        html.write("<p>");
        html.writeln("<table class='dataTable' border='1' rules='groups'>");
        for (int i = 0; i < 2; i++)
            html.writeln("<colgroup>");
        html.writeln("<colgroup class='comment'>");
        html.writeln("<thead align='left'>");
        html.writeln("<tr>");
        String tableHeading;
        if (numberOfViews == 0)
            tableHeading = "Table";
        else if (numberOfTables == 0)
            tableHeading = "View";
        else
            tableHeading = "Table / View";
        html.writeln("  <th valign='bottom'>" + tableHeading + "</th>");
        html.writeln("  <th align='right' valign='bottom'>Columns</th>");
        html.writeln("  <th class='comment' align='left' valign='bottom'>Comments</th>");
        html.writeln("</tr>");
        html.writeln("</thead>");
        html.writeln("<tbody>");
    }

    private void writeLineItem(Table table, boolean showIds, LineWriter html) throws IOException {
        html.write(" <tr class='" + (table.isView() ? "view" : "tbl") + "' valign='top'>");
        html.write("  <td class='detail'><%=link_to '");
        html.write(table.getName());
        html.write("',schemas_path(:partial => '");
        html.write(table.getName());
        html.write("_table')%></td>");
        if (showIds) {
            html.write("  <td class='detail' align='right'>");
            Object id = table.getId();
            if (id != null)
                html.write(String.valueOf(id));
            else
                html.writeln("&nbsp;");
            html.writeln("</td>");
        }
        html.write("  <td class='detail' align='right'>");
        html.write(String.valueOf(integerFormatter.format(table.getColumns().size())));
        html.writeln("</td>");
        html.write("  <td class='comment detail'>");
        String comments = table.getComments();
        if (comments != null) {
            if (encodeComments)
                for (int i = 0; i < comments.length(); ++i)
                    html.write(HtmlEncoder.encodeToken(comments.charAt(i)));
            else
                html.write(comments);
        }
        html.writeln("</td>");
        html.writeln("  </tr>");
    }
    @Override
    protected boolean isMainIndex() {
        return true;
    }
}
