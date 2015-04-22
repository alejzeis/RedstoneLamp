package redstonelamp.cmd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redstonelamp.Server;

public class SimpleCommandMap implements CommandMap {
	
	protected final Map<String, Command> redstoneCommands = new HashMap<String, Command>();
	private Server server;
	
	public SimpleCommandMap(Server server) {
		this.server = server;
	}
	
	@Override
	public void registerAll(String prefix, List<Command> commands) {
		if(commands != null) {
			for(Command c : commands) {
				register(prefix, c);
			}
		}
		
	}
	
	public boolean register(String prefix, Command command) {
		return register(command.getName(), prefix, command);
	}
	
	public boolean register(String label, String prefix, Command command) {
		label = label.toLowerCase().trim();
		if(register(label, command, false, prefix)) {
			command.register(this);
			return true;
		}
		return false;
	}
	
	public boolean register(String label, Command command, boolean isAlias, String prefix) {
		Command duplicate = redstoneCommands.get(label);
		if(duplicate != null)
			return false;
		duplicate = redstoneCommands.get(prefix + ":" + label);
		if(duplicate != null)
			return false;
		redstoneCommands.put(prefix + ":" + label, command);
		return true;
	}
}
