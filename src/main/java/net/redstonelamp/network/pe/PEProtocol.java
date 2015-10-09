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
package net.redstonelamp.network.pe;

import net.redstonelamp.Player;
import net.redstonelamp.language.PEMessageTranslator;
import net.redstonelamp.network.LowLevelNetworkException;
import net.redstonelamp.network.NetworkManager;
import net.redstonelamp.network.Protocol;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.pe.sub.PESubprotocolManager;
import net.redstonelamp.network.pe.sub.Subprotocol;
import net.redstonelamp.network.pe.sub.v27.SubprotocolV27;
import net.redstonelamp.network.pe.sub.v34.SubprotocolV34;
import net.redstonelamp.nio.BinaryBuffer;
import net.redstonelamp.request.Request;
import net.redstonelamp.response.Response;

import java.net.SocketAddress;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The Minecraft: Pocket Edition Protocol implementation
 *
 * @author RedstoneLamp Team
 */
public class PEProtocol extends Protocol{

    private final Map<String, Subprotocol> addressToSubprotocols = new HashMap<>();
    private final List<String> hasBeenOpened = new CopyOnWriteArrayList<>(); //List of sessions open
    private final PESubprotocolManager subprotocols;
    private final PeChunkSender sender;

    /**
     * Construct new PEProtocol.
     *
     * @param manager
     */
    public PEProtocol(NetworkManager manager){
        super(manager);
        subprotocols = new PESubprotocolManager(this);
        sender = new PeChunkSender(this);
        //_interface = new JRakLibPlusInterface(manager.getServer(), this);
        _interface = new JRakLibInterface(manager.getServer(), this);

        subprotocols.registerSubprotocol(new SubprotocolV27(subprotocols));
        subprotocols.registerSubprotocol(new SubprotocolV34(subprotocols));

        manager.getServer().getTranslationManager().registerTranslator(getClass(), new PEMessageTranslator());
    }

    @Override
    public String getName(){
        return "MCPE";
    }

    @Override
    public String getDescription(){
        return "Minecraft: Pocket Edition protocol, version " + PENetworkConst.MCPE_VERSION + " (protocol: " + PENetworkConst.MCPE_PROTOCOL + ")";
    }

    /**
     * default method that sends the correct packets in event of a subprotocol not found
     *
     * @param sendTo
     */
    private void defaultNoSubprotocolFoundDisconnect(SocketAddress sendTo){
        BinaryBuffer bb = BinaryBuffer.newInstance(5, ByteOrder.BIG_ENDIAN);
        bb.putByte(PENetworkConst.PLAY_STATUS_PACKET);
        bb.putInt(1); //LOGIN_FAILED_CLIENT
        sendImmediatePacket(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, sendTo));

        String message = "disconnectionScreen.outdatedClient";
        bb = BinaryBuffer.newInstance(3 + message.getBytes().length, ByteOrder.BIG_ENDIAN);
        bb.putByte(PENetworkConst.DISCONNECT_PACKET);
        bb.putString(message);
        sendImmediatePacket(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, sendTo));
    }

    @Override
    public Request[] handlePacket(UniversalPacket packet){
        if(addressToSubprotocols.containsKey(packet.getAddress().toString())){
            return addressToSubprotocols.get(packet.getAddress().toString()).handlePacket(packet);
        }else{
            getManager().getServer().getLogger().debug("Searching for subprotocol for " + packet.getAddress().toString());
            Subprotocol s = subprotocols.findSubprotocol(packet);
            if(s != null){
                addressToSubprotocols.put(packet.getAddress().toString(), s);
                getManager().getServer().getLogger().debug("Found subprotocol for " + s.getMCPEVersion() + " (" + s.getProtocolVersion() + ")");
                packet.bb().setPosition(0); //Reset the position to zero
                return s.handlePacket(packet); //TODO: Since finding the protocol already processes the packet, we are doing the same thing twice
            }else{
                getManager().getServer().getLogger().info("Could not find protocol for " + packet.getAddress().toString() + ", disconnecting.");
                defaultNoSubprotocolFoundDisconnect(packet.getAddress());
                ((PEInterface) _interface)._internalClose(packet.getAddress(), "no subprotocol found");
                return new Request[]{null};
            }
        }
    }

    @Override
    protected UniversalPacket[] _sendResponse(Response response, Player player){
        if(addressToSubprotocols.containsKey(player.getAddress().toString())){
            return addressToSubprotocols.get(player.getAddress().toString()).translateResponse(response, player);
        }/*else{
            throw new IllegalArgumentException("Player " + player.getAddress().toString() + " not found in subprotocol map!");
        }*/
        return new UniversalPacket[0];
    }

    @Override
    protected UniversalPacket[] _sendQueuedResponses(Response[] responses, Player player){
        if(addressToSubprotocols.containsKey(player.getAddress().toString())){
            UniversalPacket[] packets = addressToSubprotocols.get(player.getAddress().toString()).translateQueuedResponse(responses, player);
            if(packets == null){
                List<UniversalPacket> combinedPackets = new ArrayList<>();
                for(Response r : responses){
                    combinedPackets.addAll(Arrays.asList(_sendResponse(r, player)));
                }
                return combinedPackets.toArray(new UniversalPacket[combinedPackets.size()]);
            }
            return packets;
        }else{
            throw new IllegalArgumentException("Player " + player.getAddress().toString() + " not found in subprotocol map!");
        }
    }

    @Override
    protected void onClose(Player player){
        addressToSubprotocols.remove(player.getAddress().toString());
        sender.clearData(player);
        ((PEInterface) _interface)._internalClose(player.getAddress(), "server disconnect");
    }

    @Override
    protected void _shutdown() {
        sender.onShutdown();
    }

    protected void openSession(String session){
        if(!hasBeenOpened.contains(session)){
            hasBeenOpened.add(session);
        }
    }

    protected void sendImmediatePacket(UniversalPacket packet){
        try{
            _interface.sendPacket(packet, true);
        }catch(LowLevelNetworkException e){
            getServer().getLogger().error(e.getClass().getName() + " while sending packet immediately: " + e.getMessage());
            getServer().getLogger().trace(e);
        }
    }

    public PeChunkSender getChunkSender(){
        return sender;
    }
}