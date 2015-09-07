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
import net.redstonelamp.Server;
import net.redstonelamp.plugin.PluginLoader.PluginState;
import net.redstonelamp.plugin.java.JavaPluginManager;
import net.redstonelamp.ui.Logger;

public class PluginSystem{
    /**
     * Contains all plugin managers
     */
    @Getter
    private final static PluginManager[] pluginManagers = new PluginManager[]{new JavaPluginManager()};
    @Getter
    private static Logger logger = null;
    @Getter
    private static Server server = null;

    /**
     * Initializes the plugin system. Has to be called from the main server loop!
     *
     * @param server The server this PluginSystem belongs to.
     * @param log    Logger to print out information (maybe with a [PluginSystem] tag?)
     */
    public void init(Server server, Logger log){
        logger = log;
        PluginSystem.server = server;
        logger.info("Initializing plugin system...");
        PluginManager.init();
    }
    /**
     * Loads all plugins and initializes them
     */
    public void loadPlugins(){
        logger.info("Loading & initializing plugins...");
        for(PluginManager mgr : pluginManagers) mgr.loadPlugins();
        for(PluginManager mgr : pluginManagers){
            mgr.getPluginLoaders().forEach(this::initPlugin);
        }
    }
    /**
     * Enables all plugins
     */
    public void enablePlugins(){
        logger.info("Enabling plugins...");
        for(PluginManager mgr : pluginManagers){
            mgr.getPluginLoaders().forEach(this::enablePlugin);
        }
    }
    /**
     * Disables all plugins
     */
    public void disablePlugins(){
        logger.info("Disabling plugins...");
        for(PluginManager mgr : pluginManagers){
            mgr.getPluginLoaders().forEach(this::disablePlugin);
        }
    }
    /**
     * Checks dependencies & initializes the specific plugin & loader
     *
     * @param loader
     */
    private void initPlugin(PluginLoader loader){
        //If this plugin already got initialized, return
        if(loader.getState() == PluginState.INITIALIZED) return;
        //Check if all dependencies are loaded and load them if not
        //If a dependency is missing, return to stop loading this plugin
        for(String depend : loader.getDependencies()){
            PluginLoader dependency = getPluginLoader(depend);
            if(dependency == null || dependency.getState() == PluginState.UNLOADED){
                logger.warning(loader.getName() + " v" + loader.getVersion() + " is missing dependency " + depend + "! Disabling!");
                loader.setState(PluginState.LOADED);
                return;
            }
            if(dependency.getState() != PluginState.INITIALIZED){
                initPlugin(dependency);
            }
        }
        //Check if all soft dependencies are loaded and initialize them
        //If a soft dependency is missing, continue checking for other soft dependencies
        for(String depend : loader.getSoftDependencies()){
            PluginLoader dependency = getPluginLoader(depend);
            if(dependency == null || dependency.getState() == PluginState.UNLOADED){
                continue;
            }
            if(dependency.getState() != PluginState.INITIALIZED){
                initPlugin(dependency);
            }
        }
        //Dependencies are initialized, initialize the plugin itself
        loader.initPlugin();
    }
    /**
     * Enables the specific plugin & loader
     *
     * @param loader
     */
    private void enablePlugin(PluginLoader loader){
        //If this plugin didn't get initialized, return
        if(loader.getState() != PluginState.INITIALIZED && loader.getState() != PluginState.DISABLED) return;
        //Check if all dependencies are enabled and enable them if not
        //If a dependency is missing, return to stop loading this plugin (should never happen)
        for(String depend : loader.getDependencies()){
            PluginLoader dependency = getPluginLoader(depend);
            if(dependency == null || dependency.getState() == PluginState.UNLOADED || dependency.getState() == PluginState.LOADED){
                logger.warning(loader.getName() + " v" + loader.getVersion() + " is missing dependency " + depend + "! Disabling!");
                loader.setState(PluginState.DISABLED);
                return;
            }
            if(dependency.getName().equalsIgnoreCase(loader.getName())){
                logger.warning(loader.getName() + " v" + loader.getVersion() + " is depending on itself! Disabling!");
                loader.setState(PluginState.DISABLED);
                return;
            }
            if(dependency.dependsOn(loader.getName()) || dependency.softDependsOn(loader.getName())){
                logger.warning(loader.getName() + " v" + loader.getVersion() + " could not be loaded, because its dependency \"" + dependency.getName() + "\" is depending on the plugin itself! (Both plugins depend on each other)");
                loader.setState(PluginState.DISABLED);
                return;
            }
            if(dependency.getState() == PluginState.INITIALIZED){
                enablePlugin(dependency);
            }
        }
        //Check if all soft dependencies are enabled and enable them
        //If a soft dependency is missing, continue checking for other soft dependencies
        for(String depend : loader.getSoftDependencies()){
            PluginLoader dependency = getPluginLoader(depend);
            if(dependency == null){
                continue;
            }
            if(dependency.getName().equalsIgnoreCase(loader.getName())){
                logger.warning(loader.getName() + " v" + loader.getVersion() + " is soft depending on itself! Disabling!");
                loader.setState(PluginState.DISABLED);
                return;
            }
            if(dependency.dependsOn(loader.getName()) || dependency.softDependsOn(loader.getName())){
                logger.warning(loader.getName() + " v" + loader.getVersion() + " could not be loaded, because its soft dependency \"" + dependency.getName() + "\" is depending on the plugin itself! (Both plugins depend on each other)");
                loader.setState(PluginState.DISABLED);
                return;
            }
            if(dependency.getState() == PluginState.INITIALIZED){
                enablePlugin(dependency);
            }
        }
        //Dependencies are enabled, enable the plugin itself
        loader.enablePlugin();
    }
    /**
     * Disables the specific plugin & loader
     *
     * @param loader
     */
    private void disablePlugin(PluginLoader loader){
        //If this plugin didn't get enabled, return
        if(loader.getState() != PluginState.ENABLED) return;
        //Disable plugins depending on this plugin first
        for(PluginManager mgr : pluginManagers){
            mgr.getPluginLoaders().stream()
                    .filter(l -> l.dependsOn(loader.getName()))
                    .forEach(this::disablePlugin);
        }
        //Dependent plugins are disabled, disable the plugin itself
        loader.disablePlugin();
    }
    /**
     * Gets the correct plugin manager by a given plugin name
     *
     * @param name Name of plugin
     * @return PluginManager or null if not found
     */
    public PluginManager getPluginManager(String name){
        PluginLoader l = getPluginLoader(name);
        if(l == null) return null;
        return l.getPluginManager();
    }
    /**
     * Gets the correct plugin loader by a given plugin name
     *
     * @param name Name of plugin
     * @return PluginLoader or null if not found
     */
    public PluginLoader getPluginLoader(String name){
        for(PluginManager mgr : pluginManagers){
            for(PluginLoader l : mgr.getPluginLoaders()){
                if(l.getName().equalsIgnoreCase(name)){
                    return l;
                }
            }
        }
        return null;
    }
    /**
     * Gets the correct plugin by a given plugin name
     *
     * @param name Name of plugin
     * @return Plugin or null if not found
     */
    public Plugin getPlugin(String name){
        for(PluginManager mgr : pluginManagers){
            for(PluginLoader l : mgr.getPluginLoaders()){
                if(l.getName().equalsIgnoreCase(name)){
                    return l.getPlugin();
                }
            }
        }
        return null;
    }

    /**
     * Gets the current instance of the JavaPluginManager
     *
     * @return JavaPluginManager
     */
    public JavaPluginManager getJavaPluginManager(){
        return (JavaPluginManager) pluginManagers[0];
    }
}
