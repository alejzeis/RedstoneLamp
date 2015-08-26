/**
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
package net.redstonelamp.response;

import net.redstonelamp.level.position.Position;

/**
 * A Response to a Player Move Request.
 *
 * @author RedstoneLamp Team
 */
public class PlayerMoveResponse extends TeleportResponse{

    public long entityID;

    public PlayerMoveResponse(long eid, Position pos, boolean onGround) {
        super(pos, onGround);
        entityID = eid;
    }
}
