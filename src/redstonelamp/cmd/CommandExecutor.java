package redstonelamp.cmd;

import java.util.List;

public interface CommandExecutor {
	/**
	 * Run when a command is issued
	 * 
	 * @param CommandSender sender
	 * @param Command command
	 * @param String label
	 * @param List<String> args
	 * @return boolean
	 */
	public boolean onCommand(CommandSender sender, Command command, String label, List<String> args);
}
