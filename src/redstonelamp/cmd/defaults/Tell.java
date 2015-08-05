package redstonelamp.cmd.defaults;

import redstonelamp.Player;
import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandListener;
import redstonelamp.cmd.CommandSender;

public class Tell implements CommandListener {
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		boolean worked = false;
		String sent = "Console";
		if(sender.getSender() instanceof Player)
			sent = ((Player) sender).getName();
		if(args.length >= 3) {
			String message = args[3];
			for(Player p : RedstoneLamp.getServerInstance().getOnlinePlayers()) {
				if(args[2].toLowerCase().equals(p.getName().toLowerCase())) {
					sender.sendMessage("[You -> " + p.getName() + "] " + message);
					p.sendMessage("[" + sent + " -> You] " + message);
					worked = true;
				}
			}
			if(!worked)
				sender.sendMessage("Player is not online!");
		} else
			sender.sendMessage("Usage: /tell <player> <message>");
	}
}
