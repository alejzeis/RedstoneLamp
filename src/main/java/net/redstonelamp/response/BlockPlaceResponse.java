package net.redstonelamp.response;

import net.redstonelamp.block.Block;
import net.redstonelamp.level.position.BlockPosition;

/**
 * A Response to place a block.
 *
 * @author RedstoneLamp Team
 */
public class BlockPlaceResponse extends Response{
    public static final boolean DEFAULT_placeAllowed = true;

    public boolean placeAllowed = DEFAULT_placeAllowed;
    public Block block;
    public BlockPosition position;

    public BlockPlaceResponse(Block block, BlockPosition position) {
        this.block = block;
        this.position = position;
    }
}
