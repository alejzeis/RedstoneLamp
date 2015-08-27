/**
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

import java.util.ArrayList;

import lombok.Getter;
import net.redstonelamp.event.EventListener;
import net.redstonelamp.event.EventPriority;

/**
 * This event system contains & calls every listener, using any language, with JavaEvents.
 * If the listeners type is not Java, the listener will translate the event.
 */
public class JavaEventSystem {
	/**
	 * ArrayList of listeners
	 */
	@Getter private static ArrayList<EventListener> listeners = new ArrayList<>();
	
	/**
	 * Calls an event through the whole system, including every language
	 * @param e Event to call
	 */
	public static void callEvent(JavaEvent e){
		for(EventPriority priority : EventPriority.values()){
			for(EventListener listener : getListeners()){
				listener.receiveEvent(e, priority);
			}
		}
	}
	
	/**
	 * Registers an listener to the event system
	 * @param listener EventListener to register
	 */
	public static void registerEvents(EventListener listener){
		listeners.add(listener);
	}
	/**
	 * Unregisters an listener
	 * @param listener EventListener to unregister
	 */
	public static void unregisterEvents(EventListener listener){
		listeners.remove(listener);
	}
}
