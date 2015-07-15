package redstonelamp.plugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.io.FilenameUtils;

import redstonelamp.RedstoneLamp;

public class PluginLoader {
	private File dir = new File("./plugins");
	
	public PluginLoader() {
		
	}
	
	public void loadPlugins() {
		if(!dir.isDirectory())
			dir.mkdirs();
		for(File plugin : dir.listFiles()) {
			if(plugin.isFile()) {
				String ext = FilenameUtils.getExtension(plugin.getAbsolutePath());
				String name = FilenameUtils.removeExtension(plugin.getName());
				if(ext.equals("class"))
					loadClassPlugin(plugin);
				else if(ext.equals("jar"))
					loadJarPlugin(plugin);
				else if(ext.equals("js"))
					loadJSPlugin(plugin);
				//TODO: Other types of plugins
				else
					RedstoneLamp.getServerInstance().getLogger().error("Failed to verify plugin type for \"" + name + "\"");
			}
		}
	}
	
	private void loadClassPlugin(File plugin) {
		String name = FilenameUtils.removeExtension(plugin.getName());
		RedstoneLamp.getServerInstance().getLogger().info("Loading plugin \"" + name + "\"...");
		//TODO: Load plugin
	}
	
	private void loadJarPlugin(File plugin) {
		String name = FilenameUtils.removeExtension(plugin.getName());
		RedstoneLamp.getServerInstance().getLogger().warning("Failed to load plugin \"" + name + "\": Jar plugins are not currently supported!");
	}
	
	private void loadJSPlugin(File plugin) {
		String name = FilenameUtils.removeExtension(plugin.getName());
		RedstoneLamp.getServerInstance().getLogger().warning("Failed to load plugin \"" + name + "\": JavaScript plugins are not currently supported!");
	}
}
