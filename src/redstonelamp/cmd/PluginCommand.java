package redstonelamp.cmd;

import java.util.Arrays;

import redstonelamp.plugin.Plugin;

public class PluginCommand extends Command implements PluginIdentifiableCommand {
	
	private Plugin plugin;
	private CommandExecutor executor;
	
	public PluginCommand(final String name, Plugin plugin) {
		super(name);
		this.plugin = plugin;
		this.executor = plugin;
	}
	
	public boolean execute(CommandSender sender, final String commandLabel, String[] arg) {
		if(!plugin.isEnabled()) { return false; }
		// TODO add permission
		boolean success = executor.onCommand(sender, this, commandLabel, Arrays.asList(arg));
		if(!success) {
			sender.sendMessage("");// TODO send usage message
		}
		return success;
	}
	
	public CommandExecutor getExecutor() {
		return executor;
	}
	
	public void setExecutor(CommandExecutor executor) {
		this.executor = executor != null ? executor:plugin;
	}
	
	@Override
	public Plugin getPlugin() {
		return plugin;
	}
	
}
