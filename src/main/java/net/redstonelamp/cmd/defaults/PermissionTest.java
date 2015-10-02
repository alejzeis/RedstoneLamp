package net.redstonelamp.cmd.defaults;

import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.Server;
import net.redstonelamp.cmd.Command;
import net.redstonelamp.cmd.CommandExecutor;
import net.redstonelamp.cmd.CommandSender;
import net.redstonelamp.utils.TextFormat;

public class PermissionTest implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length >= 1) {
			if(sender instanceof Server) {
				sender.sendMessage("You are the server, you have all of the permissions!");
				return true;
			}
			boolean has = RedstoneLamp.SERVER.getPlayer((args.length >= 2 ? args[1] : sender.getName())).hasPermission(args[0]);
			sender.sendMessage((has ? TextFormat.GREEN + "You do ": TextFormat.RED + "You do not") + " have the permission \"" + args[0] + "\"");
			return true;
		}
		return false;
	}
	
}
