package redstonelamp.cmd;

import java.util.List;

public interface CommandListener {
	public boolean onCommand(CommandSender sender, Command cmd, String label, List<String> args);
}
