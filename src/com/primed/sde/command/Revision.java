package com.primed.sde.command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;

/**
 * Creates a revision file. The revision file contains information about the
 * latest branch you have deployed (should be tagged). It contains the
 * branch name, revision number and last commit datetime.
 * 
 * @author philip gloyne (philip.gloyne@gmail.com)
 * @since 25-JAN-2010
 */
public class Revision {
	
	private static final String NEW_LINE = System.getProperty("line.separator");
	
	private final SVNWCClient client;
	private final SVNURL branch; 
	private final String target;
	
	public Revision(SVNWCClient client, SVNURL branch, String target) {
		super();
		this.client = client;
		this.branch = branch;
		this.target = target;
	}
	
	/**
	 * Creates a revision file.
	 * 
	 * @throws SVNException
	 * @throws IOException
	 */
	public void execute() throws SVNException, IOException {
		SVNInfo info = client.doInfo(branch, SVNRevision.HEAD , SVNRevision.HEAD);
		new RevisionFile(target, info);		
	}
	
	/**
	 * Creates a plain text 'revision' file that contains the last commit details
	 * of a branch you specify.
	 */
	class RevisionFile extends File {
		
		private static final long serialVersionUID = -850805471980707152L;

		RevisionFile(String pathname, SVNInfo info) throws IOException {
			super(pathname);
			createNewFile();
			Writer output = new BufferedWriter(new FileWriter(this));
			StringBuffer sb = new StringBuffer();
			String branch = info.getURL().toString().replaceFirst("http://.*/", "");
			sb.append("Branch:   ").append(branch).append(NEW_LINE);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
			String updated = sdf.format(info.getCommittedDate());
			sb.append("Updated:  ").append(updated).append(NEW_LINE);
			String revision = info.getCommittedRevision().toString();
			sb.append("Revision: ").append(revision).append(NEW_LINE);
			
			output.write(sb.toString());
			output.close();
		}
	}
}
