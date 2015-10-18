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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import com.esotericsoftware.yamlbeans.YamlReader;

import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.plugin.Plugin;
import net.redstonelamp.plugin.PluginLoader;
import net.redstonelamp.plugin.PluginState;
import net.redstonelamp.plugin.exception.PluginDescriptorException;
import net.redstonelamp.plugin.exception.PluginException;

public class JavaPluginLoader extends PluginLoader {

	private final HashMap<String, Plugin> loadedPlugins = new HashMap<String, Plugin>();
	private final File folder;
	private boolean loaded;

	public JavaPluginLoader(File folder) {
		this.folder = folder;
		if (!folder.exists())
			folder.mkdirs();
	}

	@SuppressWarnings({ "unchecked", "resource" }) // Java handles garbage
													// collector from
													// ClassLoader
	@Override
	public HashMap<String, Plugin> loadPlugins() throws PluginException, IOException {
		if (loaded)
			throw new PluginException("The plugins have already been loaded!");
		HashMap<String, Plugin> plugins = new HashMap<String, Plugin>();
		HashMap<Plugin, String> mains = new HashMap<Plugin, String>(); // Debugging,
																		// only
																		// for
																		// loading
																		// plugins.
		HashMap<Plugin, File> files = new HashMap<Plugin, File>(); // Debugging,
																	// only for
																	// adding
																	// dependencies.
		for (File file : folder.listFiles()) {
			if (file.getName().endsWith(".jar")) {
				JavaPlugin plugin = null;
				try {
					URLClassLoader loader = new URLClassLoader(new URL[] { file.toURI().toURL() });
					HashMap<String, Object> yaml = (HashMap<String, Object>) new YamlReader(
							IOUtils.toString(loader.getResource("plugin.yml").openStream())).read();

					// Make sure all needed variables are not null
					String main = (String) yaml.get("main");
					try {
						if (yaml.get("name") == null)
							throw new PluginDescriptorException("name");
						if (yaml.get("version") == null)
							throw new PluginDescriptorException("version");
						if (yaml.get("author") == null)
							throw new PluginDescriptorException("author");
						if (yaml.get("main") == null)
							throw new PluginDescriptorException("main");

						plugin = (JavaPlugin) loader.loadClass(main).newInstance();
					} catch (PluginException | InstantiationException | IllegalAccessException
							| ClassNotFoundException e) {
						e.printStackTrace();
					}
					if (plugins.get(plugin.getName()) != null)
						throw new PluginException("A plugin with that name already exists!");
					if (mains.get(plugin) != null)
						throw new PluginException("A plugin with that main already exists!");
					if (loadedPlugins.get(plugin.getName()) == null)
						plugin.onLoad();
				} catch (PluginException | IOException e) {
					plugin = null; // Mandatory
					throw e; // Still throw for optional things
				}
				if (plugin != null) {
					if(plugins.get(plugin.getName()) != null)
						throw new PluginException("There are multiple plugins with the name \"" + plugin.getName() + "\"!");
					plugins.put(plugin.getName(), plugin);
					mains.put(plugin, plugin.getName());
					files.put(plugin, file);
					plugin.setState(PluginState.LOADED);
				}
			}
		}

		// Make sure all plugins have their dependencies and there
		for (Plugin plugin : plugins.values()) {
			if (plugin.getDependencies() != null) {
				for (String dependency : plugin.getDependencies()) {
					Plugin depend = plugins.get(dependency);
					if (plugins.get(dependency) != null) {
						JavaClassPath.addFile(plugin.getClass().getClassLoader(), files.get(depend));
						System.out.println(plugin.getClass().getName());
					} else
						throw new PluginException(plugin.getName() + " is missing the plugin dependency " + dependency);
				}
			}
			if (plugin.getSoftDependencies() != null) {
				for (String softDependency : plugin.getSoftDependencies()) {
					Plugin softDepend = plugins.get(softDependency);
					if (plugins.get(softDependency) != null)
						JavaClassPath.addFile(mains.get(softDepend).getClass().getClassLoader(), files.get(softDepend));
					else
						RedstoneLamp.SERVER.getLogger().warning(plugin.getName()
								+ " is missing a recommended but not needed dependency " + softDependency);
				}
			}
			loadedPlugins.put(plugin.getName(), plugin);
			plugin.setState(PluginState.INITIALIZED);
		}
		return plugins;
	}

	@Override
	public void unloadPlugins() {
		loadedPlugins.clear();
	}

	@Override
	public void enablePlugins() {
		for (Plugin plugin : loadedPlugins.values()) {
			if (plugin.getState() != PluginState.ENABLED)
				plugin.onEnable();
			plugin.setState(PluginState.ENABLED);
		}
	}

	@Override
	public void disablePlugins() {
		for (Plugin plugin : loadedPlugins.values()) {
			if (plugin.getState() != PluginState.DISABLED)
				plugin.onDisable();
			plugin.setState(PluginState.DISABLED);
		}
	}

}
