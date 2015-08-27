/**
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

public abstract class Plugin {
	/**
	 * The plugins name
	 */
	@Getter private String name;
	/**
	 * The plugins version
	 */
	@Getter private String version;
	/**
	 * The plugins authors
	 */
	@Getter private String[] authors;
	/**
	 * The plugins website
	 */
	@Getter private String website;
	
	public Plugin(String name, String version, String[] authors, String website){
		this.name = name;
		this.version = version;
		this.authors = authors;
		this.website = website;
	}
	/**
	 * In this function every plugin should handle the stuff it has to do on server start.
	 * When this method gets called, the basic structure of the server already got loaded,
	 * and events and commands can already be registered in the event/command system.
	 * WARNING: This method will also be called when the server is being reloaded
	 */
	public abstract void onEnable();
	/**
	 * In this function every plugin should stop itself.
	 * This includes removing events, commands, schedule tasks etc.
	 */
	public abstract void onDisable();
}
