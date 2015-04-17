package redstonelamp.cmd;


public class CommandRegistrationManager {

	private String  command;
	private String  descr;
	private boolean console;
	
	public void registerCommand(String command, String description, boolean console) {
		this.command = command;
		this.descr   = description;
		this.console = console;
	}
	
	public Command getPluginCommand(final String command) {
		return null;
	}
}
