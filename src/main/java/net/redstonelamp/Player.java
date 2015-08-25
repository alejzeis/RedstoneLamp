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

import net.redstonelamp.level.position.Position;
import net.redstonelamp.network.Protocol;
import net.redstonelamp.request.ChunkRequest;
import net.redstonelamp.request.LoginRequest;
import net.redstonelamp.request.Request;
import net.redstonelamp.request.SpawnRequest;
import net.redstonelamp.response.*;

import java.net.SocketAddress;

/**
 * Protocol-independent Player class. Represents a Player on the server
 *
 * @author RedstoneLamp Team
 */
public class Player {
    private final Protocol protocol;
    private final Server server;
    private final SocketAddress address;
    private final String identifier;

    private String username;
    private int clientID;
    private byte[] skin;
    private boolean slim;

    private Position position;

    private boolean spawned = false;

    /**
     * Construct a new Player instance belonging to the specified <code>Protocol</code> with the <code>identifier</code>
     * @param protocol The protocol this player belongs to
     * @param identifier The client's identifier. This is the address the player is connecting from, in the format:
     *                   [ip]:[port]
     */
    @Deprecated
    public Player(Protocol protocol, String identifier) {
        this.protocol = protocol;
        this.identifier = identifier;
        address = null;

        server = protocol.getManager().getServer();
    }
    /**
     * Construct a new Player instance belonging to the specified <code>Protocol</code> connecting from
     * <code>address</code>
     * @param protocol The protocol this player belongs to
     * @param address The SocketAddress this player is connecting from
     */
    public Player(Protocol protocol, SocketAddress address) {
        this.protocol = protocol;
        this.address = address;

        identifier = address.toString();
        server = protocol.getManager().getServer();

        loadPlayerData();
    }

    private void loadPlayerData() {
        //TODO: Load real data
        position = server.getLevelManager().getMainLevel().getSpawnPosition();
    }

    /**
     * Sends a <code>Response</code> to the client. If they are available please use API methods.
     * @param r The Response to be sent.
     */
    public void sendResponse(Response r) {
        protocol.sendResponse(r, this);
    }

    /**
     * Handles a Request from the client.
     * @param request The request from the client
     */
    public void handleRequest(Request request) {
        if(request instanceof LoginRequest) {
            LoginRequest lr = (LoginRequest) request;
            username = lr.username;
            clientID = (int) lr.clientId;
            skin = lr.skin;
            slim = lr.slim;

            LoginResponse response = new LoginResponse(true, 1, (float) getPosition().getX(), (float) getPosition().getY(), (float) getPosition().getZ());
            if(server.getPlayers().size() > server.getMaxPlayers()) {
                response.loginAllowed = false;
                response.loginNotAllowedReason = "redstonelamp.loginFailed.serverFull";
                protocol.sendImmediateResponse(response, this);
                close("", "redstonelamp.loginFailed.serverFull", false);
                return;
            }
            sendResponse(response);
            server.getLogger().info(username+"["+address+"] logged in."); //TODO: more info
        } else if(request instanceof ChunkRequest) {
            ChunkRequest r = (ChunkRequest) request;
            ChunkResponse response = new ChunkResponse(getPosition().getLevel().getChunkAt(r.position));
            sendResponse(response);
        } else if(request instanceof SpawnRequest) {
            SpawnResponse sr = new SpawnResponse(getPosition());
            sendResponse(sr);
        }
    }

    public void close(String leaveMessage, String reason, boolean notifyClient) {
        DisconnectResponse dr = new DisconnectResponse(reason);
        dr.notifyClient = notifyClient;

        protocol.sendImmediateResponse(dr, this);

        server.closeSession(this);
        server.getLogger().info(username+"["+identifier+"] logged out with reason: "+reason);
        protocol.close(this);
        /*
        if(leaveMessage != "") {
            server.broadcastMessage(username + leaveMessage);
        }
        */
    }

    public Protocol getProtocol() {
        return protocol;
    }

    @Deprecated
    public String getIdentifier() {
        return identifier;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}