package net.redstonelamp.response;

import net.redstonelamp.level.position.Position;

/**
 * A Response that teleports the player to the given position.
 *
 * @author RedstoneLamp Team
 */
public class TeleportResponse extends Response{
    public Position pos;
    public float bodyYaw;
    public boolean onGround;

    public TeleportResponse(Position pos, boolean onGround) {
        this.pos = pos;
        this.onGround = onGround;
        bodyYaw = pos.getYaw();
    }
}
