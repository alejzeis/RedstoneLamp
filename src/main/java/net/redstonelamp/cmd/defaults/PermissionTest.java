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
import net.redstonelamp.Server;
import net.redstonelamp.cmd.Command;
import net.redstonelamp.cmd.CommandExecutor;
import net.redstonelamp.cmd.CommandSender;
import net.redstonelamp.utils.TextFormat;

public class PermissionTest implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length >= 1) {
			if(sender instanceof Server) {
				sender.sendMessage("You are the server, you have all of the permissions!");
				return true;
			}
			Player toCheck = (Player) sender;
			if(args.length >= 2)
				toCheck = RedstoneLamp.SERVER.getPlayer(args[1]);
			boolean has = toCheck.hasPermission(args[0]);
			sender.sendMessage("You do? : " + has);
			sender.sendMessage((has ? TextFormat.GREEN + (toCheck.equals(sender) ? "You" : toCheck.getName()) + " do": TextFormat.RED + (toCheck.equals(sender) ? "You" : toCheck.getName()) + " " + (toCheck.equals(sender) ? "do" : "does") + " not") + " have the permission \"" + args[0] + "\"");
			return true;
		}
		return false;
	}
	
}
