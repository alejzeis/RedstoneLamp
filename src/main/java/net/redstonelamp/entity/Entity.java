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
    private Position position;
    private String nametag;
    private MetadataDictionary metadata;
    //private EntityMetadata metadata;
    private boolean initialized = false;
    private EntityMotion motion;

    protected void initEntity(){
        initialized = true;
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

    public String getNametag(){
        return nametag;
    }

    public void setNametag(String nametag){
        this.nametag = nametag;
    }
}
