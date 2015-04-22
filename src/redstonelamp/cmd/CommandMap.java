package redstonelamp.cmd;

import java.util.List;

public interface CommandMap {
	public void registerAll(String prefix, List<Command> commands);
}
