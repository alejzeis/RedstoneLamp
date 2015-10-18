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
package net.redstonelamp.network;

import net.redstonelamp.Player;
import net.redstonelamp.Server;
import net.redstonelamp.network.netInterface.NetworkInterface;
import net.redstonelamp.request.LoginRequest;
import net.redstonelamp.request.Request;
import net.redstonelamp.response.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Base class for a Protocol.
 *
 * @author RedstoneLamp Team
 */
public abstract class Protocol{
    private NetworkManager manager;
    private final Queue<Request> requestQueue = new ArrayDeque<>();
    private final Map<String, Player> players = new ConcurrentHashMap<>();
    protected NetworkInterface _interface;

    /**
     * Abstract Protocol constructor, all implementing classes MUST SET _interface IN CONSTRUCTOR.
     *
     * @param manager
     */
    public Protocol(NetworkManager manager){
        this.manager = manager;
    }

    protected final void tick() {
        try{
            manager.getActionPool().execute(() -> {
                UniversalPacket packet;
                try {
                    while ((packet = _interface.readPacket()) != null) {
                        Request[] requests = handlePacket(packet);
                        for (Request r : requests) {
                            r.from = packet.getAddress();
                        }
                        Collections.addAll(requestQueue, requests);
                    }
                } catch (LowLevelNetworkException e) {
                    e.printStackTrace();
                }
            });
            int max = 25;
            if(!requestQueue.isEmpty()) {
                while(max > 0 && !requestQueue.isEmpty()) {
                    max = max - 1;
                    Request r = requestQueue.remove();
                    if(players.containsKey(r.from.toString())){
                        manager.getActionPool().execute(() -> players.get(r.from.toString()).handleRequest(r));
                        //players.get(r.from.toString()).handleRequest(r);
                    }else{
                        if(r instanceof LoginRequest){
                            final Player player = manager.getServer().openSession(r.from, this, (LoginRequest) r);
                            players.put(player.getAddress().toString(), player);
                            manager.getActionPool().execute(() -> player.handleRequest(r));
                            //player.handleRequest(r);
                        }else{
                            manager.getServer().getLogger().warning("Failed to open session, Request: " + r.getClass().getName());
                        }
                    }
                }
                //if(max < 25) System.out.println("processed: "+max);
            }
        }catch(Exception e){
            manager.getServer().getLogger().trace(e);
        }
    }

    /**
     * Get this protocol's name.
     *
     * @return The name of this protocol.
     */
    public abstract String getName();

    /**
     * Get this protocol's description.
     *
     * @return The description of this protocol.
     */
    public abstract String getDescription();

    /**
     * Handles a <code>UniversalPacket</code> and translates it into a <code>Request</code>
     *
     * @param packet The <code>UniversalPacket</code>
     * @return The Request if translated, null if not.
     */
    public abstract Request[] handlePacket(UniversalPacket packet);

