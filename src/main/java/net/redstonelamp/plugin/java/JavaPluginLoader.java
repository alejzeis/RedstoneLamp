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

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import lombok.Getter;
import net.redstonelamp.Server;
import net.redstonelamp.plugin.PluginLoader;
import net.redstonelamp.plugin.PluginManager;
import net.redstonelamp.plugin.PluginSystem;
import net.redstonelamp.ui.ConsoleOut;
import net.redstonelamp.ui.Logger;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.jar.JarFile;

public class JavaPluginLoader extends PluginLoader{
    /**
     * The plugin's jar file
     */
    @Getter
    private File pluginFile;
    /**
     * Cashed properties for initialisation
     */
    @Getter
    private JavaPluginProperties properties = null;

    public JavaPluginLoader(PluginManager mgr, File pluginFile){
        super(mgr);
        this.pluginFile = pluginFile;
    }

    @Override
    public void loadPlugin(){
        //Include this plugin into classpath
        try{
            JavaClassPath.addFile(getPluginFile());
            YamlReader reader = new YamlReader(new InputStreamReader(getResourceAsStream("plugin.yml")));
            JavaPluginProperties prop = reader.read(JavaPluginProperties.class);
            if(prop.getMain() == null) throw new YamlException("plugin.yml does not contain main class");
            if(prop.getName() == null) throw new YamlException("plugin.yml does not contain plugin name");
            if(prop.getVersion() == null) throw new YamlException("plugin.yml does not contain plugin version");
            if(prop.getSoftdepend() != null)
                for(String sd : prop.getSoftdepend()) getSoftDependencies().add(sd);
            if(prop.getDepend() != null)
                for(String d : prop.getDepend()) getDependencies().add(d);
            name = prop.getName();
            version = prop.getVersion();
            properties = prop;
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
        setState(PluginState.LOADED);
    }

    @Override
    public void initPlugin(){
        if(getState() != PluginState.LOADED) return;
        Class<?> mainClass;
        try{
            mainClass = Class.forName(getProperties().getMain());
            if(!JavaPlugin.class.isAssignableFrom(mainClass)){
                PluginSystem.getLogger().error(name + " is in folder java-plugins, but does not extend JavaPlugin. Disabling!");
                setState(PluginState.UNLOADED);
                return;
            }

            Constructor<?> constructor = mainClass.getConstructor(Server.class, Logger.class, String.class, String.class, String[].class, String.class);
            Logger l = createPluginLogger(getProperties().getName());
            plugin = (JavaPlugin) constructor.newInstance(PluginSystem.getServer(), l, name, version, getProperties().getAuthors(), getProperties().getUrl());
        }catch(Exception e){
            e.printStackTrace();
            setState(PluginState.UNLOADED);
            return;
        }
        setState(PluginState.INITIALIZED);
    }

    private Logger createPluginLogger(String name){
        try{
            Constructor c = PluginSystem.getServer().getLogger().getConsoleOutClass().getConstructor(String.class);
            return new Logger((ConsoleOut) c.newInstance(name));
        }catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e){
            PluginSystem.getServer().getLogger().error("Exception while creating plugin logger for: " + name + ", " + e.getClass().getName() + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void enablePlugin(){
        if(getState() != PluginState.INITIALIZED) return;
        PluginSystem.getLogger().info("Enabling " + name + " v." + version + "...");
        plugin.onEnable();
        setState(PluginState.ENABLED);
    }

    @Override
    public void disablePlugin(){
        if(getState() != PluginState.ENABLED) return;
        PluginSystem.getLogger().info("Disabling " + name + " v." + version + "...");
        plugin.onDisable();
        setState(PluginState.DISABLED);
    }

    @Override
    public InputStream getResourceAsStream(String path) throws IOException{
        JarFile jar = new JarFile(getPluginFile());
        return jar.getInputStream(jar.getJarEntry(path));
    }

}
