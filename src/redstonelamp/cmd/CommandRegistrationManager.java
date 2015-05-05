package redstonelamp.cmd;

import java.util.ArrayList;

import redstonelamp.RedstoneLamp;
import redstonelamp.plugin.Plugin;

public class CommandRegistrationManager {
	private ArrayList<Command> commands = new ArrayList<Command>();
	private SimpleCommandMap commandMap;
	private Plugin plugin;
	
	public CommandRegistrationManager(SimpleCommandMap commandMap) {
		this.commandMap = commandMap;
	}
	
	/**
	 * Registers a command to the Server
	 * 
	 * @param String command
	 * @param String description
	 * @param boolean console
	 */
	public void registerCommand(String command, String description, boolean console) {
		registerCommand(command, description);
	}
	
	private void registerCommand(final String command, final String descr) {
		if(!plugin.isEnabled()) {
			RedstoneLamp.server.getLogger().info(":Command(s) are not registered as " + plugin.getName() + " is not enabled");
			return;
		}
		
		Command cmd = new PluginCommand(command, plugin);
		commands.add(cmd);
		//this.commandMap.register(plugin.getName(), cmd);
		this.commandMap.register("", cmd);
	}
	
	public PluginIdentifiableCommand getPluginCommand(final String command) {
		return null;
	}
	
	/**
	 * Returns registered plugin commands
	 * 
	 * @param String command
	 * @return ArrayList<Command>
	 */
	public ArrayList<Command> getPluginCommands(final String command) {
		ArrayList<Command> list = new ArrayList<Command>();
		for(Command cmd : commands) {
			if(cmd.getName().equals(command))
				list.add(cmd);
		}
		return list;
	}
	
	public void setPlugin(final Plugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Sets the command map
	 * 
	 * @param SimpleCommandMap simpleCommandMap
	 */
	public void setCommandMap(SimpleCommandMap simpleCommandMap) {
		this.commandMap = simpleCommandMap;
	}
}
