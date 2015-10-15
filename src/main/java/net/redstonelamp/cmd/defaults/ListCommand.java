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
import net.redstonelamp.response.ChatResponse;
import net.redstonelamp.utils.TextFormat;

public class ListCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("list")) {
			List<Player> players = RedstoneLamp.SERVER.getPlayers();
			if(players.size() > 1) {
				sender.sendMessage(new ChatResponse.ChatTranslation(TextFormat.GOLD + "redstonelamp.translation.command.list.multiple", new String[] {String.valueOf(players.size())}));
			} else if(players.size() == 0) {
				sender.sendMessage(new ChatResponse.ChatTranslation(TextFormat.GOLD + "redstonelamp.translation.command.list.noPlayers", new String[0]));
			} else {
				sender.sendMessage(new ChatResponse.ChatTranslation(TextFormat.GOLD + "redstonelamp.translation.command.list.onePlayer", new String[] {players.get(0).getDisplayName()}));
			}
			if(players.size() < 2) return true;

			String list = "";
			for(int i = 0; i < players.size(); i++)
				list += (" - " + players.get(i).getDisplayName() + (i+1 < players.size() ? "\n" : ""));
			sender.sendMessage(list);
			return true;
		}
		return false;
	}
	
}
