package net.redstonelamp.response;

/**
 * Created by gunnar on 06.09.15.
 */
public class AnimateResponse extends Response {
    private final long entityID;
    private final byte actionID;

    public AnimateResponse(long entityID, byte actionID) {
        this.entityID = entityID;
        this.actionID = actionID;
    }

    public long getEntityID() {
        return entityID;
    }

    public byte getActionID() {
        return actionID;
    }
}
