package com.primed.sde.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Creates a .zip file of the target directory with the same
 * name as the target you specified.
 * 
 * @author philip gloyne (philip.gloyne@gmail.com)
 * @since 25-JAN-2010
 */
public class Zip {

	private final File target;

	/**
	 * Creates a .zip file of the target directory.
	 * 
	 * @param target
	 */
	public Zip(File target) {
		this.target = target;
	}

	/**
	 * Performs the 'zipping' of a directory and leaves the new .zip file
	 * in the same directory as the target. Equivalent to windows right-click
	 * create zip.
	 * 
	 * @throws IOException
	 */
	public void execute() throws IOException {
		String zipPath = target.getParentFile().getPath() + "/";
		String zipName = target.getName() + ".zip";
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream( zipPath + zipName ));
		zip(target, target, zos);
		zos.close();
	}

	/**
	 * Recursive zip method.
	 * 
	 * @param directory
	 * @param base
	 * @param zos
	 * @throws IOException
	 */
	private static final void zip(File directory, File base, ZipOutputStream zos)
			throws IOException {
		File[] files = directory.listFiles();
		byte[] buffer = new byte[8192];
		int read = 0;
		for (int i = 0, n = files.length; i < n; i++) {
			if (files[i].isDirectory()) {
				zip(files[i], base, zos);
			} else {
				FileInputStream in = new FileInputStream(files[i]);
				ZipEntry entry = new ZipEntry(files[i].getPath().substring(
						base.getPath().length() + 1));
				zos.putNextEntry(entry);
				while (-1 != (read = in.read(buffer))) {
					zos.write(buffer, 0, read);
				}
				in.close();
			}
		}
	}

}
