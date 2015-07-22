package redstonelamp.cmd;

import java.util.ArrayList;
import java.util.List;

public class CommandMap {
	public List<String> commands = new ArrayList<String>();
	public List<String> description = new ArrayList<String>();
	public List<CommandListener> listeners = new ArrayList<CommandListener>();
}
