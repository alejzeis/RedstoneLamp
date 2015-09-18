package net.redstonelamp.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;

public class CommandManager {
    @Getter private CommandExecutor commandExecutor = new CommandExecutor();
    
    @Getter private HashMap<String, String> commands = new HashMap<String, String>();
    @Getter private List<CommandListener> listeners = new ArrayList<CommandListener>();
    
    public void registerCommand(String cmd, String description, CommandListener listener) {
        commands.put(cmd, description);
        listeners.add(listener);
    }
}
