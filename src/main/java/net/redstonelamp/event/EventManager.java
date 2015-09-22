package net.redstonelamp.event;

import java.util.ArrayList;

import lombok.Getter;
import net.redstonelamp.plugin.Plugin;

public class EventManager {
    @Getter private ArrayList<EventListener> listeners = new ArrayList<EventListener>();
    
    public void registerEvents(EventListener listener, Plugin plugin) { //The plugin variable will be used later
        listeners.add(listener);
    }
}
