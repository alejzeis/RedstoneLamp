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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import net.redstonelamp.event.Listener;
import net.redstonelamp.plugin.Plugin;
import net.redstonelamp.plugin.PluginManager;
import net.redstonelamp.plugin.PluginSystem;

public class JavaPluginManager extends PluginManager{
	
	// TODO: Implement right listener
	private static HashMap<Plugin, ArrayList<Listener>> listeners = new HashMap<>();
	
    /**
     * Directory all the java plugins are located in
     */
    public static final File PLUGINS_DIR = new File("plugins");

    @Override
    public void loadPlugins(){
        PluginSystem.getLogger().info("Loading java plugins...");
        if(!PLUGINS_DIR.exists()){
            PLUGINS_DIR.mkdirs();
        }
        //noinspection ConstantConditions
        for(File x : PLUGINS_DIR.listFiles()){
            if(x.getName().toLowerCase().endsWith(".jar") && x.isFile()){
                JavaPluginLoader load = new JavaPluginLoader(this, x);
                getPluginLoaders().add(load);
                load.loadPlugin();
            }
        }
    }
    
    public void registerEvents(Plugin plugin, Listener listener) {
    	if(listeners.get(plugin) == null)
    		listeners.put(plugin, new ArrayList<Listener>());
    	if(!listeners.get(plugin).contains(listener))
    		listeners.get(plugin).add(listener);
    }
    
    public void unregisterEvents(Plugin plugin, Listener listener) {
    	if(listeners.get(plugin) == null)
    		listeners.put(plugin, new ArrayList<Listener>());
    	listeners.get(plugin).remove(listener);
    }
    
    public Listener[] getListeners() {
    	ArrayList<Listener> array = new ArrayList<Listener>();
    	for(ArrayList<Listener> al : listeners.values()) {
    		for(Listener l : al)
    			array.add(l);
    	}
    	return array.toArray(new Listener[array.size()]);
    }

}
