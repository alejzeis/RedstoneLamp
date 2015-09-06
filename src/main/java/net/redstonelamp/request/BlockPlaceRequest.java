package net.redstonelamp.request;

import net.redstonelamp.block.Block;
import net.redstonelamp.math.Vector3;

/**
 * A Request to place a block at a position.
 *
 * @author RedstoneLamp Team
 */
public class BlockPlaceRequest extends Request{
    public Block block;
    public Vector3 blockPosition;

    public BlockPlaceRequest(Block block, Vector3 blockPosition) {
        this.block = block;
        this.blockPosition = blockPosition;
    }

    @Override
    public void execute() {

    }
}
