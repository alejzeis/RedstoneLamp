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
package net.redstonelamp.plugin.java;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import lombok.Getter;
import net.redstonelamp.plugin.PluginLoader;
import net.redstonelamp.plugin.PluginManager;
import net.redstonelamp.plugin.PluginSystem;
import net.sourceforge.yamlbeans.YamlException;
import net.sourceforge.yamlbeans.YamlReader;

public class JavaPluginLoader extends PluginLoader{
	/**
	 * The plugin's jar file
	 */
	@Getter private File pluginFile;
	/**
	 * Cashed properties for initialisation
	 */
	@Getter private JavaPluginProperties properties = null;
	
	public JavaPluginLoader(PluginManager mgr, File pluginFile) {
		super(mgr);
		this.pluginFile = pluginFile;
	}

	@Override
	public void loadPlugin() {
		//Include this plugin into classpath
		try {
			JavaClassPath.addFile(getPluginFile());
			YamlReader reader = new YamlReader(new InputStreamReader(getResourceAsStream("plugin.yml")));
			JavaPluginProperties prop = reader.read(JavaPluginProperties.class);
			if(prop.getMain()==null)throw new YamlException("plugin.yml does not contain main class");
			if(prop.getName()==null)throw new YamlException("plugin.yml does not contain plugin name");
			if(prop.getVersion()==null)throw new YamlException("plugin.yml does not contain plugin version");
			for(String sd : prop.getSoftdepend())this.getSoftDependencies().add(sd);
			for(String d : prop.getDepend())this.getDependencies().add(d);
			this.name = prop.getName();
			this.version = prop.getVersion();
			this.properties = prop;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		setState(PluginState.LOADED);
	}

	@Override
	public void initPlugin() {
		if(getState()!=PluginState.LOADED)return;
		Class<?> mainClass = null;
		try{
			mainClass = Class.forName(getProperties().getMain());
			if(!(mainClass.isAssignableFrom(JavaPlugin.class))){
				PluginSystem.getLogger().log(Level.SEVERE, name+" is in folder java-plugins, but does not extend JavaPlugin. Disabling!");
				setState(PluginState.UNLOADED);
				return;
			}
			Constructor<?> constructor = mainClass.getConstructor(String.class, String.class, String[].class, String.class);
			this.plugin = (JavaPlugin) constructor.newInstance(name, version, getProperties().getAuthors(), getProperties().getUrl());
		}catch(Exception e){
			e.printStackTrace();
			setState(PluginState.UNLOADED);
			return;
		}
		setState(PluginState.INITIALIZED);
	}

	@Override
	public void enablePlugin() {
		if(getState()!=PluginState.INITIALIZED)return;
		PluginSystem.getLogger().log(Level.INFO, "Enabling "+name+" v."+version+"...");
		this.plugin.onEnable();
		setState(PluginState.ENABLED);
	}

	@Override
	public void disablePlugin() {
		if(getState()!=PluginState.ENABLED)return;
		PluginSystem.getLogger().log(Level.INFO, "Disabling "+name+" v."+version+"...");
		this.plugin.onDisable();
		setState(PluginState.DISABLED);
	}

	@Override
	public InputStream getResourceAsStream(String path) throws ZipException, IOException {
		ZipFile zipFile = new ZipFile(getPluginFile());

	    Enumeration<? extends ZipEntry> entries = zipFile.entries();

	    while(entries.hasMoreElements()){
	        ZipEntry entry = entries.nextElement();
	        if(entry.getName().equals(path)){
	        	final InputStream in = zipFile.getInputStream(entry);
	        	//Limit resource to 6 mb
	        	byte[] read = new byte[1024*1024*1024*6];
	        	int length = in.read(read);
	        	zipFile.close();
	        	return new ByteArrayInputStream(Arrays.copyOfRange(read, 0, length-1));
	        }
	    }
	    zipFile.close();
		return null;
	}
	
}
