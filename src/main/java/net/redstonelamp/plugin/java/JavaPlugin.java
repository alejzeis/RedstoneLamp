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
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import com.esotericsoftware.yamlbeans.YamlReader;

import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.Server;
import net.redstonelamp.plugin.Plugin;
import net.redstonelamp.ui.Log4j2ConsoleOut;
import net.redstonelamp.ui.Logger;

public class JavaPlugin extends Plugin {
	
	private Logger logger;
	HashMap<String, Object> map;
	
	@SuppressWarnings("unchecked")
	private final HashMap<String, Object> yaml() {
		if(map == null) {
			try {
				YamlReader reader = new YamlReader(IOUtils.toString(this.getClass().getResource("/plugin.yml").openStream()));
				this.map = (HashMap<String, Object>) reader.read();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	@Override
	public Server getServer() {
		return RedstoneLamp.SERVER;
	}

	@Override
	public String getName() {
		return (String) yaml().get("name");
	}

	@Override
	public String getVersion() {
		return (String) yaml().get("name");
	}

	@Override
	public String[] getAuthors() {
		return this.yamlArray((String) yaml().get("author"));
	}

	@Override
	public String getUrl() {
		return (String) map.get("url");
	}

	@Override
	public String[] getDependencies() {
		return this.yamlArray((String) yaml().get("dependency"));
	}

	@Override
	public String[] getSoftDependencies() {
		return this.yamlArray((String) yaml().get("softdepdency"));
	}

	@Override
	public Logger getLogger() {
		if(logger == null)
			logger = new Logger(new Log4j2ConsoleOut(getName()));
		return logger;
	}
	
	public String getMain() {
		return (String) yaml().get("main");
	}
	
}
