package redstonelamp.cmd.defaults;

import redstonelamp.RedstoneLamp;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandListener;
import redstonelamp.cmd.CommandSender;

public class Stop implements CommandListener {
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		switch(cmd.getName().toLowerCase()) {
			case "stop":
				if(sender.isOp()) {
					sender.sendMessage("Stopping the server...");
					RedstoneLamp.getServerInstance().stop();
				} else
					sender.noPermission();
			break;
		}
	}
}
