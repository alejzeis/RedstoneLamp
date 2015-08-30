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
package net.redstonelamp.network.pc;

import net.redstonelamp.Player;
import net.redstonelamp.network.NetworkManager;
import net.redstonelamp.network.Protocol;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.pc.codec.HandshakePacket;
import net.redstonelamp.request.Request;
import net.redstonelamp.request.StatusRequest;
import net.redstonelamp.response.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * A Protocol implementation of the Minecraft: PC protocol.
 *
 * @author RedstoneLamp Team
 */
public class PCProtocol extends Protocol implements PCNetworkConst{

    public PCProtocol(NetworkManager manager){
        super(manager);
        _interface = new MinaInterface(manager.getServer(), this);
    }

    @Override
    public String getName(){
        return "MCPC";
    }

    @Override
    public String getDescription(){
        return "Minecraft: PC edition protocol, version " + MC_VERSION + " (protocol " + MC_PROTOCOL + ")";
    }

    @Override
    public Request[] handlePacket(UniversalPacket packet){
        List<Request> requests = new ArrayList<Request>();
        int id = packet.bb().getVarInt();

        switch(id){
            case HANDSHAKE_HANDSHAKE:
                HandshakePacket handshake = new HandshakePacket(packet);
                if(handshake.nextState == handshake.STATUS){
                    StatusRequest status = new StatusRequest();
                    status.protocol = handshake.protocol;
                    status.address = handshake.address;
                    status.port = handshake.port;
                    System.out.println("Decoded status for " + handshake.address);
                }else if(handshake.nextState == handshake.LOGIN){

                }
                break;
        }

        return requests.toArray(new Request[requests.size()]);
    }

    @Override
    protected UniversalPacket[] _sendResponse(Response response, Player player){
        return new UniversalPacket[0];
    }
}
