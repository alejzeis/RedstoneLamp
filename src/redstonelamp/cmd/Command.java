package redstonelamp.cmd;

public abstract class Command {
	private String name;
	private String label;
	
	/*
	 * @param String name
	 */
	public Command(final String name) {
		this.name  = this.label = name;
	}
	
	/*
	 * @return String name
	 */
	public String getName() {
		return this.name;
	}
}
