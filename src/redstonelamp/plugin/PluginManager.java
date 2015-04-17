package redstonelamp.plugin;

import java.io.File;
import java.util.ArrayList;

import redstonelamp.Server;
import redstonelamp.cmd.CommandMap;
import redstonelamp.cmd.SimpleCommandMap;

public class PluginManager {
	
    private Server server;
    private CommandMap commandMap;
	private ArrayList<Plugin> plugins = new ArrayList<Plugin>();
	private PluginLoader loader;
	
	public PluginManager(Server server, SimpleCommandMap commandMap) {
		this.server     = server;
		this.commandMap = commandMap;
	}
	
	/*
	 * Register Plug-in loader class
	 */
	public void registerPluginLoader(PluginLoader loader) {
		this.loader = loader;
	}
	
	/*
	 * Loads all Plug-ins
	 */
	public void loadPlugins(File folder) {
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (file.isFile() && file.getName().toLowerCase().endsWith(".java")) {
				server.getLogger().info(": Plug in -> " + file);
				loader.preparePluginFiles(file);
			}
		}
		plugins = loader.loadJavaPlugins();
	}
	
	/*
	 * return s plug-in
	 */
	public Plugin getPlugin(final String name) {
		for(Plugin p : plugins) {
			if( name.equals(p.getName())) return p;
		}
		return null;
	}
	
	/*
	 * Returns all plug-ins
	 */
	public ArrayList<Plugin> getPlugins() {
		return plugins;
	}
}
