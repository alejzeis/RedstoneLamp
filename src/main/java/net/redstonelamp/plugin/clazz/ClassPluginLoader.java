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
package net.redstonelamp.plugin.clazz;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

import net.redstonelamp.plugin.Plugin;
import net.redstonelamp.plugin.PluginLoader;
import net.redstonelamp.plugin.PluginState;
import net.redstonelamp.plugin.exception.PluginException;
import net.redstonelamp.plugin.java.JavaClassPath;

public class ClassPluginLoader extends PluginLoader {

	private final HashMap<String, Plugin> loadedPlugins = new HashMap<String, Plugin>();
	private final File folder;
	private boolean loaded;

	public ClassPluginLoader(File folder) {
		this.folder = folder;
		if (!folder.exists())
			folder.mkdirs();
	}

	@SuppressWarnings("resource") // Garbage handled by java
	@Override
	public HashMap<String, Plugin> loadPlugins() throws PluginException, IOException {
		if (loaded)
			throw new PluginException("The plugins have already been loaded!");
		HashMap<String, Plugin> plugins = new HashMap<String, Plugin>();
		for (File file : folder.listFiles()) {
			if (file.getName().endsWith(".class")) {
				ClassPlugin plugin = null;
				URLClassLoader loader = new URLClassLoader(new URL[] { file.toURI().toURL() });
				try {
					Class<?> clazz = loader.loadClass(file.getName().split("\\.")[0]);
					if (clazz.isAssignableFrom(ClassPlugin.class)) {
						plugin = (ClassPlugin) clazz.newInstance();
					} else {
						throw new PluginException("Could not load a class found in the plugins folder!");
					}
				} catch (Exception e) {
					plugin = null; // Mandatory
					e.printStackTrace();
				}
				if (plugins.get(plugin.getName()) != null)
					throw new PluginException("A plugin with that name already exists!");
				if (loadedPlugins.get(plugin.getName()) == null)
					plugin.onLoad();
				if (plugin != null) {
					plugins.put(plugin.getName(), plugin);
					plugin.setState(PluginState.LOADED);
				}
			}
		}
		return plugins;
	}

	@Override
	public void unloadPlugins() {

	}

	@Override
	public void enablePlugins() {

	}

	@Override
	public void disablePlugins() {

	}

}
