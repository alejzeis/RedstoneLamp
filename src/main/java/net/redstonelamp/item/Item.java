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
package net.redstonelamp.item;

import net.redstonelamp.block.Dirt;
import net.redstonelamp.block.Grass;
import net.redstonelamp.block.Stone;
import org.spout.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Item.
 *
 * @author RedstoneLamp Team
 */
public class Item{
    private static final List<Item> creativeItems = new ArrayList<>();
    private final int id;
    private final short meta;
    private final int count;
    private CompoundTag compoundTag;

    public Item(int id, short meta, int count){
        this.id = id;
        this.meta = meta;
        this.count = count;
    }

    public static void init(){
        creativeItems.clear();
        creativeItems.add(new Stone((short) 0, 1));
        creativeItems.add(new Dirt((short) 0, 1));
        creativeItems.add(new Grass((short) 0, 1));
    }
    
    public static List<Item> getCreativeItems() {
        return creativeItems;
    }

    public int getId(){
        return id;
    }

    public short getMeta(){
        return meta;
    }

    public int getCount(){
        return count;
    }

    public CompoundTag getCompoundTag() {
        return compoundTag;
    }

    public void setCompoundTag(CompoundTag compoundTag) {
        this.compoundTag = compoundTag;
    }
}
