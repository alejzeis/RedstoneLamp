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
package net.redstonelamp;

import net.redstonelamp.entity.PlayerEntity;
import net.redstonelamp.network.Protocol;
import net.redstonelamp.request.*;
import net.redstonelamp.response.*;

import java.net.SocketAddress;
import java.util.UUID;

/**
 * <strong>Protocol-independent</strong> Player class. Represents a Player on the server
 *
 * @author RedstoneLamp Team
 */
public class Player extends PlayerEntity{
    private final Protocol protocol;
    private final Server server;
    private final SocketAddress address;
    private final String identifier;

    private long startLogin;

    private String username;
    private UUID uuid;
    private int clientID;
    private byte[] skin;
    private boolean slim;

    private boolean connected = true;
    private boolean spawned = false;
    private int gamemode;

    /**
     * Construct a new Player instance belonging to the specified <code>Protocol</code> with the <code>identifier</code>
     *
     * @param protocol   The protocol this player belongs to
     * @param identifier The client's identifier. This is the address the player is connecting from, in the format:
     *                   [ip]:[port]
     */
    @Deprecated
    public Player(Protocol protocol, String identifier){
        this.protocol = protocol;
        this.identifier = identifier;
        address = null;

        server = protocol.getManager().getServer();
    }

    /**
     * Construct a new Player instance belonging to the specified <code>Protocol</code> connecting from
     * <code>address</code>
     *
     * @param protocol The protocol this player belongs to
     * @param address  The SocketAddress this player is connecting from
     */
    public Player(Protocol protocol, SocketAddress address){
        this.protocol = protocol;
        this.address = address;

        identifier = address.toString();
        server = protocol.getManager().getServer();

        loadPlayerData();
    }

    private void loadPlayerData(){
        //TODO: Load real data
        setPosition(server.getLevelManager().getMainLevel().getSpawnPosition());
        gamemode = 1;
    }

    @Override
    protected void initEntity(){
        setEntityID(server.getNextEntityID());
        super.initEntity();
    }

    /**
     * Sends a <code>Response</code> to the client. If they are available please use API methods.
     *
     * @param r The Response to be sent.
     */
    public void sendResponse(Response r){
        protocol.sendResponse(r, this);
    }

    public void sendMessage(String s){
        protocol.sendImmediateResponse(new ChatResponse(s), this);
    }

    public void sendPopup(String s){
        protocol.sendImmediateResponse(new PopupResponse(s), this);
    }

    /**
     * Handles a Request from the client.
     *
     * @param request The request from the client
     */
    public void handleRequest(Request request){
        if(request instanceof LoginRequest){
            LoginRequest lr = (LoginRequest) request;
            startLogin = System.currentTimeMillis();
            username = lr.username;
            uuid = lr.uuid;
            clientID = (int) lr.clientId;
            skin = lr.skin;
            slim = lr.slim;

            setNametag(username);

            LoginResponse response = new LoginResponse(true, gamemode, (float) getPosition().getX(), (float) getPosition().getY(), (float) getPosition().getZ());
            if(server.getPlayers().size() > server.getMaxPlayers()){
                response.loginAllowed = false;
                response.loginNotAllowedReason = "redstonelamp.loginFailed.serverFull";
                protocol.sendImmediateResponse(response, this);
                close("", "redstonelamp.loginFailed.serverFull", false);
                return;
            }
            server.getPlayers().stream()
                    .filter(player -> player != this && player.getNametag().equals(getNametag()))
                    .forEach(player -> player.close(" left the game", "logged in from another location", true));
            sendResponse(response);
            initEntity();
            server.getLogger().info(username + "[" + address + "] logged in with entity ID " + getEntityID() + " in level \"" + getPosition().getLevel().getName() + "\""
                            + " at position [x: " + getPosition().getX() + ", y: " + getPosition().getY() + ", z: " + getPosition().getZ() + "]"
            );
        }else if(request instanceof ChunkRequest){
            ChunkRequest r = (ChunkRequest) request;
            ChunkResponse response = new ChunkResponse(getPosition().getLevel().getChunkAt(r.position));
            sendResponse(response);
        }else if(request instanceof SpawnRequest){
            SpawnResponse sr = new SpawnResponse(getPosition());
            sendResponse(sr);
            TeleportResponse tr = new TeleportResponse(getPosition(), true);
            sendResponse(tr);

            server.getPlayers().stream().filter(player -> player != this && player.getPosition().getLevel() == getPosition().getLevel()).forEach(player -> {
                spawnTo(player);
                player.spawnTo(this);
            });

            server.getLogger().debug("Player " + username + " spawned (took " + (System.currentTimeMillis() - startLogin) + " ms)");
            server.broadcastMessage(username + " joined the game.");
        }else if(request instanceof ChatRequest){
            ChatRequest cr = (ChatRequest) request;
            server.broadcastMessage("<" + username + "> " + cr.message);
        }else if(request instanceof PlayerMoveRequest){
            PlayerMoveRequest pmr = (PlayerMoveRequest) request;
            if(gamemode == 1){
                PlayerMoveResponse response = new PlayerMoveResponse(getEntityID(), pmr.position, pmr.onGround);
                setPosition(pmr.position);
                server.getPlayers().stream().filter(player -> player != this).forEach(player -> player.sendResponse(response));
            } //TODO: Check movement if in survival
        }
    }

    public void close(String leaveMessage, String reason, boolean notifyClient){
        DisconnectResponse dr = new DisconnectResponse(reason);
        dr.notifyClient = notifyClient;

        protocol.sendImmediateResponse(dr, this);

        server.getPlayers().stream().filter(player -> player != this && player.getPosition().getLevel() == getPosition().getLevel()).forEach(this::despawnFrom);

        destroyEntity();

        server.closeSession(this);
        server.getLogger().info(username + "[" + identifier + "] logged out with reason: " + reason);
        connected = false;
        protocol.close(this);

        if(!leaveMessage.isEmpty()){
            server.broadcastMessage(username + leaveMessage);
        }

    }

    public Protocol getProtocol(){
        return protocol;
    }

    @Deprecated
    public String getIdentifier(){
        return identifier;
    }

    public SocketAddress getAddress(){
        return address;
    }

    public boolean isSpawned(){
        return spawned;
    }

    public int getGamemode(){
        return gamemode;
    }

    public byte[] getSkin(){
        return skin;
    }

    public boolean isSlim(){
        return slim;
    }

    public UUID getUuid(){
        return uuid;
    }

    public boolean isConnected(){
        return connected;
    }
}
