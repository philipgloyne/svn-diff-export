package com.primed.sde;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Svn properties.
 * For read-only operations you may omit a .properties file if you have a
 * read-only user setup with the username:robot, password:robot.
 * 
 * @author philip gloyne (philip.gloyne@gmail.com)
 * @since 25-JAN-2010
 */
public class SvnProperties extends Properties {

	private static final long serialVersionUID = -2243343266732814909L;
	
	public SvnProperties() {}
	
	public SvnProperties(File propsFile) throws IOException {
		try {
			load(new FileInputStream(propsFile));
		} catch (FileNotFoundException e) {
			System.out.println("svn.properties file not found. Using user: robot, pw: robot.");
		}
	}
	
	/**
	 * Defaults to 'robot'.
	 * @return the svn username.
	 */
	public String getSvnUsername() {
		return getProperty("svn.username", "robot");
	}
	
	/**
	 * Defaults to 'robot'.
	 * @return the svn password.
	 */
	public String getSvnPassword() {
		return getProperty("svn.password", "robot");
	}
}
