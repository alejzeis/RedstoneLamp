/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
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
