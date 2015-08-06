package redstonelamp.cmd.defaults;

import redstonelamp.RedstoneLamp;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandListener;
import redstonelamp.cmd.CommandSender;

public class Stop implements CommandListener {
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.isOp()) {
			RedstoneLamp.getServerInstance().stop();
		} else
			sender.noPermission();
	}
}
