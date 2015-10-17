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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import lombok.Getter;
import net.redstonelamp.cmd.defaults.*;
import net.redstonelamp.cmd.exception.CommandException;

public class Command {
	
	private static HashMap<String, Command> cmds = new HashMap<String, Command>();
	private static boolean registeredDefaults = false;
	
	@Getter private final String label;
	@Getter private final String usage;
	@Getter private final String description;
	@Getter private final boolean overridable;
	@Getter private CommandExecutor executor;
	
	public static void init() throws CommandException {
		if(registeredDefaults)
			throw new CommandException("The defaults have already been registered!");
		registerCommand(new Command("say", "/say (message)", "Broadcasts a message to the server", new SayCommand(), true));
		registerCommand(new Command("list", "/list", "Shows all of the online players to the sender", new ListCommand(), true));
		registerCommand(new Command("kick", "/kick (player) [reason]", "Kicks a player with the specified reason", new KickCommand(), true));
		registerCommand(new Command("permission", "/permission (permission) [player]", "See's if a player has a permission", new PermissionTest(), true));
		registerCommand(new Command("help", "/help", "View a list of all commands", new HelpCommand(), false));
	    registerCommand(new Command("version", "/version", "Shows the version of this server", new VersionCommand(), false));
		registerCommand(new Command("reload", "/reload", "Reloads all the server data and plugins", new ReloadCommand(), false));
	    registerCommand(new Command("stop", "/stop", "Stops the server", new StopCommand(), false));
	    registeredDefaults = true;
	}
	
	private Command(String label, String usage, String description, CommandExecutor executor, boolean overridable) {
		this.label = label;
		if(usage == null)
			this.usage = ("/" + label);
		else
			this.usage = ((!usage.startsWith("/") ? "/" : "") + usage);
		this.description = description;
		this.overridable = overridable;
		this.executor = executor;
	}
	
	public Command(String label, String usage, String description) throws CommandException {
		this(label, usage, description, null, false);
	}
	
	public Command(String label, String usage, String description, CommandExecutor executor) throws CommandException {
		this(label, usage, description, executor, true);
	}
	
	public static void registerCommand(Command command) throws CommandException {
		String label = command.getLabel().toLowerCase();
		if(cmds.containsKey(label))
			throw new CommandException("A command with that label already exists!");
		cmds.put(label, command);
	}
	
	public static void unregisterCommand(Command command) throws CommandException {
		String label = command.getLabel().toLowerCase();
		if(cmds.get(label) != null) {
			if(!cmds.get(label).isOverridable())
				throw new CommandException("Cannot remove a un-overridable command!");
		}
		cmds.remove(label);
	}
	
	public static Command[] getCommands() {
		ArrayList<Command> list = new ArrayList<Command>();
		Iterator<String> labels = cmds.keySet().iterator();
		while(labels.hasNext())
			list.add(cmds.get(labels.next()));
		return list.toArray(new Command[list.size()]);
	}
	
	public static Command getByLabel(String label) {
		return cmds.get(label.toLowerCase());
	}
	
	public void setExecutor(CommandExecutor executor) throws CommandException {
		if(!this.isOverridable())
			throw new CommandException("The command \"" + this.getLabel() + "\" can not be overriden!");
		this.executor = executor;
	}
	
}
