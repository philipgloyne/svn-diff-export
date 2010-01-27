---------------
Svn Diff Export
---------------
Date: 25-JAN-2010
Author: Philip Gloyne (philip.gloyne@gmail.com)

ABOUT
-----
Patch a baseline svn export to a newer branch. Allows a user to chain multiple svn commands: 
	
	svn diff --summarize | svn export <each file> | zip <dir>


WHY?
----
We use it to patch a large deployed project on a server we can't directly export to.


EXAMPLE USAGE:
--------------
Can be found in releases/1.0/svn-diff-export.bat.example


COMMANDS:
---------

Create a diff.patch file
java -jar svn-diff-export-1.0.jar diff <old-branch-url> <new-branch-url> <diff-file>

Export each of the files described in the diff.patch to a target directory
java -jar svn-diff-export-1.0.jar export <diff-file> <old-branch-url> <new-branch-url> <target-dir>

Create a revision file (usually in a public folder - we put ours next to robots.txt)
java -jar svn-diff-export-1.0.jar revision <new-branch-url> <revision-file-full-path>

Create a .zip of the 'export' folder to push to the server
java -jar svn-diff-export-1.0.jar zip <directory-to-zip>


TODO
----
Unit tests. Tidy up docs.