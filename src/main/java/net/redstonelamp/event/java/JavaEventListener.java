/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.redstonelamp.event.java;

import net.redstonelamp.event.EventContext;
import net.redstonelamp.event.EventListener;
import net.redstonelamp.event.EventPriority;
import net.redstonelamp.event.EventSystem;

import java.lang.reflect.Method;

public class JavaEventListener implements EventListener{

    @Override
    public void receiveEvent(JavaEvent e, EventPriority priority){
        for(Method method : this.getClass().getMethods()){
            if(method.isAnnotationPresent(EventSystem.class)){
                //Check if event matches
                if(method.getParameterCount() != 1) continue;
                if(!method.getParameterTypes()[0].isAssignableFrom(e.getClass())) return;
                EventSystem anno = method.getAnnotation(EventSystem.class);
                //Priority doesn't match --> continue
                if(anno.priority() != priority) continue;
                //Context doesn't match --> continue
                if(anno.eventContext() == EventContext.PC){
                    if(e.getEventContext() == EventContext.POCKET) continue;
                }
                if(anno.eventContext() == EventContext.POCKET){
                    if(e.getEventContext() == EventContext.PC) continue;
                }
                //Skip cancelled events
                if(!anno.ignoreCancelled()){
                    if(e instanceof CancellableJavaEvent){
                        if(((CancellableJavaEvent) e).isCancelled()) continue;
                    }
                }
            }
        }
    }
}
