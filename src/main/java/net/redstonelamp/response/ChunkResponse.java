package net.redstonelamp.response;

import net.redstonelamp.level.Chunk;

/**
 * A Response to a Chunk Request.
 *
 * @author RedstoneLamp Team
 */
public class ChunkResponse extends Response{
    public Chunk chunk;

    public ChunkResponse(Chunk chunk) {
        this.chunk = chunk;
    }

}
