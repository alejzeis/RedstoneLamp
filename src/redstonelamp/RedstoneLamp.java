package redstonelamp;

import java.io.File;

import redstonelamp.plugin.PluginLoader;

public class RedstoneLamp {
	public static String SOFTWARE = "RedstoneLamp";
	public static String VERSION = "1.0.0";
	public static String CODENAME = "Baby Villager";
	public static String STAGE = "DEVELOPMENT";
	public static int API_VERSION = 1;
	
	public static boolean DEGUG = false;
	
	public static Server server;
	
	public static void main(String[] args) {
		server = new Server("RedstoneLamp Server", "Welcome to this server!", 19132, false, true, 16, 20, false, true, true, 0, false, false, true, 1, null, "world", null, "DEFAULT", true, false, null, true);

		File folder = new File("./plugins");
		if(!folder.exists())
			folder.mkdirs();
		File[] listOfFiles = folder.listFiles();
		PluginLoader pluginLoader = new PluginLoader();
		for(File file : listOfFiles) {
			if(file.isFile() && file.getName().toLowerCase().endsWith(".java")) {
		    	pluginLoader.loadPlugin(file.getName());
			}
		}
		
		server.getLogger().info("Done! For help, type \"help\" or \"?\"");
	}
}
