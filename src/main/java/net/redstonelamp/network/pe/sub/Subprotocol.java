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
package net.redstonelamp.network.pe.sub;

import net.redstonelamp.Player;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.pe.PEProtocol;
import net.redstonelamp.request.Request;
import net.redstonelamp.response.Response;

/**
 * Represents a sub-protocol of the MCPE protocol. Each subprotocol handles a different version of the MCPE protocol.
 *
 * @author RedstoneLamp Team
 */
public abstract class Subprotocol{
    private final PESubprotocolManager manager;
    private final PEProtocol protocol;

    protected Subprotocol(PESubprotocolManager manager){
        this.manager = manager;
        protocol = manager.getProtocol();
    }

    /**
     * Handle a <code>UniversalPacket</code> and translate it into a <code>Request</code>
     *
     * @param up The UniversalPacket recieved
     * @return A translated Request if successful, null if not
     */
    public abstract Request[] handlePacket(UniversalPacket up);

    /**
     * Translates a  <code>Response</code> to a <code>UniversalPacket</code> array.
     * The Response may translate to more than one packet.
     *
     * @param response The Response to be translated
     * @param player   The Player this response was sent from
     * @return A UniversalPacket array, containing all the packets the response needs.
     */
    public abstract UniversalPacket[] translateResponse(Response response, Player player);

    /**
     * Some Responses are queued in order to combine them into one or more packets. Some protocols support this, some do not.
     * If the subprotocol does not support this, return "null" and the parent PEProtocol will handle it for you. The responses
     * will all be of the same type.
     *
     * @param responses The Responses to be combined into one or more packets.
     * @param player    The Player these responses are sent to
     * @return A UniversalPacket array containing all the packets the queued responses need, or "null" if the
     * subprotocol does not support it.
     */
    public abstract UniversalPacket[] translateQueuedResponse(Response[] responses, Player player);

    /**
     * Get the version of MCPE that this subprotocol implements as a String
     *
     * @return The version of MCPE that this subprotocol implements
     */
    public abstract String getMCPEVersion();

    /**
     * Get the protocol version of MCPE that this subprotocol implements
     *
     * @return The protocol version of MCPE that this subprotocol implements
     */
    public abstract int getProtocolVersion();

    /**
     * Get the SubprotocolManager for this Subprotocol
     *
     * @return The Subprotocol manager that manages this subprotocol
     */
    public PESubprotocolManager getManager(){
        return manager;
    }

    /**
     * Get the PEProtocol for this Subprotocol
     *
     * @return The PEProtocol this Subprotocol belongs to
     */
    public PEProtocol getProtocol(){
        return protocol;
    }
}
