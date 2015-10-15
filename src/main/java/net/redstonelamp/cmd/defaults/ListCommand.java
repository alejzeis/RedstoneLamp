/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
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
