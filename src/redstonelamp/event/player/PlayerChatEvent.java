package redstonelamp.event.player;

import java.util.List;

import redstonelamp.Player;
import redstonelamp.event.Cancellable;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class PlayerChatEvent extends PlayerEvent implements Cancellable {
	private String type = "PlayerChatEvent";
	private Player player;
	private List<Player> recipents;
	private String message;
	private String format;
	private Event e = this;
	
	private boolean canceled;
	
	public PlayerChatEvent(Player player, String message) {
		this.player = player;
		this.recipents = player.getServer().getOnlinePlayers();
		this.message = message;
		this.format = ("<" + player.getName() + "> " + message);
	}

	public void execute(Listener listener) {
		listener.onEvent(e);
	}
	
	public String getEventName() {
		return type;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public List<Player> getRecipents() {
		return recipents;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getFormat() {
		return format;
	}
	
	public boolean isCanceled() {
		return canceled;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void setRecipents(List<Player> recipents) {
		this.recipents = recipents;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
}