    /**
     * Send a <code>Response</code> by translating it into a native packet
     *
     * @param response The Response to be sent
     * @param player   The Player the response is being sent from
     */
    public void sendResponse(Response response, Player player){
        manager.getActionPool().execute(() -> {
            try {
                UniversalPacket[] packets = _sendResponse(response, player);
                for (UniversalPacket packet : packets) {
                    try {
                        _interface.sendPacket(packet, false);
                    } catch (LowLevelNetworkException e) {
                        manager.getServer().getLogger().error(e.getClass().getName() + " while sending response " + response.getClass().getName() + ": " + e.getMessage());
                        manager.getServer().getLogger().trace(e);
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                //TODO
            }
        });
    }

    /**
     * Sends a queue of responses to a player, combining them into one or more packets if possible.
     *
     * @param responses The queue of responses.
     * @param player    The player they are to be sent to.
     */
    public void sendQueuedResponses(Response[] responses, Player player){
        manager.getActionPool().execute(() -> {
            List<Response> typeResponses = new ArrayList<>();
            List<Response> rest = new ArrayList<>();
            typeResponses.add(responses[0]);
            for (Response r : responses) {
                if (r.getClass().getName().equals(typeResponses.get(0).getClass().getName()) && r != typeResponses.get(0)) {
                    typeResponses.add(r);
                } else if (r != typeResponses.get(0)) {
                    rest.add(r);
                }
            }
            UniversalPacket[] packets = _sendQueuedResponses(typeResponses.toArray(new Response[typeResponses.size()]), player);
            if (packets == null) { //Check if protocol supports
                //protocol doesn't support
                for (Response r : responses) {
                    sendResponse(r, player);
                }
                return;
            }
            for (UniversalPacket packet : packets) {
                try {
                    _interface.sendPacket(packet, false);
                } catch (LowLevelNetworkException e) {
                    manager.getServer().getLogger().error(e.getClass().getName() + " while sending queued responses of type " + responses[0].getClass().getName() + ": " + e.getMessage());
                    manager.getServer().getLogger().trace(e);
                }
            }
            if (!rest.isEmpty()) {
                sendQueuedResponses(rest.toArray(new Response[rest.size()]), player);
            }
        });
    }

    /**
     * Send a <code>Response</code> by translating it into a native packet.
     * The packet will be sent IMMEDIATLY and should skip any queues the underlying NetworkInterface
     * has.
     *
     * @param response The Response to be sent
     * @param player   The Player the response is being sent from
     */
    public void sendImmediateResponse(Response response, Player player){
        UniversalPacket[] packets = _sendResponse(response, player);
        for (UniversalPacket packet : packets) {
            try {
                _interface.sendPacket(packet, true);
            } catch (LowLevelNetworkException e) {
                manager.getServer().getLogger().error(e.getClass().getName() + " while sending response " + response.getClass().getName() + ": " + e.getMessage());
                manager.getServer().getLogger().trace(e);
            }
        }
    }

    /**
     * Special method called whenever a player is closing. Please use <code>Player.kick()</code> or <code>Player.close()</code> as those
     * methods call this one.
     *
     * @param player The Player closing
     */
    public final void close(Player player){
        players.remove(player.getAddress().toString());
        onClose(player);
    }

    /**
     * This method translates a <code>Response</code> to a UniversalPacket array
     *
     * @param response
     * @param player
     * @return
     */
    protected abstract UniversalPacket[] _sendResponse(Response response, Player player);

    /**
     * This method translates a queue of <code>Responses</code> to a UniversalPacket array. Some protocols
     * have packets than can have multiple "records", combining multiple Responses into one or more packets. If your
     * protocol does not support this, then return "null" and the superclass will call _sendResponse one by one. The Response array
     * will always have Responses of all the same type.
     *
     * @param responses The Queued responses to be sent.
     * @param player    The Player these responses will be sent to
     * @return A UniversalPacket array of the translated queued responses, or "null" if the protocol does not support it.
     */
    protected abstract UniversalPacket[] _sendQueuedResponses(Response[] responses, Player player);

    /**
     * This method is called whenever a Player is closed. You can override this method if you have to
     * do extra things when a session is closed.
     *
     * @param player The player which is closing.
     */
    protected void onClose(Player player){

    }

    /**
     * Send a <code>UniversalPacket</code>
     *
     * @param packet The <code>UniversalPacket</code> to be sent
     * @return If the packet was sent successfully
     */
    public boolean sendPacket(UniversalPacket packet){
        try{
            _interface.sendPacket(packet, false);
            return true;
        }catch(LowLevelNetworkException e){
            manager.getServer().getLogger().trace(e);
            return false;
        }
    }

    /**
     * Get the <code>NetworkManager</code> that this protocol belongs to.
     *
     * @return The <code>NetworkManager</code> the protocol belongs to.
     */
    public NetworkManager getManager(){
        return manager;
    }

    /**
     * Get the <code>Server</code> that belongs to the <code>NetworkManager</code>
     *
     * @return The <code>Server</code> belonging to the <code>NetworkManager</code>
     */
    public Server getServer(){
        return manager.getServer();
    }

    /**
     * Get the NetworkInterface this protocol uses to read and write packets.
     * @return The NetworkInterface used by this protocol.
     */
    public NetworkInterface getInterface() {
        return _interface;
    }

    protected final void onShutdown() {
        try {
            _interface.shutdown();
        } catch (LowLevelNetworkException e) {
            e.printStackTrace();
        } finally {
            _shutdown();
        }
    }

    /**
     * Method overridden in subclasses for extra cleanup when shutting down.
     */
    protected void _shutdown() {

    }

    @Override
    public String toString(){
        return getName() + " - " + getDescription();
    }
}
