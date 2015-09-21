package net.redstonelamp.event;

public @interface EventHandler {
    
    EventPriority priority() default EventPriority.DEFAULT;
    
    EventPlatform platform() default EventPlatform.BOTH;
    
    ClientProtocol protocol();
    
    boolean ignoreCancelled() default false;
    
}
