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
package net.redstonelamp.metadata;


import net.redstonelamp.nio.BinaryBuffer;

/**
 * Represents an element in a MetadataDictionary.
 *
 * @author RedstoneLamp Team
 */
public abstract class MetadataElement{
    private byte index;

    public abstract void fromBytes(BinaryBuffer bb);

    public abstract void toBytes(BinaryBuffer bb, int index);

    public abstract byte getIdentifier();

    public byte getKey(byte index){
        return (byte) (getIdentifier() << 5 | index & 0x1F);
    }

    public byte getIndex(){
        return index;
    }

    public void setIndex(byte index){
        this.index = index;
    }
}
