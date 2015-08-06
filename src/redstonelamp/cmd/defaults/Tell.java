package redstonelamp.cmd.defaults;

import java.util.Arrays;

import redstonelamp.Player;
import redstonelamp.RedstoneLamp;
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
			String name = args[1];
			args[0] = null;
			args[1] = null;
			String message = Arrays.toString(args);
			for(Player p : RedstoneLamp.getServerInstance().getOnlinePlayers()) {
				if(name.toLowerCase().equals(p.getName().toLowerCase())) {
					sender.sendMessage("[You -> " + p.getDisplayName() + "] " + message);
					p.sendMessage("[" + sent + " -> You] " + message);
					worked = true;
				}
			}
			if(!worked)
				sender.sendMessage("Player \"" + name + "\" is not online!");
		} else
			sender.sendMessage("Usage: /tell <player> <message>");
	}
}
