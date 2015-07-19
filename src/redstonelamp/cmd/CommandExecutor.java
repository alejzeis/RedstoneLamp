package redstonelamp.cmd;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.event.cmd.CommandExecuteEvent;

public class CommandExecutor {
	private Server server;
	
	public void executeCommand(String command, String sender) {
		server = RedstoneLamp.getServerInstance();
		boolean executed = false;
		String[] args = command.split(" ");
		Command cmd = new Command(args[0]);
		CommandSender commandSender = new CommandSender(sender);
		String label = null; //TODO
		server.getEventManager().getEventExecutor().execute(new CommandExecuteEvent());
		for(int i = 0; i < (server.getCommandManager().getCommandMap().commands.size()); i++) {
			if(command.startsWith(server.getCommandManager().getCommandMap().commands.get(i))) {
				server.getCommandManager().getCommandMap().listeners.get(i).onCommand(commandSender, cmd, label, args);
				executed = true;
			}
		}
		if(!executed)
			commandSender.sendMessage("Unknown command! For help, run \"/help\" or \"/?\"");
	}
}
