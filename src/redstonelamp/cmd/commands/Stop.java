package redstonelamp.cmd.commands;

import java.util.List;

import redstonelamp.RedstoneLamp;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandExecutor;
import redstonelamp.cmd.CommandSender;
import redstonelamp.event.server.ServerStartEvent;

public class Stop implements CommandExecutor {
	public void onServerStart(ServerStartEvent event) {
		RedstoneLamp.server.getCommandRegistrationManager().registerCommand("stop", "Stops the server", true);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, List<String> args) {
		switch(cmd.getName()) {
			case "stop":
				RedstoneLamp.server.stop();
			break;
		}
		return true;
	}
}
