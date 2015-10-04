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
package net.redstonelamp.cmd;

import java.util.Arrays;

import net.redstonelamp.cmd.exception.CommandException;
import net.redstonelamp.response.ChatResponse;
import net.redstonelamp.utils.TextFormat;

public class CommandManager {
	
	public void executeCommand(String cmd, CommandSender sender) throws CommandException{
        if(cmd.startsWith("/"))
            cmd = cmd.substring(1);
        String label = cmd.split(" ")[0];
        String[] args = (String[]) Arrays.copyOfRange(cmd.split(" "), 1, cmd.split(" ").length);
        Command command= Command.getByLabel(label);
        if(command != null) {
        	if(!command.getExecutor().onCommand(sender, command, label, args))
        		sender.sendMessage(new ChatResponse.ChatTranslation(TextFormat.RED + "redstonelamp.translation.command.usage", new String[] {command.getUsage()}));
        } else {
            sender.sendMessage("Unknown command! For help, use \"/help\"");
        }
    }
	
	public Command[] getCommands() {
		return Command.getCommands();
	}
	
}
