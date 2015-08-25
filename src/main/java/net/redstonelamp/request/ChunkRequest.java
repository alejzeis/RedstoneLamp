package net.redstonelamp.request;

import net.redstonelamp.level.ChunkPosition;

/**
 * A Request for a Chunk to be sent.
 *
 * @author RedstoneLamp Team
 */
public class ChunkRequest extends Request{
    public ChunkPosition position;

    public ChunkRequest(ChunkPosition position) {
        this.position = position;
    }

    @Override
    public void execute() {
        //TODO: ?
    }
}
