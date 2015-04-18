package redstonelamp.cmd;

public class CommandRegistrationManager {
	private String  command;
	private String  description;
	private boolean console;
	
	/*
	 * @param String command
	 * @param String description
	 * @param boolean console
	 */
	public void registerCommand(String command, String description, boolean console) {
		this.command = command;
		this.description   = description;
		this.console = console;
	}
	
	/*
	 * @param String command
	 * 
	 * @return Command
	 */
	public Command getPluginCommand(final String command) {
		return null;
	}
}
