package redstonelamp.cmd.defaults;

import redstonelamp.RedstoneLamp;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandListener;
import redstonelamp.cmd.CommandSender;
import redstonelamp.utils.TextFormat;

public class Reload implements CommandListener {
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		RedstoneLamp.getServerInstance().getPluginManager().getPluginLoader().disablePlugins();
		RedstoneLamp.getServerInstance().getPluginManager().getPluginLoader().loadPlugins();
		sender.sendMessage(TextFormat.GREEN + "Reload complete.");
	}
}
