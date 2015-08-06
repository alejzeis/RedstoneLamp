package redstonelamp.cmd.defaults;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import redstonelamp.RedstoneLamp;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandListener;
import redstonelamp.cmd.CommandSender;

public class Plugins implements CommandListener {
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage("Plugins (" + RedstoneLamp.getServerInstance().getPluginManager().getPluginArray().size() + "): ");
		for(File plugin : RedstoneLamp.getServerInstance().getPluginManager().getPluginLoader().getPluginsFolder().listFiles()) {
			if(plugin.isFile()) {
				String name = FilenameUtils.removeExtension(plugin.getName());
				sender.sendMessage(name);
			}
		}
	}
}
