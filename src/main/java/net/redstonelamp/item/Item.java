package net.redstonelamp.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Item.
 *
 * @author RedstoneLamp Team
 */
public class Item {
    private static final List<Item> implementedItems = new ArrayList<>();
    private final int id;
    private final short meta;
    private final int count;

    public Item(int id, short meta, int count) {
        this.id = id;
        this.meta = meta;
        this.count = count;
    }

    public static void init() {
        implementedItems.clear();
        //TODO: register implemented items
    }

    public int getId() {
        return id;
    }

    public short getMeta() {
        return meta;
    }

    public int getCount() {
        return count;
    }
}
