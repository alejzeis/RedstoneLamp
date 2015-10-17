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

import java.io.IOException;

import net.redstonelamp.event.Event;
import net.redstonelamp.event.EventPlatform;
import net.redstonelamp.plugin.exception.AsyncAPICallException;
import net.redstonelamp.plugin.exception.PluginException;

public abstract class PluginManager {
	
	private static long THREAD_ID;
	public PluginManager() {
		THREAD_ID = Thread.currentThread().getId();
	}
	
	/**
	 * Gets type of file that the plugin managers work with
	 */
	public abstract String getFileType();
	
	/**
	 * Loads all plugins managed by this plugin manager
	 */
	public abstract void loadPlugins() throws PluginException, IOException;
	
	/**
	 * Unloads all plugins managed by this plugin manager
	 */
	public abstract void unloadPlugins();
	
	/**
	 * Get all plugins being managed by this plugin manager
	 */
	public abstract Plugin[] getPlugins();
	
	/**
	 * Call event for the specified platforms
	 * @param platform
	 * @param event
	 */
	public abstract void callEvent(EventPlatform platform, Event event);
	
	/**
	 * Call event for both platforms
	 * @param event
	 */
	public final void callEvent(Event event) {
		this.callEvent(EventPlatform.BOTH, event);
	}
	
	public final Plugin getPlugin(String name) {
		for(Plugin plugin : getPlugins()) {
			if(plugin.getName().equals(name))
				return plugin;
		}
		return null;
	}

	/**
	 * Checks if a task is running async. Throws an AsyncAPICallException when
	 * the task is async.
	 *
	 * @param msg
	 *            The message to use in the AsyncAPICallException
	 * @throws AsyncAPICallException
	 */
	public static void checkAsync(String msg) throws AsyncAPICallException {
		if (Thread.currentThread().getId() != THREAD_ID) {
			throw new AsyncAPICallException(msg);
		}
	}
}
