package net.redstonelamp.block;

import net.redstonelamp.item.Item;

/**
 * Base class for all blocks
 *
 * @author RedstoneLamp Team
 */
public class Block extends Item{

    public Block(int id, short meta, int count) {
        super(id, meta, count);
    }
}
