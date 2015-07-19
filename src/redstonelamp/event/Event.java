package redstonelamp.event;

import redstonelamp.Player;

public abstract interface Event {
	public void execute(Listener listener);
	
	public String getName();
	
	public void cancel();
	
	public Player getPlayer();
}
