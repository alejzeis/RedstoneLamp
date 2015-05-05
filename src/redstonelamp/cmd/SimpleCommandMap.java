package redstonelamp.cmd;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import redstonelamp.Server;

public class SimpleCommandMap<T> implements CommandMap {
	private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);
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
	
	/**
	 * Registers a command
	 * 
	 * @param String prefix
	 * @param Command command
	 * @return boolean
	 */
	public boolean register(String prefix, Command command) {
		return register(command.getName(), prefix, command);
	}
	
	/**
	 * Registers a command
	 * 
	 * @param String label
	 * @param String prefix
	 * @param Command command
	 * @return boolean
	 */
	public boolean register(String label, String prefix, Command command) {
		label = label.toLowerCase().trim();
		if(register(label, command, false, prefix)) {
			command.register(this);
			return true;
		}
		return false;
	}
	
	/**
	 * Registers a command
	 * 
	 * @param String label
	 * @param Command command
	 * @param boolean isAlias
	 * @param String prefix
	 * @return boolean
	 */
	public boolean register(String label, Command command, boolean isAlias, String prefix) {
		Command duplicate = redstoneCommands.get(label);
			if(duplicate != null)
				return false;
			redstoneCommands.put(label, command);
			return true;
	}
	
	public boolean dispatch(CommandSender sender, String commandLine)  {
        String[] args = PATTERN_ON_SPACE.split(commandLine);

        if (args.length == 0) {
            return false;
        }

        String sentCommandLabel = args[0].toLowerCase();
        Command target = getCommand(sentCommandLabel);
        if (target == null) {
            return false;
        }
  
        try {
            //target.execute(sender, sentCommandLabel, (String[]) Arrays_copyOfRange(args, 1, args.length));
        } catch (Exception ex) {
            ex.printStackTrace();
        } catch (Throwable ex) {
        	ex.printStackTrace();
            throw new IllegalArgumentException("Unhandled exception executing '" + commandLine + "' in " + target, ex);
        }
        return true;
    }
	
    public Command getCommand(String name) {
        Command target = redstoneCommands.get(name.toLowerCase());
        return target;
    }

    private  T[] Arrays_copyOfRange(String[] args, int start, int end) {
        if (args.length >= start && 0 <= start) {
            if (start <= end) {
                int length = end - start;
                int copyLength = Math.min(length, args.length - start);
                T[] copy = (T[]) Array.newInstance(args.getClass().getComponentType(), length);

                System.arraycopy(args, start, copy, 0, copyLength);
                return copy;
            }
            throw new IllegalArgumentException();
        }
        throw new ArrayIndexOutOfBoundsException();
    }
}
