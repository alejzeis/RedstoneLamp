package redstonelamp.cmd;

import java.util.List;

public interface CommandMap {
	/**
	 * Registers all commands
	 * 
	 * @param String prefix
	 * @param List<Command> commands
	 */
	public void registerAll(String prefix, List<Command> commands);
}
