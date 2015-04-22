package redstonelamp.cmd.commands;

import java.util.List;

import redstonelamp.RedstoneLamp;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandExecutor;
import redstonelamp.cmd.CommandSender;
import redstonelamp.event.server.ServerStartEvent;

public class Help implements CommandExecutor {
	public void onServerStart(ServerStartEvent event) {
		RedstoneLamp.server.getCommandRegistrationManager().registerCommand("help", "Shows a list of all server commands", true);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, List<String> args) {
		switch(cmd.getName()) {
			case "help":
			case "?":
			//TODO: Get all commands
			break;
		}
		return true;
	}
}
