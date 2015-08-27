package net.redstonelamp.plugin.java;

import java.io.File;

import net.redstonelamp.plugin.PluginManager;

public class JavaPluginManager extends PluginManager{
	/**
	 * Directory all the java plugins are located in
	 */
	public static final File PLUGINS_DIR = new File("java-plugins");

	@Override
	public void loadPlugins() {
		for(File x : PLUGINS_DIR.listFiles()){
			if(x.getName().toLowerCase().endsWith(".jar")&&x.isFile()){
				JavaPluginLoader load = new JavaPluginLoader(this, x);
				this.getPluginLoaders().add(load);
				load.loadPlugin();
			}
		}
	}
	
}
