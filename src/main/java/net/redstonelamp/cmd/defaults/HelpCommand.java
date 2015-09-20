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

import java.util.HashMap;
import java.util.Map.Entry;

import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.cmd.CommandListener;
import net.redstonelamp.cmd.CommandSender;

public class HelpCommand implements CommandListener {
    @Override
    public void onCommand(CommandSender sender, String cmd, String label, String[] params) {
        switch(cmd) {
            case "help":
                HashMap<String, String> commands = RedstoneLamp.SERVER.getCommandManager().getCommands();
                for(Entry<String, String> command : commands.entrySet()) {
                    sender.sendMessage("/" + command.getKey() + " - " + command.getValue());
                }
            break;
        }
    }
}
