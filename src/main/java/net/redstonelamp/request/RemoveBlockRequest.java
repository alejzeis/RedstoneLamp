package net.redstonelamp.request;

import net.redstonelamp.level.position.BlockPosition;

/**
 * A Request to remove a block.
 *
 * @author RedstoneLamp Team
 */
public class RemoveBlockRequest extends Request{
    public BlockPosition position;

    public RemoveBlockRequest(BlockPosition position) {
        this.position = position;
    }

    @Override
    public void execute() {

    }
}
