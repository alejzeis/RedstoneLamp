package redstonelamp.cmd.defaults;

import redstonelamp.Player;
import redstonelamp.RedstoneLamp;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandListener;
import redstonelamp.cmd.CommandSender;

public class List implements CommandListener {
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		switch(cmd.getName().toLowerCase()) {
			case "list":
				sender.sendMessage("=== Online Players ===");
				for(Player p : RedstoneLamp.getServerInstance().getOnlinePlayers()) {
					sender.sendMessage(p.getName());
				}
			break;
		}
	}
}
