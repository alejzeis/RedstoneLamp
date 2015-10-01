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
import net.redstonelamp.metadata.*;
import net.redstonelamp.response.AddPlayerResponse;
import net.redstonelamp.response.RemovePlayerResponse;

/**
 * An entity implementation of a Player. This is the Player's
 * parent class.
 *
 * @author RedstoneLamp Team
 */
public abstract class PlayerEntity extends Entity{

    @Override
    protected void initEntity(){
        MetadataDictionary data = new MetadataDictionary();
        data.put((byte) 0, new MetadataByte((byte) 0)); //TODO: Is player on fire
        data.put((byte) 1, new MetadataShort((short) 300)); //Air
        data.put((byte) 2, new MetadataString(getName()));
        data.put((byte) 3, new MetadataByte((byte) 1)); //Hide nametag (1: false, 0: true)
        data.put((byte) 4, new MetadataByte((byte) 0)); //silent
        data.put((byte) 7, new MetadataInt(0)); //Potion color
        data.put((byte) 8, new MetadataByte((byte) 0)); //Potion ambient
        data.put((byte) 15, new MetadataByte((byte) 0)); //No ai
        data.put((byte) 16, new MetadataByte((byte) 0)); //Player flags
        //data.put((byte) 17, new MetadataLong((byte) 0));
        setMetadata(data);

        /*
        EntityMetadata em = new EntityMetadata();
        em.set((byte) 0, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 0)); //Is player on fire
        em.set((byte) 1, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_SHORT, (short) 300)); //Strange Air thing
        em.set((byte) 2, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_STRING, getNametag())); //nametag
        em.set((byte) 3, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 1)); //Hide nametag?
        em.set((byte) 4, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 0)); //silent thing
        em.set((byte) 7, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_INT, 0)); //Potion color
        em.set((byte) 8, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 0)); //Potion ambient
        em.set((byte) 15, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 0)); //No ai
        em.set((byte) 16, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 0)); //Player flags
        em.set((byte) 17, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_LONG, (long) 0));
        setMetadata(em);
        */
        super.initEntity();
    }

    @Override
    public void spawnTo(Player player){
        if(player != this){
            if(this instanceof Player){
                Player me = (Player) this;
                player.getProtocol().sendImmediateResponse(new AddPlayerResponse(me), player);
            }
        }
    }

    @Override
    public void despawnFrom(Player player){
        if(player != this){
            if(this instanceof Player){
                Player me = (Player) this;
                player.getProtocol().sendImmediateResponse(new RemovePlayerResponse(me), player);
            }
        }
    }
    
}
