package redstonelamp.plugin;

import java.lang.reflect.Constructor;

import redstonelamp.Server;

public class PluginManager {
	private Server server;
/*
 *  1. find the plugin folder
 *  2. Register it.
 *  3. 
 */
	/*
	 * Register Plugin loader class to load the plugins
	 */
	
	
	public void registerPluginLoader(Class<? extends PluginLoader> loader) {
		PluginLoader pluginLoader;
	    if(PluginLoader.class.isAssignableFrom(loader)) {
	    	try {
				Constructor<? extends PluginLoader> constructor = loader.getConstructor(Server.class);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
	    }
	}
}
