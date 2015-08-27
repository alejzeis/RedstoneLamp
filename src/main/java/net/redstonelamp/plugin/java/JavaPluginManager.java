package net.redstonelamp.plugin.java;

import java.io.File;
import java.util.logging.Level;

import net.redstonelamp.plugin.PluginManager;
import net.redstonelamp.plugin.PluginSystem;

public class JavaPluginManager extends PluginManager{
	/**
	 * Directory all the java plugins are located in
	 */
	public static final File PLUGINS_DIR = new File("java-plugins");

	@Override
	public void loadPlugins() {
		PluginSystem.getLogger().log(Level.INFO, "Loading java plugins...");
		for(File x : PLUGINS_DIR.listFiles()){
			if(x.getName().toLowerCase().endsWith(".jar")&&x.isFile()){
				JavaPluginLoader load = new JavaPluginLoader(this, x);
				this.getPluginLoaders().add(load);
				load.loadPlugin();
			}
		}
	}
	
}
