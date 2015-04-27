package redstonelamp.cmd;

import redstonelamp.plugin.Plugin;

public interface PluginIdentifiableCommand {
	/**
	 * Returns the plugin
	 * 
	 * @return Plugin
	 */
	public Plugin getPlugin();
}
