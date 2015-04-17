package redstonelamp.plugin;

import redstonelamp.event.Event;
import redstonelamp.event.EventException;
import redstonelamp.event.Listener;

public class MethodEventExecutor implements EventExecutor {
	private String method;

	public MethodEventExecutor(final String method) {
		this.method = method;
	}

	@Override
	public void execute(Listener listener, Event event) throws EventException {

	}

	public String getMethod() {
		return method;
	}
}
