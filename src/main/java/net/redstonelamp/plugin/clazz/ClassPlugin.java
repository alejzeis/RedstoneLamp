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

import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.Server;
import net.redstonelamp.plugin.Plugin;
import net.redstonelamp.ui.Log4j2ConsoleOut;
import net.redstonelamp.ui.Logger;

public abstract class ClassPlugin extends Plugin {
	
	private Logger logger;
	
	abstract String name();
	abstract String version();
	abstract String[] authors();
	String url() {
		return null;
	}
	String[] dependencies() {
		return null;
	}
	String[] softDependencies() {
		return null;
	}
	
	@Override
	public final Server getServer() {
		return RedstoneLamp.SERVER;
	}
	
	@Override
	public String getName() {
		return name();
	}
	
	@Override
	public final String getVersion() {
		return version();
	}
	
	@Override
	public final String[] getAuthors() {
		return authors();
	}
	
	@Override
	public final String getUrl() {
		return url();
	}
	
	@Override
	public final String[] getDependencies() {
		return dependencies();
	}

	@Override
	public final String[] getSoftDependencies() {
		return softDependencies();
	}

	@Override
	public final Logger getLogger() {
		if(logger == null)
			logger = new Logger(new Log4j2ConsoleOut(getName()));
		return logger;
	}
	
}
