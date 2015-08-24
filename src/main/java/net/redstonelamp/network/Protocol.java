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

import net.redstonelamp.Player;
import net.redstonelamp.Server;
import net.redstonelamp.request.LoginRequest;
import net.redstonelamp.request.Request;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class for a Protocol.
 *
 * @author RedstoneLamp Team
 */
public abstract class Protocol {
    private NetworkManager manager;
    private final Map<String, Player> players = new ConcurrentHashMap<>();
    protected NetworkInterface _interface;

    /**
     * Abstract Protocol constructor, all implementing classes MUST SET _interface IN CONSTRUCTOR.
     * @param manager
     */
    public Protocol(NetworkManager manager){
        this.manager = manager;
    }

    protected final void tick() {
        try {
            UniversalPacket packet;
            while((packet = _interface.readPacket()) != null) {
                Request[] requests = handlePacket(packet);
                for(Request r : requests) {
                    if (r != null) {
                        if (players.containsKey(packet.getAddress().toString())) {
                            players.get(packet.getAddress().toString()).handleRequest(r);
                        } else {
                            if (r instanceof LoginRequest) {
                                Player player = manager.getServer().openSession(packet.getAddress(), this, (LoginRequest) r);
                                players.put(player.getIdentifier(), player);
                            } else {
                                manager.getServer().getLogger().warning("Failed to open session, Request: " + r.getClass().getName());
                            }
                        }
                    }
                }
            }
        } catch (LowLevelNetworkException e) {
            manager.getServer().getLogger().trace(e);
        }
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
     * Handles a <code>UniversalPacket</code> and translates it into a <code>Request</code>
     * @param packet The <code>UniversalPacket</code>
     * @return The Request if translated, null if not.
     */
    public abstract Request[] handlePacket(UniversalPacket packet);

    /**
     * Send a <code>UniversalPacket</code>
     * @param packet The <code>UniversalPacket</code> to be sent
     * @return If the packet was sent successfully
     */
    public boolean sendPacket(UniversalPacket packet) {
        try {
            _interface.sendPacket(packet);
            return true;
        } catch (LowLevelNetworkException e) {
            manager.getServer().getLogger().trace(e);
            return false;
        }
    }

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
