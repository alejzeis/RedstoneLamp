package net.redstonelamp.request;

/**
 * A request to animate from the Player. There are many types of animations, which
 * can be found in the class <code>ActionType</code>
 *
 * @author RedstoneLamp Team
 */
public class AnimateRequest extends Request {
    public ActionType actionType;

    public AnimateRequest(ActionType actionType) {
        this.actionType = actionType;
    }

    @Override
    public void execute() {

    }

    /**
     * This enum contains all the different types of actions an
     * AnimateRequest/Response supports.
     *
     * @author RedstoneLamp Team
     */
    public static enum ActionType {
        /**
         * This action is when the player's arm swings towards a
         * block or entity.
         */
        SWING_ARM,
        /**
         * This action is when the player is ordered to wake
         * up.
         */
        WAKE_UP;
    }
}
