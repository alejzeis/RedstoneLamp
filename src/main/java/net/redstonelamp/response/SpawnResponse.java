package net.redstonelamp.response;

import net.redstonelamp.level.position.Position;

/**
 * A Response to a SpawnRequest
 *
 * @author RedstoneLamp Team
 */
public class SpawnResponse extends Response{
    private Position spawnPosition;

    public SpawnResponse(Position spawnPosition) {
        this.spawnPosition = spawnPosition;
    }
}
