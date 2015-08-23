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
package net.redstonelamp;

import net.redstonelamp.network.Protocol;
import net.redstonelamp.request.Request;

/**
 * Protocol-independent Player class. Represents a Player on the server
 *
 * @author RedstoneLamp Team
 */
public class Player {
    private final Protocol protocol;
    private final String identifier;

    /**
     * Construct a new Player instance belonging to the specified <code>Protocol</code> with the <code>identifier</code>
     * @param protocol The protocol this player belongs to
     * @param identifier The client's identifier. This is the address the player is connecting from, in the format:
     *                   [ip]:[port]
     */
    public Player(Protocol protocol, String identifier) {
        this.protocol = protocol;
        this.identifier = identifier;
    }

    public void handleRequest(Request request) {

    }

    public Protocol getProtocol() {
        return protocol;
    }

    public String getIdentifier() {
        return identifier;
    }
}
