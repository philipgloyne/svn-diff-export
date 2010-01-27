package com.primed.sde.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

/**
 * Reads and exports the content of diff.patch. It will only export the 'added' and 
 * 'modified' files, ignoring 'delete' commands (which you should deal with later in
 * the deployment process)
 * 
 * @author philip gloyne (philip.gloyne@gmail.com)
 * @since 25-JAN-2010
 */
public class Export {

	protected static String NEW_LINE = System.getProperty("line.separator");

	private final SVNUpdateClient client;
	private final File diff;
	private final String oldBranch;
	private final String newBranch;
	private final String target;
	
	/**
	 * Reads and exports the content of diff.patch.
	 * 
	 * @param client
	 * @param diff the diff.patch
	 * @param oldBranch the older branch (should be the same at the current baseline export).
	 * @param newBranch the new branch which you wish you take the baseline to.
	 * @param target the directory to output the exports
	 * @throws SVNException
	 * @throws IOException
	 */
	public Export(SVNUpdateClient client, File diff, String oldBranch, String newBranch, String target) throws SVNException, IOException {
		this.client = client;
		this.diff = diff;
		this.oldBranch = oldBranch;
		this.newBranch = newBranch;
		this.target = target;
	}
	
	/**
	 * Read and exports all added and modified files.
	 * 
	 * @throws SVNException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void execute() throws SVNException, IOException, InterruptedException {
		
		InputStream is = new FileInputStream(diff);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader buf = new BufferedReader(isr);

		String line;
		while ((line = buf.readLine()) != null) {
			export(line);
		}
		
		buf.close();
		isr.close();
		is.close();
	}
	
	/**
	 * Called for each line in the diff.patch. Exports a single file to the target.
	 * 
	 * @param change
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SVNException
	 */
	private void export(String change) throws IOException, InterruptedException, SVNException {
		String operation = change.trim().charAt(0) + "";
		String path = change.trim().substring(1).trim();
		SVNURL location = SVNURL.parseURIEncoded(path.replace(oldBranch, newBranch));
		String exportTo = target + path.replaceFirst(oldBranch, "");
		File f = new File(exportTo);
		File d = new File(f.getAbsolutePath().replaceFirst(f.getName(), ""));
		d.mkdirs();

		if (operation.equalsIgnoreCase("D")) {
			// Handle deletes if you wish, be careful of directories. 
			
		} else if (operation.equalsIgnoreCase("M") || operation.equalsIgnoreCase("A")) {
			client.doExport(location,f,SVNRevision.HEAD,SVNRevision.HEAD,"native",true,SVNDepth.EMPTY );		

		} else {
			throw new IOException("Error! Malformed operation: " + operation);
		}

	}

}
