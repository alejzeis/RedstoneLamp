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
	 * @param PluginLoader loader
	 */
	public void registerPluginLoader(PluginLoader loader) {
		this.loader = loader;
	}
	
	/*
	 * @param File folder
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
	 * @param String name
	 * 
	 * @return Plugin
	 */
	public Plugin getPlugin(final String name) {
		for(Plugin p : plugins) {
			if( name.equals(p.getName())) return p;
		}
		return null;
	}
	
	/*
	 * @return ArrayList<Plugin> plugins
	 */
	public ArrayList<Plugin> getPlugins() {
		return plugins;
	}
}
