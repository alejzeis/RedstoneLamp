package redstonelamp.cmd.defaults;

import redstonelamp.RedstoneLamp;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandListener;
import redstonelamp.cmd.CommandSender;
import redstonelamp.network.PENetworkInfo;
import redstonelamp.network.pc.PCNetworkInfo;

public class Version implements CommandListener {
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage("This server is running " + RedstoneLamp.SOFTWARE + " version " + RedstoneLamp.VERSION);
		//TODO: Disable if MCPC is not running
		sender.sendMessage("Running MCPC Version - " + PCNetworkInfo.MC_VERSION);
		//TODO: Disable if MCPE is not running
		sender.sendMessage("Running MCPE Version - " + PENetworkInfo.MCPE_VERSION);
	}
}
