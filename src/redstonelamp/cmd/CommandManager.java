package redstonelamp.cmd;

public class CommandManager {
	private CommandMap commandMap = new CommandMap();
	
	public void registerCommand(String command, String usage, CommandListener listener) {
		//TODO: Register command
	}
	
	public CommandMap getCommandMap() {
		return commandMap;
	}
}
