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

import net.redstonelamp.plugin.PluginManager;
import net.redstonelamp.plugin.PluginSystem;

import java.io.File;

public class JavaPluginManager extends PluginManager{
    /**
     * Directory all the java plugins are located in
     */
    public static final File PLUGINS_DIR = new File("java-plugins");

    @Override
    public void loadPlugins(){
        PluginSystem.getLogger().info("Loading java plugins...");
        if(!PLUGINS_DIR.exists()){
            PLUGINS_DIR.mkdirs();
        }
        for(File x : PLUGINS_DIR.listFiles()){
            if(x.getName().toLowerCase().endsWith(".jar") && x.isFile()){
                JavaPluginLoader load = new JavaPluginLoader(this, x);
                this.getPluginLoaders().add(load);
                load.loadPlugin();
            }
        }
    }

}
