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

import net.redstonelamp.Player;
import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.cmd.Command;
import net.redstonelamp.cmd.CommandExecutor;
import net.redstonelamp.cmd.CommandSender;
import net.redstonelamp.utils.TextFormat;

public class KickCommand implements CommandExecutor {
	
	private static final String KICK_PERMISSION = "redstonelamp.command.player.kick";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("kick")) {
			if(sender.hasPermission(KICK_PERMISSION)) {
				if(args.length >= 1) {
					Player player = RedstoneLamp.SERVER.getPlayer(args[0]);
					if(player == null) {
						sender.sendMessage(TextFormat.RED + "Unable to find the player " + args[0]);
						return true;
					}
					sender.sendMessage("Kicked " + args[0]);
					String kickMessage = (args.length >= 2 ? args[1] : "Kicked by an operator");
					player.close(" was kicked from the game", kickMessage, true);	
					return true;
				}
				return false;
			}
			else {
				sender.sendMessage(TextFormat.DARK_RED + "You do not have permission!");
				return true;
			}
		}
		return false;
	}

}
