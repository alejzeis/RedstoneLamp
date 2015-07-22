package redstonelamp.cmd;

public class CommandManager {
	private CommandMap commandMap = new CommandMap();
	private CommandExecutor commandExecutor = new CommandExecutor();
	
	public void registerCommand(String command, String description, CommandListener listener) {
		getCommandMap().commands.add(command);
		getCommandMap().description.add(description);
		getCommandMap().listeners.add(listener);
	}
	
	public CommandMap getCommandMap() {
		return commandMap;
	}

	public CommandExecutor getCommandExecutor() {
		return commandExecutor;
	}
}
