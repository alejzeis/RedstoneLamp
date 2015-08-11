package redstonelamp.cmd.defaults;

import java.util.Arrays;

import redstonelamp.Player;
import redstonelamp.RedstoneLamp;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandListener;
import redstonelamp.cmd.CommandSender;

public class Me implements CommandListener {
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String sent = "Console";
		if(sender.getSender() instanceof Player)
			sent = ((Player) sender.getSender()).getDisplayName();
		if(args.length > 1) {
			args[0] = null;
			String message = Arrays.toString(args);
			RedstoneLamp.getServerInstance().broadcast("* " + sent + " " + message);
		}
	}
}
