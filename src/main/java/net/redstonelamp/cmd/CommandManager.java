package net.redstonelamp.cmd;

import java.util.Arrays;

import net.redstonelamp.cmd.exception.CommandException;

public class CommandManager {
	
	public void executeCommand(String cmd, CommandSender sender) throws CommandException{
        if(cmd.startsWith("/"))
            cmd = cmd.substring(1);
        String label = cmd.split(" ")[0];
        String[] args = (String[]) Arrays.copyOfRange(cmd.split(" "), 1, cmd.split(" ").length);
        Command command= Command.getByLabel(label);
        if(command != null) {
        	if(!command.getExecutor().onCommand(sender, command, label, args))
        		sender.sendMessage("Usage: " + command.getUsage());
        } else {
            sender.sendMessage("Unknown command! For help, use \"/help\"");
        }
    }
	
	public Command[] getCommands() {
		return Command.getCommands();
	}
	
}
