package net.redstonelamp.response;

import net.redstonelamp.request.AnimateRequest;

/**
 * A Response to an AnimateRequest
 *
 * @author RedstoneLamp Team
 */
public class AnimateResponse extends Response {
    public AnimateRequest.ActionType actionType;

    public AnimateResponse(AnimateRequest.ActionType actionType) {
        this.actionType = actionType;
    }
}
