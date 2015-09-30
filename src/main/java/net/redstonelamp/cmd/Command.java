package net.redstonelamp.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import lombok.Getter;
import net.redstonelamp.cmd.defaults.HelpCommand;
import net.redstonelamp.cmd.defaults.SayCommand;
import net.redstonelamp.cmd.defaults.StopCommand;
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
		new Command("say", "/say (message)", "Broadcasts a message to the server", new SayCommand(), true);
		new Command("help", "/help", "View a list of all commands", new HelpCommand(), false);
	    new Command("stop", "/stop", "Stops the server", new StopCommand(), false);
	    registeredDefaults = true;
	}
	
	private Command(String label, String usage, String description, CommandExecutor executor, boolean overridable) throws CommandException {
		if(cmds.containsKey(label))
			throw new CommandException("A command with that label already exists!");
		this.label = label;
		if(usage == null)
			this.usage = ("/" + label);
		else
			this.usage = ((!usage.startsWith("/") ? "/" : "") + usage);
		this.description = description;
		this.overridable = overridable;
		this.executor = executor;
		cmds.put(label, this);
	}
	
	public Command(String label, String usage, String description) throws CommandException {
		this(label, usage, description, null, false);
	}
	
	public Command(String label, String usage, String description, CommandExecutor executor) throws CommandException {
		this(label, usage, description, executor, true);
	}
	
	public static Command[] getCommands() {
		ArrayList<Command> list = new ArrayList<Command>();
		Iterator<String> labels = cmds.keySet().iterator();
		while(labels.hasNext())
			list.add(cmds.get(labels.next()));
		return list.toArray(new Command[list.size()]);
	}
	
	public static Command getByLabel(String label) {
		return cmds.get(label);
	}
	
	public void setExecutor(CommandExecutor executor) throws CommandException {
		if(!this.isOverridable())
			throw new CommandException("The command \"" + this.getLabel() + "\" can not be overriden!");
		this.executor = executor;
	}
	
}
