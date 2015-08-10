package redstonelamp.plugin;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.resources.annotations.RedstonePlugin;
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
	
	public String getName() {
		RedstonePlugin annotation = this.getClass().getAnnotation(RedstonePlugin.class);
		if(annotation != null)
			return annotation.name();
		return null;
	}
	
	public String getVersion() {
		RedstonePlugin annotation = this.getClass().getAnnotation(RedstonePlugin.class);
		if(annotation != null)
			return annotation.version();
		return null;
	}
	
	public double getApi() {
		RedstonePlugin annotation = this.getClass().getAnnotation(RedstonePlugin.class);
		if(annotation != null)
			return annotation.api();
		return 0;
	}
	
	public String getAuthor() {
		RedstonePlugin annotation = this.getClass().getAnnotation(RedstonePlugin.class);
		if(annotation != null)
			return annotation.author();
		return null;
	}
	
	public String getDescription() {
		RedstonePlugin annotation = this.getClass().getAnnotation(RedstonePlugin.class);
		if(annotation != null)
			return annotation.description();
		return null;
	}
	
	public String getWebsite() {
		RedstonePlugin annotation = this.getClass().getAnnotation(RedstonePlugin.class);
		if(annotation != null)
			return annotation.website();
		return null;
	}
	
	public String getUpdateUrl() {
		RedstonePlugin annotation = this.getClass().getAnnotation(RedstonePlugin.class);
		if(annotation != null)
			return annotation.updateUrl();
		return null;
	}
}