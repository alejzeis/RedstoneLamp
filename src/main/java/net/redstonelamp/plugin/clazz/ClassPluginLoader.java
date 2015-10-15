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
