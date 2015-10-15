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
package net.redstonelamp.plugin.java;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Used to add plugin's and extra files to the system classpath making libraries
 * such as ProtocolLib (If it were to be ported to RedstoneLamp) accessible by
 * other plugins
 * 
 * @author RedstoneLamp Team
 */
public class JavaClassPath {
	private static final Class<?>[] parameters = new Class[] { URL.class };

	public static void addFile(ClassLoader loader, String path) throws IOException {
		File file = new File(path);
		addFile(loader, file);
	}

	public static void addFile(ClassLoader loader, File file) throws IOException {
		addURL(loader, file.toURI().toURL());
	}

	public static void addURL(ClassLoader loader, URL url) throws IOException {
		Class<URLClassLoader> sysclass = URLClassLoader.class;

		try {
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(loader, url);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IOException("Error, could not add URL to system classloader");
		}

	}
}
