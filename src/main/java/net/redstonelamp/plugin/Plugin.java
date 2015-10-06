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

import lombok.Getter;
import lombok.Setter;
import net.redstonelamp.Server;
import net.redstonelamp.ui.Logger;

public abstract class Plugin {
	
	@Setter
	@Getter
	public PluginState state;
	
	/**
	 * The plugin's server
	 */
	public abstract Server getServer();

	/**
	 * The plugins name
	 */
	public abstract String getName();

	/**
	 * The plugins version
	 */
	public abstract String getVersion();

	/**
	 * The plugins authors
	 */
	public abstract String[] getAuthors();

	/**
	 * The plugins website
	 */
	public abstract String getUrl();
	
	/**
	 * The plugins required depdencies to run
	 */
	public abstract String[] getDependencies();
	
	/**
	 * The plugins recommended depencies which aren't required to run
	 */
	public abstract String[] getSoftDependencies();

	/**
	 * The plugin's logger
	 */
	public abstract Logger getLogger();

	/**
	 * In this function plugins run everything they need to do on a fresh server
	 * start. It is careful what is called within this method, as things like
	 * RedstoneLamp.SERVER may be NULL and unusable.
	 */
	public void onLoad() {
	}

	/**
	 * In this function every plugin should handle the stuff it has to do on
	 * server start/reload. When this method gets called, the basic structure of
	 * the server already got loaded, and events and commands can already be
	 * registered in the event/command system. WARNING: This method will also be
	 * called when the server is being reloaded
	 */
	public void onEnable() {
	}

	/**
	 * In this function every plugin should stop itself. This includes removing
	 * events, commands, schedule tasks etc.
	 */
	public void onDisable() {
	}
	
	/**
     * Returns if this loader's plugin depends on the dependency
     *
     * @param depend The dependency to check
     * @return
     */
    public final boolean dependsOn(String depend){
        for(String dependency : getDependencies()){
            if(dependency.equalsIgnoreCase(depend))
            	return true;
        }
        return false;
    }
    
    /**
     * Returns if this loader's plugin soft depends on the dependency
     *
     * @param depend The dependency to check
     * @return
     */
    public final boolean softDependsOn(String depend){
        for(String dependency : getSoftDependencies()){
            if(dependency.equalsIgnoreCase(depend))
            	return true;
        }
        return false;
    }
	
	/**
	 * Used to convert a String to a String[] easily <br>
	 * Valid strings to split look like: {@code [foo, bar]} <br>
	 * Invalid strings are just returned as they were presented in an array.
	 * 
	 * @param x
	 * @return Split string
	 */
	protected final String[] yamlArray(String x) {
		if(x == null) // Do not try to handle a null value!
			return null;
		if (x.startsWith("[") && x.endsWith("]"))
			return x.substring(1, x.length() - 1).split(", ");
		else
			return new String[] { x };
	}
}
