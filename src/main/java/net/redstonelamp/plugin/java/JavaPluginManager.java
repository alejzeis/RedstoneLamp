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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.redstonelamp.event.Listener;
import net.redstonelamp.plugin.Plugin;
import net.redstonelamp.plugin.PluginManager;
import net.redstonelamp.plugin.exception.PluginException;

public class JavaPluginManager extends PluginManager {
	
	private final JavaPluginLoader loader;
	private final HashMap<String, JavaPlugin> plugins;
	private final HashMap<JavaPlugin, ArrayList<Listener>> listeners;

	public JavaPluginManager(JavaPluginLoader loader) throws IOException {
		this.loader = loader;
		this.plugins = new HashMap<String, JavaPlugin>();
		this.listeners = new HashMap<JavaPlugin, ArrayList<Listener>>();
	}

	@Override
	public String getFileType() {
		return ".jar";
	}

	@Override
	public void loadPlugins() throws PluginException, IOException {
		for (Plugin plugin : loader.loadPlugins().values())
			plugins.put(plugin.getName(), (JavaPlugin) plugin);
	}

	@Override
	public void unloadPlugins() {
		loader.unloadPlugins();
		plugins.clear();
	}

	public void registerEvents(JavaPlugin plugin, Listener listener) {
		if (listeners.get(plugin) == null)
			listeners.put(plugin, new ArrayList<Listener>());
		listeners.get(plugin).add(listener);
	}

	public void unregisterEvents(JavaPlugin plugin, Listener listener) {
		if (listeners.get(plugin) == null)
			listeners.put(plugin, new ArrayList<Listener>());
		listeners.get(plugin).remove(listener);
	}

	@Override
	public Plugin[] getPlugins() {
		return plugins.values().toArray(new Plugin[plugins.size()]);
	}

	public Listener[] getListeners() {
		ArrayList<Listener> arr = new ArrayList<Listener>();
		for (Plugin plugin : getPlugins()) {
			for (Listener listener : listeners.get(plugin))
				arr.add(listener);
		}
		return arr.toArray(new Listener[arr.size()]);
	}

}
