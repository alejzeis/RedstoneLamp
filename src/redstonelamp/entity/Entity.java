package redstonelamp.entity;

import redstonelamp.level.Location;

/**
 * Created by jython234 on 7/14/2015.
 */
public abstract class Entity {
    private Location location;

    public Location getLocation(){
        return location;
    }

    protected void setLocation(Location location){
        this.location = location;
    }
}
