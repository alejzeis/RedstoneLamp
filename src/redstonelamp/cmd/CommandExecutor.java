package redstonelamp.cmd;

import javax.script.Invocable;
import javax.script.ScriptException;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.event.cmd.CommandExecuteEvent;

public class CommandExecutor {
	private Server server;
	
	public void executeCommand(String command, Object sender) {
		server = RedstoneLamp.getServerInstance();
		command = command.toLowerCase();
		if(command.startsWith("/"))
			command = command.replace("/", "");
		boolean executed = false;
		String[] args = command.split(" ");
		Command cmd = new Command(args[0]);
		CommandSender commandSender = new CommandSender(sender);
		String label = null; //TODO
		server.throwEvent(new CommandExecuteEvent(commandSender, cmd, label, args));
		for(int i = 0; i < (server.getCommandManager().getCommandMap().commands.size()); i++) {
			if(command.startsWith(server.getCommandManager().getCommandMap().commands.get(i))) {
				if(server.getCommandManager().getCommandMap().listeners.get(i) != null) {
					server.getCommandManager().getCommandMap().listeners.get(i).onCommand(commandSender, cmd, label, args);
					executed = true;
				}
			}
		}
		for(Object plugin : server.getPluginManager().getPluginArray()) {
			if(plugin instanceof Invocable) {
				Invocable p = (Invocable) plugin;
				try {
					p.invokeFunction("onCommand", commandSender, cmd, label, args);
					executed = true;
				} catch(NoSuchMethodException e) {} catch(ScriptException e) {
					e.printStackTrace();
				}
			}
		}
		if(!executed)
			commandSender.sendMessage("Unknown command! For help, type \"/help\"");
	}
}
