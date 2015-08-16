package redstonelamp.event;

public interface Listener {
	public default void onEvent(Event event) {}
}
