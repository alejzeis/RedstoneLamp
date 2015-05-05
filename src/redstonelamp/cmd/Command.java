package redstonelamp.cmd;

import java.util.List;

public abstract class Command {
	private String name;
	private String label;
	private String desciption;
	private String useMessage;
	private List<String> aliases;
	private CommandMap commandMap;
	private String nextLabel;

	public Command(final String name) {
		this.name = this.label = name;
	}
	
	public Command(final String name, final String description, final String useMessage, List<String> aliases) {
		this.name = name;
		this.desciption = description;
		this.useMessage = useMessage;
	}
	
	/**
	 * Returns the command sent
	 * 
	 * @return String
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Registers the command into a Command map
	 *
	 * @param CommandMap $commandMap
	 *
	 * @return boolean
	 */
	public boolean register(CommandMap commandMap) {
		if(allowChangesFrom(commandMap)) {
			this.commandMap = commandMap;
			return true;
		}
		return false;
	}
	
	public abstract boolean execute(CommandSender sender, String commandLabel, String[] args);

	/**
	 * @param CommandMap $commandMap
	 *
	 * @return boolean
	 */
	public boolean unregister(CommandMap commandMap) {
		if(allowChangesFrom(commandMap)) {
            this.commandMap = null;
            this.label = this.nextLabel;
            return true;
		}
		return false;
	}
	
    private boolean allowChangesFrom(CommandMap commandMap) {
        return (null == this.commandMap || this.commandMap == commandMap);
    }

}
