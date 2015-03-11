# SchemaSpy for BETYdb
This is a clone of the original sourceforge repo at https://schemaspy.svn.sourceforge.net/svnroot/schemaspy

The source code is modified to generate a documentation of [BETYdb](https://www.betydb.org/)

Not intended to be used for any other databases.

The documentation generated here is not based on the current bety database, but my local copy. The difference is that I have added some of the foreign key constraints (otherwise schemaSpy won't be able to detect any relationship or create diagrams). Currently this application generates multiple pages of output, but I think the only useful ones are index.html, relationship.html and the pages for each table inside the "table" folder. In order to keep it simple I removed some output such as 'parent', 'children' and the one-degree diagrams in the table documentation pages and added some extra conditions for displaying information.

## SchemaSpy

To compile: 
1. <code>cd</code> into the directory containing the source files
2. output all the filenames into a .txt file using, for example<code>ls > sources.txt</code>
3. compile using <code>javac @sources.txt</code>

To compress into .jar:
1. <code>cd</code> into the directory containing the source files
2. <code>ls > sources.txt</code>
3. compress using <code>jar cmf META-INF/MANIFEST.MF out.jar @sources.txt</code>
note: META-INF/MANIFEST.MF is the location of the existing manifest file

