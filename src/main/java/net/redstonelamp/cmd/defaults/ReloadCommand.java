package net.redstonelamp.cmd.defaults;

import java.io.IOException;

import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.cmd.Command;
import net.redstonelamp.cmd.CommandExecutor;
import net.redstonelamp.cmd.CommandSender;
import net.redstonelamp.plugin.exception.PluginException;

public class ReloadCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("reload")) {
			try {
				// TODO: Reload entire server and not just plugins
				RedstoneLamp.SERVER.getPluginSystem().disablePlugins();
				RedstoneLamp.SERVER.getPluginSystem().enablePlugins();
			} catch(PluginException | IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

}
