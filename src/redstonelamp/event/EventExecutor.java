package redstonelamp.event;

import redstonelamp.RedstoneLamp;

public class EventExecutor {
	public void execute(Event e) {
		for(Object o : RedstoneLamp.getServerInstance().getEventManager().getListeners()) {
			if(o instanceof Listener) {
				Listener l = (Listener) o;
				e.execute(l);
			}
		}
	}
}
