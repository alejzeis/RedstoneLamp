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
import java.net.URL;
import java.net.URLClassLoader;

import lombok.Getter;
import net.redstonelamp.Server;
import net.redstonelamp.plugin.Plugin;
import net.redstonelamp.plugin.exception.PluginDescriptorException;
import net.redstonelamp.plugin.exception.PluginException;
import net.redstonelamp.ui.Log4j2ConsoleOut;
import net.redstonelamp.ui.Logger;

public class ClazzPlugin extends Plugin {
	
	private final Class<?> clazz;
	private final Object instance;
	private final ClassPlugin plugin;
	
	@Getter
	private final Server server;
	@Getter
	private final String name;
	@Getter
	private final String version;
	@Getter
	private final String[] authors;
	@Getter
	private final String url;
	@Getter
	private final String[] dependencies;
	@Getter
	private final String[] softDependencies;
	@Getter
	private final Logger logger;
	
	@SuppressWarnings("resource") // Java garbage collector handles ClassLoaders
	public ClazzPlugin(Server server, File clazzFile) throws Exception {
		URLClassLoader loader = new URLClassLoader(new URL[] { clazzFile.getParentFile().toURI().toURL() });
		this.clazz = loader.loadClass(clazzFile.getName().split("\\.")[0]);
		this.plugin = clazz.getAnnotation(ClassPlugin.class);
		this.instance = clazz.newInstance();
		
		// Load variables
		this.server = server;
		this.name = plugin.name();
		this.version = plugin.version();
		this.authors = this.yamlArray(plugin.author());
		this.url = plugin.url();
		this.dependencies = this.yamlArray(plugin.dependency());
		this.softDependencies = this.yamlArray(plugin.softDependency());
		this.logger = new Logger(new Log4j2ConsoleOut(name));
		
		// Make sure all needed variables are not null
		try {
			if(this.name == null)
				throw new PluginDescriptorException("name");
			if(this.version == null)
				throw new PluginDescriptorException("version");
			if(this.authors == null)
				throw new PluginDescriptorException("authors");
		} catch(PluginException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void main(String[] args) throws Exception {
		new ClazzPlugin(null, new File("C:/Users/Trent/Desktop/TestPlugin.class"));
	}

}
