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

import java.util.ArrayList;

import lombok.Getter;
import net.redstonelamp.plugin.exception.AsyncAPICallException;

public abstract class PluginManager {
	/**
	 * Main thread id - to catch async access by plugins and block it
	 */
	@Getter private static long THREAD_ID = -1;
	/**
	 * ArrayList of plugins/pluginloaders managed by this manager
	 */
	@Getter private ArrayList<PluginLoader> pluginLoaders = new ArrayList<>();
	
	/**
	 * Sets some basic values. This method has to be called from the main thread!
	 */
	public static void init(){
		//Store the main thread id
		THREAD_ID = Thread.currentThread().getId();
	}
	
	/**
	 * Loads all plugins managed by this plugin manager
	 */
	public abstract void loadPlugins();
	/**
	 * Checks if a task is running async. Throws an AsyncAPICallException when the task is async.
	 * @param msg The message to use in the AsyncAPICallException
	 * @throws AsyncAPICallException
	 */
	public static void checkAsync(String msg) throws AsyncAPICallException{
		if(Thread.currentThread().getId()!=THREAD_ID){
			throw new AsyncAPICallException(msg);
		}
	}
}
