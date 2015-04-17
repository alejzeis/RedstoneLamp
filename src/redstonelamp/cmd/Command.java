package redstonelamp.cmd;

public abstract class Command {
	
	private String name;
	
	private String label;
	
	public Command(final String name) {
		this.name  = this.label = name;
	}
	
	public String getName() {
		return this.name;
	}
}
