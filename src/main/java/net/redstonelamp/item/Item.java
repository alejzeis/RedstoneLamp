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

import net.redstonelamp.block.*;
import org.spout.nbt.CompoundTag;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an Item.
 *
 * @author RedstoneLamp Team
 */
public class Item implements Items {
    private static final Map<Integer, Class<? extends Item>> items = new HashMap<>();
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
        items.clear();
        initItems();
        initCreativeItems();
    }

    private static void initItems() {
        items.put(DIRT, Dirt.class);
        items.put(GRASS, Grass.class);
        items.put(STONE, Stone.class);
        items.put(TALL_GRASS, TallGrass.class);
    }

    private static void initCreativeItems() {
        addCreativeItem(get(DIRT, (short) 0));
        addCreativeItem(get(GRASS, (short) 0));
        addCreativeItem(get(STONE, (short) 0));
        addCreativeItem(get(TALL_GRASS, (short) 0));
    }

    public static synchronized void addCreativeItem(Item item) {
        creativeItems.add(get(item.getId(), item.getMeta()));
    }

    public static Item get(int id) {
        return get(id, (short) 0, 1);
    }

    public static Item get(int id, short meta) {
        return get(id, meta, 1);
    }

    public static Item get(int id, short meta, int count) {
        if(items.containsKey(id)) {
            try {
                Constructor c = items.get(id).getConstructor(int.class, short.class, int.class);
                return (Item) c.newInstance(id, meta, count);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return new Item(id, meta, count);
        }
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
