package net.redstonelamp.response;

import net.redstonelamp.level.position.BlockPosition;

/**
 * A Response to remove a block
 *
 * @author RedstoneLamp Team
 */
public class RemoveBlockResponse extends Response {
    public BlockPosition position;

    public RemoveBlockResponse(BlockPosition position) {
        this.position = position;
    }

}
