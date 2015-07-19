package redstonelamp.plugin;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.utils.MainLogger;

public class PluginBase implements Plugin {
	public void onLoad() {}
	
	public void onEnable() {}
	
	public boolean isEnabled(String plugin) {
		return this.getServer().getPluginManager().getPluginArray().toString().contains(plugin);
	}
	
	public void onDisable() {}
	
	public boolean isDisabled(String plugin) {
		return !(this.getServer().getPluginManager().getPluginArray().toString().contains(plugin));
	}
	
	public Server getServer() {
		return RedstoneLamp.getServerInstance();
	}
	
	public MainLogger getLogger() {
		return this.getServer().getLogger();
	}
	
	public File getDataFolder() {
		String name = FilenameUtils.removeExtension(this.getClass().getName());
		File data = new File("./plugins/" + name);
		if(!data.isDirectory())
			data.mkdirs();
		return data;
	}
}