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
 * An element implementation of an Integer
 * This code is based off of the code here: https://github.com/NiclasOlofsson/MiNET/tree/master/src/MiNET/MiNET/Utils
 *
 * @author RedstoneLamp team
 */
public class MetadataInt extends MetadataElement{
    private int i;

    public MetadataInt(){
    }

    public MetadataInt(int i){
        this.i = i;
    }

    @Override
    public void fromBytes(BinaryBuffer bb){
        i = bb.getInt();
    }

    @Override
    public void toBytes(BinaryBuffer bb, int index){
        bb.putByte(getKey((byte) index));
        bb.putInt(i);
    }

    @Override
    public byte getIdentifier(){
        return 2;
    }

    @Override
    public int getLength() {
        return 4;
    }
}
