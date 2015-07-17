package redstonelamp.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.Policy;

import org.apache.commons.io.FilenameUtils;

import redstonelamp.RedstoneLamp;

public class PluginLoader {
	private File dir = new File("./plugins");
	
	public PluginLoader() {
		
	}
	
	public void loadPlugins() {
		if(!this.getPluginsFolder().isDirectory())
			this.getPluginsFolder().mkdirs();
		for(File plugin : this.getPluginsFolder().listFiles()) {
			if(plugin.isFile()) {
				String ext = FilenameUtils.getExtension(plugin.getAbsolutePath());
				String name = FilenameUtils.removeExtension(plugin.getName());
				if(ext.equals("jar"))
					loadJarPlugin(plugin);
				else if(ext.equals("js"))
					loadJSPlugin(plugin);
				//TODO: Other types of plugins
				else
					RedstoneLamp.getServerInstance().getLogger().error("Failed to verify plugin type for \"" + name + "\"");
			}
		}
	}
	
	public File getPluginsFolder() {
		return this.dir;
	}
	
	private void loadJarPlugin(File plugin) {
		String name = FilenameUtils.removeExtension(plugin.getName());
		try {
			RedstoneLamp.getServerInstance().getLogger().info("Loading plugin \"" + name + "\"...");
			Policy.setPolicy(new PluginPolicy());
			System.setSecurityManager(new SecurityManager());
			
			ClassLoader loader = URLClassLoader.newInstance(new URL[] {plugin.toURL()});
			PluginBase redstonelampPlugin = (PluginBase) loader.loadClass(name).newInstance();
			redstonelampPlugin.onLoad();
		} catch(MalformedURLException e) {
			e.printStackTrace();
			RedstoneLamp.getServerInstance().getLogger().error("Failed to load plugin \"" + name + "\": MalformedURL");
		} catch(InstantiationException e) {
			e.printStackTrace();
			RedstoneLamp.getServerInstance().getLogger().error("Failed to load plugin \"" + name + "\": Instantiation");
		} catch(IllegalAccessException e) {
			e.printStackTrace();
			RedstoneLamp.getServerInstance().getLogger().error("Failed to load plugin \"" + name + "\": IllegalAccess");
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			RedstoneLamp.getServerInstance().getLogger().error("Failed to load plugin \"" + name + "\": ClassNotFound");
		} catch(ClassCastException e) {
			RedstoneLamp.getServerInstance().getLogger().error("Failed to load plugin \"" + name + "\": Plugins must extend PluginBase");
		}
	}
	
	private void loadJSPlugin(File plugin) {
		String name = FilenameUtils.removeExtension(plugin.getName());
		RedstoneLamp.getServerInstance().getLogger().warning("Failed to load plugin \"" + name + "\": JavaScript plugins are not currently supported!");
	}
}
