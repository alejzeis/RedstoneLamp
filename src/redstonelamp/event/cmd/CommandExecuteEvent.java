package redstonelamp.event.cmd;

import redstonelamp.RedstoneLamp;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandSender;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class CommandExecuteEvent extends Event {
	private Event e = this;
	private CommandSender commandSender;
	private Command cmd;
	private String label;
	private String[] args;
	
	public CommandExecuteEvent(CommandSender commandSender, Command cmd, String label, String[] args) {
		this.commandSender = commandSender;
		this.cmd = cmd;
		this.label = label;
		this.args = args;
	}

	public void execute(Listener listener) {
		RedstoneLamp.getAsync().execute(new Runnable() {
			public void run() {
				listener.onEvent(e);
			}
		});
	}
	
	public CommandSender getSender() {
		return this.commandSender;
	}
	
	public Command getCommand() {
		return this.cmd;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public String[] getArguments() {
		return this.args;
	}
}
