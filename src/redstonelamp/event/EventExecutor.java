package redstonelamp.event;

import java.lang.reflect.Method;

import redstonelamp.RedstoneLamp;

public class EventExecutor {
	public void execute(Event e) {
		for(Listener l : RedstoneLamp.getServerInstance().getEventManager().getListeners()) {
			Method[] methods = l.getClass().getDeclaredMethods();
			for(Method method : methods) {
				method.setAccessible(true);
				Class<?>[] params = method.getParameterTypes();
				if(params.length == 1) {
					if(params[0].equals(e.getClass())) {
						try {
							method.invoke(l, e);
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		}
	}
}
