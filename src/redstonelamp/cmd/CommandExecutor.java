package redstonelamp.cmd;

import java.util.List;

public interface CommandExecutor {
	/*
	 * @param CommandSender sender
	 * @param Command command
	 * @param String label
	 * @param List<String> args
	 */
	public boolean onCommand(CommandSender sender , Command command, String label, List<String> arg);
}
