package redstonelamp.cmd;

import redstonelamp.RedstoneLamp;
import redstonelamp.event.cmd.CommandExecuteEvent;

public class CommandExecutor {
	public void executeCommand(String command) {
		RedstoneLamp.getServerInstance().getEventManager().getEventExecutor().execute(new CommandExecuteEvent());
		//TODO: Execute command
	}
}
