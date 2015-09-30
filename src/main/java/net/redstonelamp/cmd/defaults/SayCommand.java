package net.redstonelamp.cmd.defaults;

import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.cmd.Command;
import net.redstonelamp.cmd.CommandExecutor;
import net.redstonelamp.cmd.CommandSender;
import net.redstonelamp.utils.TextFormat;

public class SayCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("say")) {
			if(args.length == 0)
				return false;
			String message = new String();
			for(int i = 0; i < args.length; i++)
				message += (args[i] + (i + 1 < args.length ? " " : ""));
			RedstoneLamp.SERVER.broadcastMessage(TextFormat.LIGHT_PURPLE + "[" + TextFormat.RESET + sender.getName() + TextFormat.LIGHT_PURPLE + "] " + message);
			return true;
		}
		return false;
	}

}
