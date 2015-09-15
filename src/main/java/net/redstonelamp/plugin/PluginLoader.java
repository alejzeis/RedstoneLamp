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

import java.io.InputStream;
import java.util.ArrayList;

public abstract class PluginLoader{
    public enum PluginState{
        UNLOADED,
        LOADED,
        INITIALIZED,
        ENABLED,
        DISABLED
    }

    /**
     * Current state this loader is in
     */
    @Getter
    @Setter
    private PluginState state = PluginState.UNLOADED;
    /**
     * Name of plugin
     */
    @Getter
    protected String name = null;
    /**
     * Version of plugin
     */
    @Getter
    protected String version = null;
    /**
     * The plugin manager handling this loader
     */
    @Getter
    private PluginManager pluginManager;
    /**
     * Instance of plugin
     */
    @Getter
    protected Plugin plugin = null;
    /**
     * List of dependencies that are needed to run this plugin
     */
    @Getter
    private ArrayList<String> dependencies = new ArrayList<>();
    /**
     * List of dependencies that are optional to influence behaviour of this plugin
     */
    @Getter
    private ArrayList<String> softDependencies = new ArrayList<>();

    public PluginLoader(PluginManager mgr){
        pluginManager = mgr;
    }

    /**
     * Load the plugin
     */
    public abstract void loadPlugin();
    /**
     * Initialize the plugin
     */
    public abstract void initPlugin();
    /**
     * Enable the plugin
     */
    public abstract void enablePlugin();
    /**
     * Disable the plugin
     */
    public abstract void disablePlugin();

    /**
     * Returns if this loader's plugin depends on the dependency
     *
     * @param depend The dependency to check
     * @return
     */
    public boolean dependsOn(String depend){
        for(String dependency : getDependencies()){
            if(dependency.equalsIgnoreCase(depend)) return true;
        }
        return false;
    }
    /**
     * Returns if this loader's plugin soft depends on the dependency
     *
     * @param depend The dependency to check
     * @return
     */
    public boolean softDependsOn(String depend){
        for(String dependency : getSoftDependencies()){
            if(dependency.equalsIgnoreCase(depend)) return true;
        }
        return false;
    }

    /**
     * Gets a resource as input stream
     *
     * @param path: Path to the resource requested
     * @return InputStream
     * @throws Exception
     */
    public abstract InputStream getResourceAsStream(String path) throws Exception;
}
