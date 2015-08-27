package net.redstonelamp.event.java;

import java.lang.reflect.Method;

import net.redstonelamp.event.EventContext;
import net.redstonelamp.event.EventListener;
import net.redstonelamp.event.EventPriority;
import net.redstonelamp.event.EventSystem;

public class JavaEventListener implements EventListener{

	@Override
	public void receiveEvent(JavaEvent e, EventPriority priority) {
		for (Method method : this.getClass().getMethods()){
		    if (method.isAnnotationPresent(EventSystem.class)){
		        //Check if event matches
		    	if(method.getParameterCount()!=1)continue;
		    	if(!method.getParameterTypes()[0].isAssignableFrom(e.getClass()))return;
		    	EventSystem anno = method.getAnnotation(EventSystem.class);
		        //Priority doesn't match --> continue
		        if(anno.priority()!=priority)continue;
		        //Context doesn't match --> continue
		        if(anno.eventContext()==EventContext.PC){
		        	if(e.getEventContext()==EventContext.POCKET)continue;
		        }
		        if(anno.eventContext()==EventContext.POCKET){
		        	if(e.getEventContext()==EventContext.PC)continue;
		        }
		        //Skip cancelled events
		        if(!anno.ignoreCancelled()){
		        	if(e instanceof CancellableJavaEvent){
		        		if(((CancellableJavaEvent) e).isCancelled())continue;
		        	}
		        }
		    }
		}
	}
}
