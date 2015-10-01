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
package net.redstonelamp.entity;

import net.redstonelamp.Player;
import net.redstonelamp.level.position.Position;
import net.redstonelamp.metadata.MetadataDictionary;

/**
 * Represents an Entity.
 *
 * @author RedstoneLamp Team
 */
public abstract class Entity{
    private int entityID;
    private int health;
    private Position position;
    private String name;
    private MetadataDictionary metadata;
    //private EntityMetadata metadata;
    private boolean initialized = false;
    private EntityMotion motion;
    
    protected void initEntity(){
        initialized = true;
        if(position != null && position.getLevel() != null){
            position.getLevel().getEntityManager().addEntity(this);
        }
    }

    protected void destroyEntity(){
        initialized = false;
        if(position != null && position.getLevel() != null){
            position.getLevel().getEntityManager().removeEntity(this);
        }
    }

    /**
     * Spawns this entity to a player. This method may be overridden in child classes.
     *
     * @param player The Player this entity will spawn to
     */
    public void spawnTo(Player player){
        //TODO: Send AddEntityResponse or Request?
    }

    /**
     * De-spawns this entity (removes) from a player. This method may be overriden in child classes.
     *
     * @param player The Player this entity will spawn to
     */
    public void despawnFrom(Player player){
        //TODO: Send RemoveEntityResponse or Request?
    }

    public int getEntityID(){
        return entityID;
    }

    /**
     * Sets this entity's ID. WARNING: ONLY FOR USE IN initEntity()!!!
     *
     * @param id The EntityID of the player
     */
    protected void setEntityID(int id){
        entityID = id;
    }

    public MetadataDictionary getMetadata(){
        return metadata;
    }

    protected void setMetadata(MetadataDictionary metadata){
        this.metadata = metadata;
    }

    public boolean isInitialized(){
        return initialized;
    }

    public Position getPosition(){
        return position;
    }

    public void setPosition(Position position){
        this.position = position;
    }

    public String getName(){
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public int getHealth(){
        return health;
    }

    /**
     * Sets the health of this entity.
     * <br>
     * NOTE: This sets the health to an absolute value, it does not increase the health.
     *
     * @param health The amount of health points to set this entity to.
     */
    public void setHealth(int health){
        this.health = health;
    }
}
