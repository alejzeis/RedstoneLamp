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
import net.redstonelamp.nio.BinaryBuffer;
import net.redstonelamp.request.LoginRequest;
import net.redstonelamp.request.Request;
import net.redstonelamp.response.LoginResponse;
import net.redstonelamp.response.Response;
import org.json.simple.JSONObject;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
    protected void onClose(Player player) {
        ((MinaInterface) _interface).close(player.getAddress());
    }

    @Override
    public Request[] handlePacket(UniversalPacket packet){
        List<Request> requests = new ArrayList<>();
        ProtocolState state = ((MinaInterface) _interface).getProtocolStateOfAddress(packet.getAddress());
        if(state == null) {
            return new Request[0];
        }
        switch (state) {
            case STATE_LOGIN:
                requests.addAll(Arrays.asList(handleLoginPacket(packet)));
                break;

            case STATE_PLAY:
                requests.addAll(Arrays.asList(handlePlayPacket(packet)));
        }

        return requests.toArray(new Request[requests.size()]);
    }

    private Request[] handleLoginPacket(UniversalPacket packet) {
        List<Request> requests = new ArrayList<>();
        int id = packet.bb().getVarInt();
        switch (id) {
            case LOGIN_LOGIN_START:
                //TODO: Implementing encryption: move login request to EncryptionResponse handle
                String name = packet.bb().getVarString();
                LoginRequest lr = new LoginRequest(name, UUID.nameUUIDFromBytes(name.getBytes()));
                requests.add(lr);
                break;
        }

        return requests.toArray(new Request[requests.size()]);
    }

    private Request[] handlePlayPacket(UniversalPacket packet) {
        List<Request> requests = new ArrayList<>();
        int id = packet.bb().getVarInt();
        BinaryBuffer bb;
        switch (id) {
            case PLAY_KEEP_ALIVE:
                int keepAliveId = packet.bb().getVarInt();
                bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
                bb.putVarInt(PLAY_KEEP_ALIVE);
                bb.putVarInt(keepAliveId);
                sendPacket(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, packet.getAddress()));
                break;
        }
        return requests.toArray(new Request[requests.size()]);
    }

    @Override
    protected UniversalPacket[] _sendResponse(Response response, Player player){
        List<UniversalPacket> packets = new ArrayList<>();
        ProtocolState state = ((MinaInterface) _interface).getProtocolStateOfAddress(player.getAddress());
        if(state == null) {
            return new UniversalPacket[0];
        }
        switch (state) {
            case STATE_LOGIN:
                packets.addAll(Arrays.asList(translateLoginResponse(response, player)));
                break;

            case STATE_PLAY:
                packets.addAll(Arrays.asList(translatePlayResponse(response, player)));
                break;
        }

        return packets.toArray(new UniversalPacket[packets.size()]);
    }

    private UniversalPacket[] translateLoginResponse(Response response, Player player) {
        List<UniversalPacket> packets = new ArrayList<>();
        if(response instanceof LoginResponse) {
            LoginResponse lr = (LoginResponse) response;
            BinaryBuffer bb;
            if(!lr.loginAllowed) {
                bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
                bb.putVarInt(LOGIN_DISCONNECT);
                String message;
                switch (lr.loginNotAllowedReason) {
                    case "redstonelamp.loginFailed.serverFull":
                        message = "Server Full!";
                        break;
                    default:
                        message = lr.loginNotAllowedReason;
                }
                bb.putVarString("{\"text\":\""+message+"\"}");
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, player.getAddress()));
            } else {
                bb = BinaryBuffer.newInstance(1, ByteOrder.BIG_ENDIAN);
                bb.putVarInt(LOGIN_SET_COMPRESSION);
                bb.putVarInt(-1); //Disable compression, TODO: implement compression
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, player.getAddress()));

                bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
                bb.putVarInt(LOGIN_LOGIN_SUCCESS);
                bb.putVarString(player.getUuid().toString()); //Login Success packet sends the UUID as a string
                bb.putVarString(player.getNametag());
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, player.getAddress()));
                ((MinaInterface) _interface).updateProtocolState(ProtocolState.STATE_PLAY, player.getAddress());

                sendInitalLoginPackets(lr, player);
            }
        }
        return packets.toArray(new UniversalPacket[packets.size()]);
    }

    private UniversalPacket[] translatePlayResponse(Response response, Player player) {
        List<UniversalPacket> packets = new ArrayList<>();

        return packets.toArray(new UniversalPacket[packets.size()]);
    }

    private void sendInitalLoginPackets(LoginResponse lr, Player player) {

    }
}
