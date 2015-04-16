package redstonelamp.command;


public class CommandRegistrationManager {

	private String  command;
	private String  descr;
	private boolean console;
	
	public void register(String command, String descr, boolean conosole) {
		this.command = command;
		this.descr   = descr;
		this.console = console;
	}
	
	public Command getPluginCommand(final String command) {
		return null;
	}
}
