package redstonelamp.cmd;

import java.util.List;

public interface CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, List<String> arg);
}
