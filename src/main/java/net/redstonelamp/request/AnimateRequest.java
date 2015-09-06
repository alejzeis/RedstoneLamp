package net.redstonelamp.request;

/**
 * Created by gunnar on 06.09.15.
 */
public class AnimateRequest extends Request {
    private final byte actionID;

    public AnimateRequest(byte actionID) {
        this.actionID = actionID;
    }

    public byte getActionID() {
        return actionID;
    }

    @Override
    public void execute() {

    }
}
