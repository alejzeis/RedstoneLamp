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

import java.util.Arrays;

import net.redstonelamp.cmd.Command;
import net.redstonelamp.cmd.CommandExecutor;
import net.redstonelamp.cmd.CommandSender;
import net.redstonelamp.response.ChatResponse;
import net.redstonelamp.utils.TextFormat;

public class HelpCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("help")) {
			int page = 1;
			if(args.length >= 1)
				page = Integer.parseInt(args[0]);
			page--;
			Command[] commands = getCommands(page);
			if(commands != null) {
				//sender.sendMessage("Commands (" + (page+1) + "/" + getPages() + ")");
				sender.sendMessage(new ChatResponse.ChatTranslation(TextFormat.GREEN+"redstonelamp.translation.command.help.header", new String[] {String.valueOf(page + 1), String.valueOf(getPages())}));
				for (Command command : commands) {
					if(command != null)
						//sender.sendMessage("/" + command.getLabel() + " - " + command.getDescription());
						sender.sendMessage(new ChatResponse.ChatTranslation("redstonelamp.translation.command.help.listEntry", new String[] {command.getLabel(), command.getDescription()}));
				}
			} else {
				sender.sendMessage(new ChatResponse.ChatTranslation("redstonelamp.translation.command.help.invalidPage", new String[] {String.valueOf(page + 1)}));
			}
			return true;
		}
		return false;
	}
	
	private Command[] getCommands(int page) {
		try {
			return Arrays.copyOfRange(Command.getCommands(), page*10, (page*10)+10);
		} catch(ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	private int getPages() {
		Command[] commands = Command.getCommands();
		int j = 0;
		int pages = 1;
		for(int i = 0; i < commands.length; i++) {
			if(j > 10) {
				pages++;
				j = 0;
			}
		}
		return pages;
	}
	
}