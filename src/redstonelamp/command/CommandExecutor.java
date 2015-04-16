package redstonelamp.command;

import java.util.List;

public interface CommandExecutor {
	public void onCommand(CommandSender sender , Command command, String label, List<String> args);
}
