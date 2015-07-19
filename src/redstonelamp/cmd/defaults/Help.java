package redstonelamp.cmd.defaults;

import redstonelamp.RedstoneLamp;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandListener;
import redstonelamp.cmd.CommandSender;

public class Help implements CommandListener {
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		switch(cmd.getName().toLowerCase()) {
			case "help":
				for(int i = 0; i < RedstoneLamp.getServerInstance().getCommandManager().getCommandMap().commands.size(); i++) {
					sender.sendMessage("/" + RedstoneLamp.getServerInstance().getCommandManager().getCommandMap().commands.get(i) + " - " + RedstoneLamp.getServerInstance().getCommandManager().getCommandMap().description.get(i));
				}
			break;
		}
	}
}
