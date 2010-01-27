package com.primed.sde.command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.ISVNDiffStatusHandler;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNDiffStatus;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatusType;

/**
 * Performs a svn diff --summarize --old <oldBranch> --new <newBranch> and outputs 
 * to target with the name diff.patch. In addition it adds a diff-info.properties 
 * to the same folder which contains the branches that were compared.
 * 
 * @author philip gloyne (philip.gloyne@gmail.com)
 * @since 25-JAN-2010
 */
public class Diff implements ISVNDiffStatusHandler {

	private static final String DIFF_INFO = "/diff-info.properties";

	protected static String NEW_LINE = System.getProperty("line.separator");
	
	private List<String> changes;

	private final SVNDiffClient client;
	private final SVNURL oldBranch;
	private final SVNURL newBranch;
	private final String diff;
	
	/**
	 * Performs a svn diff summerize.
	 * 
	 * @param client
	 * @param oldBranch the full branch/tag url
	 * @param newBranch the full branch/tag url
	 * @param diff
	 * @throws SVNException
	 * @throws IOException
	 */
	public Diff(SVNDiffClient client, SVNURL oldBranch, SVNURL newBranch, String diff) throws SVNException, IOException {
		this.client = client;
		this.oldBranch = oldBranch;
		this.newBranch = newBranch;
		this.diff = diff;
		changes = new ArrayList<String>();
	}
	
	/**
	 * Performs a diff on the HEAD revisions of two branches.
	 * 
	 * @throws SVNException
	 * @throws IOException
	 */
	public void execute() throws SVNException, IOException {
		client.doDiffStatus(oldBranch, SVNRevision.HEAD, newBranch, SVNRevision.HEAD, SVNDepth.INFINITY, false, this);
		File diffFile = new DiffFile(diff,changes);
		new DiffInfoFile(diffFile.getParent()+DIFF_INFO);
	}
	
	public void handleDiffStatus(SVNDiffStatus svnDiffStatus) throws SVNException {
		if( svnDiffStatus.getModificationType().equals(SVNStatusType.STATUS_MODIFIED) || 
				svnDiffStatus.getModificationType().equals(SVNStatusType.STATUS_ADDED) ||
				svnDiffStatus.getModificationType().equals(SVNStatusType.STATUS_DELETED)) {
		
				changes.add(encodeStatus(svnDiffStatus.getModificationType())+" "+svnDiffStatus.getURL());
		}
	}
	
	/**
	 * Encodes modified to 'M', added to 'A' and deleted to 'D'.
	 * @param modificationType
	 * @return
	 */
	private String encodeStatus(SVNStatusType modificationType) {
		if (modificationType.equals(SVNStatusType.STATUS_MODIFIED)) 
			return "M";
		if (modificationType.equals(SVNStatusType.STATUS_ADDED)) 
			return "A";
		if (modificationType.equals(SVNStatusType.STATUS_DELETED)) 
			return "D";
		return "?";
	}

	/**
	 * Creates a file equivalent to a svn diff --summarize command.
	 */
	class DiffFile extends File {
		
		private static final long serialVersionUID = -850805471980707152L;

		DiffFile(String pathname, List<String> changes) throws IOException {
			super(pathname);
			createNewFile();
			Writer output = new BufferedWriter(new FileWriter(this));
			for (String change : changes) {
				output.write(change);
				output.write(NEW_LINE);
			}
			output.close();
		}
	}
	
	/**
	 * Creates a diff-info.properties file which specifies which two branches
	 * were compared when creating the diff.patch.
	 */
	class DiffInfoFile extends File {

		private static final long serialVersionUID = 3441114164356456471L;

		DiffInfoFile(String pathname) throws IOException {
			super(pathname);
			createNewFile();
			Writer output = new BufferedWriter(new FileWriter(this));
			output.write("old-branch=");
			output.write(oldBranch.getURIEncodedPath());
			output.write(NEW_LINE);
			output.write("new-branch=");
			output.write(newBranch.getURIEncodedPath());
			output.write(NEW_LINE);
			output.close();
		}
	}

}
