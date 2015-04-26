package redstonelamp.event.player;

import redstonelamp.entity.Player;
import redstonelamp.event.Event;

public abstract class PlayerEvent extends Event {
	protected Player player;
	
	public PlayerEvent(final Player who) {
		player = who;
	}
	
	//PlayerEvent(final Player who, boolean async) {
	//	super(async);
	//	player = who;
	//}
	
	public final Player getPlayer() {
		return player;
	}
}
