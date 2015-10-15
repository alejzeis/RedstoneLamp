package net.redstonelamp.cmd.defaults;

import java.util.List;

import net.redstonelamp.Player;
import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.cmd.Command;
import net.redstonelamp.cmd.CommandExecutor;
import net.redstonelamp.cmd.CommandSender;

public class ListCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("list")) {
			List<Player> players = RedstoneLamp.SERVER.getPlayers();
			String list = new String("Players (" + players.size() + ")\n");
			for(int i = 0; i < players.size(); i++)
				list += (" - " + players.get(i).getDisplayName() + (i+1 < players.size() ? "\n" : ""));
			sender.sendMessage(list);
			return true;
		}
		return false;
	}
	
}
