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
package net.redstonelamp.network;

import net.redstonelamp.Server;

/**
 * Base class for a Protocol.
 *
 * @author RedstoneLamp Team
 */
public abstract class Protocol {
    private NetworkManager manager;

    public Protocol(NetworkManager manager){
        this.manager = manager;
    }

    /**
     * Get this protocol's name.
     * @return The name of this protocol.
     */
    public abstract String getName();

    /**
     * Get this protocol's description.
     * @return The description of this protocol.
     */
    public abstract String getDescription();

    /**
     * Get the <code>NetworkManager</code> that this protocol belongs to.
     * @return The <code>NetworkManager</code> the protocol belongs to.
     */
    public NetworkManager getManager() {
        return manager;
    }

    /**
     * Get the <code>Server</code> that belongs to the <code>NetworkManager</code>
     * @return The <code>Server</code> belonging to the <code>NetworkManager</code>
     */
    public Server getServer() {
        return manager.getServer();
    }

    @Override
    public String toString() {
        return getName() + " - " + getDescription();
    }
}
