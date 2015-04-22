package redstonelamp.cmd;

import java.util.List;

public abstract class Command {
	
	private String name;
	private String label;
	private String desciption;
	private String useMessage;
	private List<String> aliases;
	private CommandMap commandMap;
	
	public Command(final String name) {
		this.name = this.label = name;
	}
	
	public Command(final String name, final String description, final String useMessage, List<String> aliases) {
		this.name = name;
		this.desciption = description;
		this.useMessage = useMessage;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean register(CommandMap commandMap) {
		if(this.commandMap == commandMap || this.commandMap == null) {
			this.commandMap = commandMap;
			return true;
		}
		return false;
	}
	
}
