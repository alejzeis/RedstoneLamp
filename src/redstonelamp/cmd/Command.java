package redstonelamp.cmd;

public class Command {
	private String cmd;
	
	public Command(String cmd) {
		this.cmd = cmd;
	}
	
	public String getName() {
		return cmd;
	}
}
