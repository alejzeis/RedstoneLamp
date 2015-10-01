package net.redstonelamp.cmd.defaults;

import net.redstonelamp.Player;
import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.cmd.Command;
import net.redstonelamp.cmd.CommandExecutor;
import net.redstonelamp.cmd.CommandSender;
import net.redstonelamp.utils.TextFormat;

public class KickCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("kick")) {
			if(sender.hasOp()) {
				if(args.length >= 1) {
					Player player = RedstoneLamp.SERVER.getPlayer(args[0]);
					if(player == null) {
						sender.sendMessage(TextFormat.RED + "Unable to find the player " + args[0]);
						return true;
					}
					sender.sendMessage("Kicked " + args[0]);
					String kickMessage = (args.length >= 2 ? args[1] : "Kicked by an operator");
					player.close(" was kicked from the game", kickMessage, true);	
					return true;
				}
				return false;
			}
			else {
				sender.sendMessage(TextFormat.DARK_RED + "You do not have permission!");
				return true;
			}
		}
		return false;
	}

}
