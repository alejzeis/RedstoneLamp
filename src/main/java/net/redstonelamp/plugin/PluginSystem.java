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
package net.redstonelamp.plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import net.redstonelamp.Server;
import net.redstonelamp.plugin.exception.PluginException;
import net.redstonelamp.plugin.java.JavaPluginLoader;
import net.redstonelamp.plugin.java.JavaPluginManager;
import net.redstonelamp.ui.Log4j2ConsoleOut;
import net.redstonelamp.ui.Logger;

public final class PluginSystem {
	
	@Getter
	private final Server server;
	private final Logger logger;
	private final ArrayList<String> exempt;
	private final HashMap<String, PluginManager> managers;
	
	public PluginSystem(Server server) {
		this.server = server;
		this.logger = new Logger(new Log4j2ConsoleOut("PluginSystem"));
		this.exempt = new ArrayList<String>();
		this.managers = new HashMap<String, PluginManager>();
	}
	
	public void init() throws IOException {
		File plugins = new File("plugins");
		JavaPluginLoader javaLoader = new JavaPluginLoader(plugins);
		JavaPluginManager javaManager = new JavaPluginManager(javaLoader);
		this.addPluginManager(javaManager);
		logger.info("Loaded PluginSystem and registered default PluginManagers!");
	}
	
	public void addPluginManager(PluginManager manager) throws IOException {
		if(managers.get(manager.getFileType()) != null)
			throw new IOException("A manager that handles that filetype already exists!");
		managers.put(manager.getFileType(), manager);
	}
	
	public final void removePluginManager(PluginManager manager) throws IOException {
		if(exempt.contains(manager.getFileType()))
			throw new IOException("This type of manager can not be tampered with!");
		managers.remove(manager.getFileType(), manager);
	}
	
	public PluginManager[] getPluginManagers() {
		return managers.values().toArray(new PluginManager[managers.size()]);
	}
	
	public PluginManager getPluginManager(String filetype) {
		if(!filetype.startsWith("."))
			filetype = ("." + filetype);
		return managers.get(filetype);
	}
	
	/**
	 * Used to tell all PluginManagers to load their plugins
	 * 
	 * @throws PluginException
	 * @throws IOException
	 */
	public final void loadPlugins() throws PluginException, IOException {
		for(PluginManager manager : managers.values())
			manager.loadPlugins();
	}
	
	/**
	 * Used to tell all PluginManagers to unload their plugins
	 */
	public final void unloadPlugins() {
		for(PluginManager manager : managers.values())
			manager.unloadPlugins();
	}
	
	
	/**
	 * Used to enable all plugins no matter their PluginManager
	 * 
	 * @throws PluginException
	 * @throws IOException
	 */
	public final void enablePlugins() throws PluginException, IOException {
		for(PluginManager manager : managers.values()) {
			for(Plugin plugin : manager.getPlugins())
				plugin.onEnable();
		}
	}
	
	/**
	 * Used to disable all plugins no matter their PluginManager
	 * 
	 * @throws PluginException
	 * @throws IOException
	 */
	public final void disablePlugins() {
		for(String key : managers.keySet()) {
			PluginManager manager = managers.get(key);
			for(Plugin plugin : manager.getPlugins())
				plugin.onDisable();
			managers.remove(key);
		}
	}
	
}
