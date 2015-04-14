package redstonelamp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import redstonelamp.plugin.PluginLoader;

public class RedstoneLamp {
	
	public static String SOFTWARE  = "RedstoneLamp";
	public static String VERSION   = "1.0.0";
	public static String CODENAME  = "Baby Villager";
	public static String STAGE     = "DEVELOPMENT";
	public static int API_VERSION  = 1;
	
	public static boolean DEGUG = true; //Debug mode for developers
	
	public static Server server;
	
	/*
	 * RedstoneLamp properties
	 */
	public static Properties props;
	private final static String REDSTONELAMP_PROPERTIES  = "redstonelamp.properties";
	private final static String PLUGIN_FOLDER            = "PLUGIN_FOLDER";
	private final static String PLUGIN_CLASS_FILE_FOLDER = "PLUGIN_CLASS_FILE_FOLDER"; 
	private final static String JAVA_SDK                 = "JAVA_SDK";
	
	public static void main(String[] args) {
		server = new Server("RedstoneLamp Server", "Welcome to this server!", 19132, false, true, 16, 20, false, true, true, 0, false, false, true, 1, null, "world", null, "DEFAULT", true, false, null, true);
		
		// load Redstone property file
		loadProperties();
		/*
		 * Load each plug-in in the Plug-ins directory and create the directory if it doesnt exist
		 */
		File folder = new File(props.getProperty(PLUGIN_FOLDER));
		if(!folder.exists())
			folder.mkdirs();
		
		File inuse = new File(props.getProperty(PLUGIN_CLASS_FILE_FOLDER).trim());  // class files are generated in this folder
		if(!inuse.exists()) inuse.mkdirs();
		
		File[] listOfFiles = folder.listFiles();
		PluginLoader pluginLoader = new PluginLoader();
		
		// sets java SDK Location and PLUGIN_FOLDER
		pluginLoader.setPluginOption(props.getProperty(PLUGIN_FOLDER).trim(), props.getProperty(PLUGIN_CLASS_FILE_FOLDER).trim(), props.getProperty(JAVA_SDK).trim());
		
		for(File file : listOfFiles) {
			if(file.isFile() && file.getName().toLowerCase().endsWith(".java")) {
				//pluginLoader.loadPlugin(file.getName());
				pluginLoader.loadPluginJavaFile(file);
			}
		}
		
		/*
		 * Tell the console the server has loaded (Dummy location)
		 */
		server.getLogger().info("Done! For help, type \"help\" or \"?\"");
	}
	
	/*
	 * loads property file
	 */
	private static void loadProperties() {
		props          = new Properties();
		InputStream is = RedstoneLamp.class.getClassLoader().getResourceAsStream(REDSTONELAMP_PROPERTIES);
		try {
			props.load(is);
		} catch (IOException ioe) {
			throw new IllegalStateException(" redstonelamp property file is missing....");
		}
	}
	
	
}
