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

import javax.script.Invocable;
import javax.script.ScriptException;

import net.redstonelamp.Player;
import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.Server;
import net.redstonelamp.cmd.exception.InvalidCommandSenderException;

public class CommandExecutor {
    public void execute(String cmd, Object sender) throws InvalidCommandSenderException {
        Server server = RedstoneLamp.SERVER;
        if(sender instanceof Server)
            sender = (Server) sender;
        else if(sender instanceof Player)
            sender = (Player) sender;
        else
            throw new InvalidCommandSenderException();
        
        if(cmd.startsWith("/"))
            cmd = cmd.substring(1);
        boolean executed = false;
        String[] params = cmd.split(" ");
        CommandSender commandSender = new CommandSender(sender);
        String label = null; //TODO
        //TODO: Command Execution event
        for(CommandListener l : server.getCommandManager().getListeners()) {
            if(l != null) {
                l.onCommand(commandSender, params[0], label, params);
                executed = true;
            }
        }
        for(Invocable i : server.getScriptManager().getScripts()) {
            try {
                Object exc = i.invokeFunction("onCommand", commandSender, params[0], label, params);
                if(exc != null && (boolean) exc)
                    executed = true;
            } catch (ScriptException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {}
        }
        
        if(!executed)
            commandSender.sendMessage("Unknown command! For help, use \"/help\"");
    }
}
