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

import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.Server;
import net.redstonelamp.plugin.Plugin;
import net.redstonelamp.ui.Logger;

public abstract class JavaPlugin extends Plugin {
	
    public JavaPlugin(Server server, Logger logger, String name, String version, String[] authors,
                      String website){
        super(server, logger, name, version, authors, website);
    }
    
    public final Server getServer() {
    	return RedstoneLamp.SERVER;
    }

}
