<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<head>
  <title>SchemaSpy</title>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <meta name="keywords" content="schemaspy schema ddl entity relationships er erwin database db2 oracle udb mysql postgresql sybase hsqldb"/>
  <style type="text/css">
    body    { color: #000000; background-color: #F7F7F7; font-family: verdana, times, sans-serif; }
    table   { border-style: none; margin: 0; }
    th      { background-color: #9BAB96; text-align: left; }
    img     { border: 0; }
    .params { background-color: #E7E7E7; vertical-align: top; }
    .param  { padding: 0px 4px; vertical-align: top; }
    .dbType { padding: 0px 4px; vertical-align: top; }
    .menu {
      border-width: 1px;
      border-style: solid;
      border-color: #CCCCCC;
      padding: 1px;
      margin: 1px;
      width: 125px;
      background-color: #E7E7E7
    }
    .quicklinks { font-size: 8pt; display: block; color: #000000; width: 116px; margin-top: 1px; padding: 2px; background-color: #E7E7E7; border: 1px solid #ddd; text-align: center}
    .quicklinks:hover { background: #142D4E; color:#fff;  border: 1px solid; border-color: #fff; text-decoration: none }
    .donors {
      font-size: 8pt;
      text-align: center;
      border-width: 1px;
      border-style: solid;
      border-color: #CCCCCC;
      padding: 2px 1px 4px;
      margin: 1px;
      width: 124px;
      background-color: #E7E7E7
    }
  </style>
</head>
<body>
<table style="text-align: left;" border="0" cellpadding="2" cellspacing="2" width="100%">
  <tbody>
    <tr>
      <td style="vertical-align: top;">
      <h2>SchemaSpy</h2><h3>Graphical Database Schema Metadata Browser</h3>
      </td>
      <td style="vertical-align: top;">
      <div style="text-align: right;"><br>
      <a href='http://sourceforge.net' target='_blank'><img src='http://sourceforge.net/sflogo.php?group_id=137197&amp;type=1' alt='SourceForge.net' border='0' height='31' width='88'></a>
      <br>
      </div>
      </td>
    </tr>
  </tbody>
</table>
<hr>
<table align="left">
  <tr><td>
    <table class="menu" cellpadding="0" cellspacing="1">
      <tr><td>
        <a class="quicklinks" href="sample/">Sample Output</a>
        <a class="quicklinks" href="http://sourceforge.net/project/showfiles.php?group_id=137197">Download</a>
        <a class="quicklinks" href="releaseNotes.html">Release Notes</a>
        <a class="quicklinks" href="http://sourceforge.net/donate/index.php?group_id=137197">Support SchemaSpy</a>
        <a class="quicklinks" href="http://john.currier.googlepages.com">John Currier</a>
      </td></tr>
    </table>
  </td></tr>
  <tr><td>
    <div class="donors">
    Recent&nbsp;Donors:<br>
    <?php
      echo file_get_contents("donors.html");
    ?>
    <br/>
    <a href="http://sourceforge.net/donate/index.php?group_id=137197"><img src="http://images.sourceforge.net/images/project-support.jpg" alt="Support SchemaSpy" style="border: 0px solid; width: 88px; height: 32px;"></a>
    </div>
  </td></tr>
</table>
Do you hate starting on a new project and having to try to figure
out someone else's idea of a database?
Or are you in QA and the
developers expect you to understand all the relationships in their
schema?
If so then this tool's for you.<br>
<br>SchemaSpy is a Java-based tool (requires <a href="www.java.com/getjava/">Java 1.4 or higher</a>) that analyzes the 
metadata of a schema in a database and generates a visual representation of it in a
browser-displayable format.
It lets you click through the
hierarchy of database tables via child and parent table relationships.
The browsing through relationships can occur though HTML links and/or though the
graphical representation of the relationships.
It's also designed
to help resolve the obtuse errors that a database sometimes gives related
to failures due to constraints.<br>
      <p>
It is free software that is distributed under the terms of the
<a target="_blank" href="http://www.gnu.org/licenses/lgpl.html">
Lesser GNU Public License</a>.
Your <a href="http://sourceforge.net/donate/index.php?group_id=137197">donations</a>
are, however, <span style="font-weight: bold;">greatly </span>appreciated.<br>
      <p>
If you like SchemaSpy then please: 
 nominate it: <a href="https://sourceforge.net/awards/cca/nomination.php?group_id=137197"> 
		SourceForge.net 2007 Community Choice awards</a>, 
 vote for it: <a href="http://freshmeat.net/projects/schemaspy">freshmeat</a>,
 <a href="http://digg.com/programming/SchemaSpy_-_opensource_graphical_DB_schema_generator">digg it</a>,
 and tag it: <a href="http://del.icio.us/post?url=http%3A%2F%2Fschemaspy.sourceforge.net%2F&title=SchemaSpy&copyuser=tag&copytags=database%2Bjava%2Bschema%2Btools%2Bvisualization&jump=no&partner=del">del.icio.us</a>.

      <p>
SchemaSpy uses the <span style="font-style: italic;">dot</span>
executable from <a target="_blank" href="http://www.graphviz.org/">Graphviz</a>
to generate graphical representations of the table/view relationships.
This was initially added for people who see things visually.
Now the graphical representation of relationships is a fundamental feature of the tool.
Graphvis is not required to view the output generated by SchemaSpy, but the <span
 style="font-style: italic;">dot</span> program should be in your PATH
(not CLASSPATH) when running SchemaSpy or none of the graphs will be generated.

<p>Note that some Linux users have experienced problems with Graphviz version 2.6.  
Versions 2.6 to 2.8 are preferred, but if they don't work then try
<a target="_blank" href="http://graphviz.org/pub/graphviz/ARCHIVE/">2.2.1</a>.
Note that SchemaSpy takes advantage of some 2.6+ features so graphs won't be
as well formed when using 2.2.1.<br>
      <p>
SchemaSpy uses JDBC's database metadata extraction services to gather
the majority of its information,
but has to make vendor-specific SQL queries to gather some information
such as the SQL associated with a view and the details of check
constraints.
The differences between vendors have been isolated to configuration files and are extremely limited. 
Almost all of the vendor-specific SQL is optional.<br>
      <br>
Sample output of the tool is available at <a
 onclick="return top.js.OpenExtLink(window,event,this)" href="sample/"
 target="_blank">here</a>.
Note that this was run against an extremely limited schema so it doesn't show the full power of the tool.<br>
      <hr>
      <h3><a name="Running_SchemaSpy">Running SchemaSpy </a></h3>
      <p>You run SchemaSpy from the command line: </p>
      <table>
        <tbody>
          <tr>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td><code>java -jar schemaSpy.jar -t dbType -db dbName [-s schema] -u user [-p password] -o outputDir [-nohtml][-noimplied]</code>
            </td>
          </tr>
        </tbody>
      </table>
      <p/>
      <table class="params" border="1" cellpadding="0" cellspacing="1">
        <tbody>
          <tr>
            <th>&nbsp;</th>
            <th class="param">Parameter</th>
            <th class="param">Description</th>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="param"><code>-t databaseType</code></td>
            <td class="param">Type of database (e.g. ora, db2, etc.).
                Use <code>-dbhelp</code> for a list of built-in types.  Defaults to <code>ora</code>.
            </td>
          </tr>
          <tr>
            <td>*</td>
            <td class="param"><code>-db dbName</code></td>
            <td class="param">Name of database to connect to</td>
          </tr>
          <tr>
            <td>*</td>
            <td class="param"><code>-u user</code></td>
            <td class="param">Valid database user id with read access</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="param"><code>-s schema</code></td>
            <td class="param">Database schema (optional if the same as user or isn't supported by your database)</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="param"><code>-p password</code></td>
            <td class="param">Password associated with that user.  Defaults to no password.</td>
          </tr>
          <tr>
            <td>*</td>
            <td class="param"><code>-o outputDirectory</code></td>
            <td class="param">Directory to write the generated HTML/graphs to</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="param"><code>-cp pathToDrivers</code></td>
            <td class="param">Looks for drivers here before looking in driverPath in [databaseType].properties</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="param"><code>-desc "Schema description"</code></td>
            <td class="param">Displays the specified textual description on summary pages.
                If your description includes an equals sign then escape it with a backslash.<br>
                For example:<br>
                -desc "&lt;a href\='http://schemaspy.sourceforge.net'&gt;SchemaSpy&lt;/a&gt;".</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="param"><code>-i tableNamesRegex</code></td>
            <td class="param">Only include matching tables/views.
                This is a regular expression that's used to determine which
                tables/views to include.  <br>For example: <code>-i "(.*book.*)|(library.*)"</code> 
                includes only those tables/views with 'book' in their names or that start with 'library'.<br>
                You might want to use -desc with this option to describe the subset of tables.
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="param"><code>-x columnNamesRegex</code></td>
            <td class="param">Exclude matching columns from relationship analysis to
                simplify the generated graphs.
                This is a regular expression that's used to determine which
                columns to exclude.  It must match table name, followed by a dot, followed by
                column name.<br/>For example: <code>-x "(book.isbn)|(borrower.address)"</code><br/>
                Note that each column name regular expression must be surround by <code>()</code>'s and
                separated from other column names by a <code>|</code>.
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="param"><code>-ahic</code></td>
            <td class="param"><em>A</em>llow <em>H</em>TML <em>I</em>n <em>C</em>omments.<br/>
                Any HTML embedded in comments normally gets encoded so that it's rendered as text.  
                This option allows it to be rendered as HTML.</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="param"><code>-cid</code></td>
            <td class="param"><em>C</em>omments <em>I</em>nitially <em>D</em>isplayed.<br/>
                Column comments are normally hidden by default.  This option displays them by default.</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="param"><code>-notablecomments</code></td>
            <td class="param">Don't display table-based comments.
            	for databases like MySQL that stuff unrelated data where comments belong.</td>
          </tr>
          <!-- 
          <tr>
            <td>&nbsp;</td>
            <td class="param"><code>-norows</code></td>
            <td class="param">Don't display row counts.</td>
          </tr>
           -->
          <tr>
            <td>&nbsp;</td>
            <td class="param"><code>-noimplied</code></td>
            <td class="param">Don't include implied foreign key relationships in the generated table details</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="param"><code>-nohtml</code></td>
            <td class="param">Only generate files needed for insertion/deletion of data (e.g. for scripts)</td>
          </tr>
        </tbody>
      </table>
      * denotes required parameter.
      <br/>
      <p>Here are the currently supported database types.
         Use <code>java -jar schemaSpy.jar -dbhelp</code> for a complete list of the supported
         database types and the parameters that each one requires.</p>
      <table class="params" border="1" cellpadding="0" cellspacing="1">
        <tbody>
          <tr>
            <th class="param">Type</th>
            <th class="param">Description</th>
          </tr>
          <tr>
            <td class="dbType">db2</td>
            <td class="param">IBM DB2 with the 'App' Driver</td>
          </tr>
          <tr>
            <td class="dbType">db2net</td>
            <td class="param">IBM DB2 with the 'Net' Driver</td>
          </tr>
          <tr>
            <td class="dbType">firebird</td>
            <td class="param">Firebird</td>
          </tr>
          <tr>
            <td class="dbType">hsqldb</td>
            <td class="param">HSQLDB Server</td>
          </tr>
          <tr>
            <td class="dbType">mssql</td>
            <td class="param">Microsoft SQL Server</td>
          </tr>
          <tr>
            <td class="dbType">mysql</td>
            <td class="param">MySQL</td>
          </tr>
          <tr>
            <td class="dbType">ora</td>
            <td class="param">Oracle with OCI8 Driver</td>
          </tr>
          <tr>
            <td class="dbType">orathin</td>
            <td class="param">Oracle with Thin Driver</td>
          </tr>
          <tr>
            <td class="dbType">pgsql</td>
            <td class="param">PostgreSQL</td>
          </tr>
          <tr>
            <td class="dbType">sybase</td>
            <td class="param">Sybase Server with JDBC3 Driver</td>
          </tr>
          <tr>
            <td class="dbType">sybase2</td>
            <td class="param">Sybase Server with JDBC2 Driver</td>
          </tr>
          <tr>
            <td class="dbType">udbt4</td>
            <td class="param">DB2 UDB Type 4 Driver</td>
          </tr>
        </tbody>
      </table>
      <br>
      <p>A MySQL example:</p>
      <table>
        <tbody>
          <tr>
            <td>&nbsp;
            <code>java -jar schemaSpy.jar -t mysql -o library -host localhost -db library -u user -p password -notablecomments</code>
            </td>
          </tr>
        </tbody>
      </table>
      <p/>will create a series of files in the <code>library</code>
      directory that give the details of the schema in the database <code>library</code>.
      This is what I used to generate the <a href="http://schemaspy.sourceforge.net/sample">sample output</a>.
      <p>An MS SQL Server example:</p>
      <table>
        <tbody>
          <tr>
            <td>&nbsp;
            <code>java -jar schemaSpy.jar -t mssql -db library -host localhost -port 1433 -u user -p password -o library</code>
            </td>
          </tr>
        </tbody>
      </table>
      <p/>does the same thing as the MySQL example, but specifies an <code>mssql</code>
      database type with MS SQL Server-specific database connection parameters.
      <hr/>
      Some information about the developer, John Currier, is available <a href="http://john.currier.googlepages.com">here</a>.
      <br/>Feedback on <a href="https://sourceforge.net/tracker/?group_id=137197&atid=737987">problems</a> and/or <a href="https://sourceforge.net/forum/?group_id=137197">enhancements</a> is appreciated.
      <span style="float: right;">
      <!-- Site Meter -->
      <script type="text/javascript" src="http://s28.sitemeter.com/js/counter.js?site=s28schemaspy">
      </script>
      <noscript>
      <a href="http://s28.sitemeter.com/stats.asp?site=s28schemaspy" target="_top">
      <img src="http://s28.sitemeter.com/meter.asp?site=s28schemaspy" alt="Site Meter" border="0"/></a>
      </noscript>
      <!-- Copyright (c)2006 Site Meter -->
      </span>
</body>
</html>
