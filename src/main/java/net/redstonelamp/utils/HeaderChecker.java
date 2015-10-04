/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.redstonelamp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

/**
 * This class is built to go through all of the source code and make sure it has
 * the required header for the build to pass
 * 
 * @author RedstoneLamp team
 */
public abstract class HeaderChecker {

	private static final File srcDir = new File(new File(System.getProperty("user.dir")), "src/main/java");
	private static final String fileType = ".java";

	public static final void main(String[] args) throws IOException {
		System.out.println("Starting header check in directory " + srcDir.getPath() + "\n");
		String header = IOUtils.toString(HeaderChecker.class.getResource("/header.txt").openStream());
		boolean missed = false;
		for (File f : getFiles(srcDir, new ArrayList<File>())) {
			String fileString = IOUtils.toString(new FileInputStream(f));
			if (!fileString.startsWith(header)) {
				System.out.println(f.getPath().substring((int) srcDir.getPath().length() + 1, f.getPath().length() - fileType.length()).replace("\\", "."));
				missed = true;
			}
		}
		System.out.println((missed ? "The files above are missing their headers, fix them before pushing to github!"
				: "All files have their headers, go on and push to github!"));
	}

	/**
	 * Used by the HeaderChecker to get all the files and sub directories of
	 * every directory
	 * 
	 * @param directory
	 * @param files
	 * @return List of files and the sub directories of every directory
	 */
	private static final File[] getFiles(File directory, ArrayList<File> files) {
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				if (file.getName().endsWith(fileType))
					files.add(file);
			} else if (file.isDirectory()) {
				getFiles(file, files);
			}
		}
		return files.toArray(new File[files.size()]);
	}

}
