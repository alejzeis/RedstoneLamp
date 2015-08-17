package redstonelamp.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.Policy;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.FilenameUtils;

import redstonelamp.event.Listener;
import redstonelamp.plugin.js.JavaScriptAPI;
import redstonelamp.RedstoneLamp;
import redstonelamp.cmd.CommandListener;
import redstonelamp.resources.annotations.RedstonePlugin;

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
					RedstoneLamp.getServerInstance().getLogger().fatal("Failed to verify plugin type for \"" + name + "\"");
			}
		}
	}
	
	public File getPluginsFolder() {
		return this.dir;
	}
	
	/**
	 * INTERNAL METHOD!
	 */
	public void enablePlugins() {
		for(Object o : RedstoneLamp.getServerInstance().getPluginManager().getPluginArray()) {
			if(o instanceof PluginBase) {
				PluginBase plugin = (PluginBase) o;
				plugin.onEnable();
			} else if(o instanceof Invocable) {
				Invocable plugin = (Invocable) o;
				try {
					plugin.invokeFunction("onEnable");
				} catch (NoSuchMethodException e) {} catch (ScriptException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * INTERNAL METHOD!
	 */
	public void disablePlugins() {
		for(Object o : RedstoneLamp.getServerInstance().getPluginManager().getPluginArray()) {
			if(o instanceof PluginBase) {
				PluginBase plugin = (PluginBase) o;
				plugin.onDisable();
			} else if(o instanceof Invocable) {
				Invocable plugin = (Invocable) o;
				try {
					plugin.invokeFunction("onDisable");
				} catch (NoSuchMethodException e) {} catch (ScriptException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void loadJarPlugin(File plugin) {
		String name = FilenameUtils.removeExtension(plugin.getName());
		try {
			Policy.setPolicy(new PluginPolicy());
			System.setSecurityManager(new SecurityManager());
			
			ClassLoader loader = URLClassLoader.newInstance(new URL[] {plugin.toURL()});
			PluginBase redstonelampPlugin = (PluginBase) loader.loadClass(name).newInstance();
			RedstonePlugin annotation = redstonelampPlugin.getClass().getAnnotation(RedstonePlugin.class);
			if(annotation != null) {
				if(!(annotation.api() > RedstoneLamp.API_VERSION)) {
					RedstoneLamp.getServerInstance().getPluginManager().getPluginArray().add(redstonelampPlugin);
					if(!annotation.author().equals(""))
						RedstoneLamp.getServerInstance().getLogger().info("Loading plugin " + name + " v" + annotation.version() + " by " + annotation.author() + "...");
					else
						RedstoneLamp.getServerInstance().getLogger().info("Loading plugin " + name + " v" + annotation.version() + "...");
					if(annotation.api() < RedstoneLamp.API_VERSION)
						RedstoneLamp.getServerInstance().getLogger().warning("Plugin \"" + name + "\" uses an older API version which may cause issues.");
					redstonelampPlugin.onLoad();
				} else
					RedstoneLamp.getServerInstance().getLogger().warn("Failed to load plugin \"" + name + "\": API version is greater than the current API version");
			} else
				RedstoneLamp.getServerInstance().getLogger().error("Failed to load plugin \"" + name + "\": @RedstonePlugin annotation is missing from main class");
		} catch(MalformedURLException e) {
			e.printStackTrace();
			RedstoneLamp.getServerInstance().getLogger().writeToLog(e.getMessage());
			RedstoneLamp.getServerInstance().getLogger().error("Failed to load plugin \"" + name + "\": Malformed URL");
		} catch(InstantiationException e) {
			e.printStackTrace();
			RedstoneLamp.getServerInstance().getLogger().writeToLog(e.getMessage());
			RedstoneLamp.getServerInstance().getLogger().error("Failed to load plugin \"" + name + "\": Instantiation error");
		} catch(IllegalAccessException e) {
			RedstoneLamp.getServerInstance().getLogger().writeToLog(e.getMessage());
			RedstoneLamp.getServerInstance().getLogger().error("Failed to load plugin \"" + name + "\": Plugin does not contain a src directory");
		} catch(ClassNotFoundException e) {
			RedstoneLamp.getServerInstance().getLogger().writeToLog(e.getMessage());
			RedstoneLamp.getServerInstance().getLogger().error("Failed to load plugin \"" + name + "\": Unable to find main class");
		} catch(ClassCastException e) {
			RedstoneLamp.getServerInstance().getLogger().error("Failed to load plugin \"" + name + "\": Plugins must extend PluginBase");
		}
	}
	
	private void loadJSPlugin(File plugin) {
		try {
			String name = FilenameUtils.removeExtension(plugin.getName());
			RedstoneLamp.getServerInstance().getLogger().info("Loading JavaScript plugin " + name + "...");
			ScriptEngineManager _manager = new ScriptEngineManager();
			ScriptEngine _engine = _manager.getEngineByName("JavaScript");
			Reader reader = new InputStreamReader(new FileInputStream(plugin));
			_engine.put("PluginBase", new PluginBase());
			_engine.eval(reader);
			for(JavaScriptAPI api : RedstoneLamp.getServerInstance().getPluginManager().getJavaScriptManager().getAPIClasses()) {
				try {
					_engine.put(api.getClass().getSimpleName(), api.getClass().newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			Invocable inv = (Invocable) _engine;
			RedstoneLamp.getServerInstance().getPluginManager().getPluginArray().add(inv);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(ScriptException e) {
			e.printStackTrace();
		}
	}
}
