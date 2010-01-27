package com.primed.sde;

import junit.framework.Assert;

import org.junit.Test;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * Test SVNKit SVNWCClient
 * @author philip gloyne
 */
public class SvnInfoTest {
	
	@Test
	public void getRevision() throws SVNException {
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		BasicAuthenticationManager bam = new BasicAuthenticationManager("robot", "robot");
		SVNWCClient client = new SVNWCClient(bam, options);
		DAVRepositoryFactory.setup();
		SVNURL url = SVNURL.create("http", null, "<svn host>", 80, "<svn root path>", false);
		SVNInfo info = client.doInfo(url, SVNRevision.HEAD, SVNRevision.HEAD);
		Assert.assertTrue(info.getRevision().getNumber() > 0);
		System.out.println("Current revision: "+ info.getRevision());
	}

}
