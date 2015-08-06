package redstonelamp.cmd.defaults;

import redstonelamp.Player;
import redstonelamp.RedstoneLamp;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandListener;
import redstonelamp.cmd.CommandSender;

public class Kick implements CommandListener {
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.isOp()) {
			if(args.length >= 2) {
				boolean kicked = false;
				String reason = "No reason";
				if(args.length >= 3)
					reason = args[2];
				for(Player p : RedstoneLamp.getServerInstance().getOnlinePlayers()) {
					if(args[1].toLowerCase().equals(p.getName().toLowerCase())) {
						p.kick(reason, true);
						sender.sendMessage("kicked " + p.getName() + " Reason: " + reason);
						kicked = true;
					}
				}
				if(!kicked)
					sender.sendMessage("Unable to kick " + args[1]);
			} else
				sender.sendMessage("Usage: /kick <player> [reason]");
		} else
			sender.noPermission();
	}
}
