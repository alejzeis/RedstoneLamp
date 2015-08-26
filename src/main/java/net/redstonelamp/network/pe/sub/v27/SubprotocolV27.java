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
package net.redstonelamp.network.pe.sub.v27;

import java.net.SocketAddress;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.zip.DataFormatException;

import net.redstonelamp.Player;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.pe.sub.PESubprotocolManager;
import net.redstonelamp.network.pe.sub.Subprotocol;
import net.redstonelamp.nio.BinaryBuffer;
import net.redstonelamp.request.ChatRequest;
import net.redstonelamp.request.LoginRequest;
import net.redstonelamp.request.Request;
import net.redstonelamp.response.ChatResponse;
import net.redstonelamp.response.ChunkResponse;
import net.redstonelamp.response.DisconnectResponse;
import net.redstonelamp.response.LoginResponse;
import net.redstonelamp.response.Response;
import net.redstonelamp.response.SpawnResponse;
import net.redstonelamp.utils.CompressionUtils;

/**
 * A subprotocol implementation for the MCPE version 0.11.1 (protocol 27)
 *
 * @author RedstoneLamp Team
 */
public class SubprotocolV27 extends Subprotocol implements ProtocolConst27{

    public SubprotocolV27(PESubprotocolManager manager) {
        super(manager);
    }

    @Override
    public Request[] handlePacket(UniversalPacket up) {
        List<Request> requests = new ArrayList<>();
        byte id = up.bb().getByte();
        if(id == BATCH_PACKET) {
            return processBatch(up);
        }

        switch (id) {
            case LOGIN_PACKET:
                getProtocol().getServer().getLogger().debug("Got Login packet!");
                LoginRequest lr = new LoginRequest(up.bb().getString());
                up.bb().skip(8); //Skip protocol1, protocol 2 (int, int)
                lr.clientId = up.bb().getInt();
                lr.slim = up.bb().getByte() > 0;
                lr.skin = up.bb().get(up.bb().getUnsignedShort()); //Skin written as a String, but the String class seems to corrupt the Skin

                requests.add(lr);
                break;
            case TEXT_PACKET:
            	ChatRequest cr = new ChatRequest(up.bb().getByte());
            	switch(cr.type) {
            		case ChatRequest.TYPE_CHAT:
            			cr.source = up.bb().getString();
            		case ChatRequest.TYPE_RAW:
            		case ChatRequest.TYPE_POPUP:
            		case ChatRequest.TYPE_TIP:
            			cr.message = up.bb().getString();
            			break;
            		case ChatRequest.TYPE_TRANSLATION:
            			cr.message = up.bb().getString();
            			for(int i = 0; i < up.bb().getByte(); i++)
            				cr.parameters[i] = up.bb().getString();
            	}
            	// TODO: Throw PlayerChatEvent
            	requests.add(cr);
            	break;
        }
        return requests.toArray(new Request[requests.size()]);
    }

    @Override
    public UniversalPacket[] translateResponse(Response response, SocketAddress address) {
        List<UniversalPacket> packets = new CopyOnWriteArrayList<>();
        BinaryBuffer bb;
        if(response instanceof LoginResponse) {
            LoginResponse lr = (LoginResponse) response;
            if(lr.loginAllowed) {
                bb = BinaryBuffer.newInstance(5, ByteOrder.BIG_ENDIAN);
                bb.putByte(PLAY_STATUS_PACKET);
                bb.putInt(0); //LOGIN_SUCCESS
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

                bb = BinaryBuffer.newInstance(48, ByteOrder.BIG_ENDIAN);
                bb.putByte(START_GAME_PACKET);
                bb.putInt(-1); //seed
                bb.putInt(lr.generator);
                bb.putInt(lr.gamemode);
                bb.putLong(lr.entityID);
                bb.putInt(lr.spawnX);
                bb.putInt(lr.spawnY);
                bb.putInt(lr.spawnZ);
                bb.putFloat(lr.x);
                bb.putFloat(lr.y);
                bb.putFloat(lr.z);
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

                bb = BinaryBuffer.newInstance(6, ByteOrder.BIG_ENDIAN);
                bb.putByte(SET_TIME_PACKET);
                bb.putInt(0); //TODO: Correct time
                bb.putByte((byte) 1);
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

                bb = BinaryBuffer.newInstance(10, ByteOrder.BIG_ENDIAN);
                bb.putByte(SET_SPAWN_POSITION_PACKET);
                bb.putInt(lr.spawnX);
                bb.putInt(lr.spawnZ);
                bb.putByte((byte) lr.spawnY);
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

                bb = BinaryBuffer.newInstance(5, ByteOrder.BIG_ENDIAN);
                bb.putByte(SET_HEALTH_PACKET);
                bb.putInt(20); //TODO: Correct health
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

                bb = BinaryBuffer.newInstance(5, ByteOrder.BIG_ENDIAN);
                bb.putByte(SET_DIFFICULTY_PACKET);
                bb.putInt(1); //TODO: Correct difficulty
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

                //TODO: If creative, send items

                getProtocol().getChunkSender().registerChunkRequests(getProtocol().getServer().getPlayer(address), 96);
            } else {
                String message;
                switch (lr.loginNotAllowedReason) {
                    case LoginResponse.DEFAULT_loginNotAllowedReason:
                        message = "disconnectionScreen.noReason";
                        break;

                    case "redstonelamp.loginFailed.serverFull":
                        message = "disconnectionScreen.serverFull";
                        break;

                    default:
                        message = lr.loginNotAllowedReason;
                }

                bb = BinaryBuffer.newInstance(3 + message.getBytes().length, ByteOrder.BIG_ENDIAN);
                bb.putByte(DISCONNECT_PACKET);
                bb.putString(message);

                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
            }
        } else if(response instanceof DisconnectResponse) {
            DisconnectResponse dr = (DisconnectResponse) response;
            if(dr.notifyClient) {
                bb = BinaryBuffer.newInstance(3 + dr.reason.getBytes().length, ByteOrder.BIG_ENDIAN);
                bb.putByte(DISCONNECT_PACKET);
                bb.putString(dr.reason);

                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
            }
        } else if(response instanceof ChunkResponse) {
            ChunkResponse cr = (ChunkResponse) response;

            BinaryBuffer ordered = BinaryBuffer.newInstance(83200, ByteOrder.BIG_ENDIAN);
            ordered.put(cr.chunk.getBlockIds());
            ordered.put(cr.chunk.getBlockMeta());
            ordered.put(cr.chunk.getSkylight());
            ordered.put(cr.chunk.getBlocklight());
            ordered.put(cr.chunk.getHeightmap());
            ordered.put(cr.chunk.getBiomeColors());

            byte[] orderedData = ordered.toArray();
            ordered = null;

            bb = BinaryBuffer.newInstance(83213, ByteOrder.BIG_ENDIAN);
            bb.putByte(FULL_CHUNK_DATA_PACKET);
            bb.putInt(cr.chunk.getPosition().getX());
            bb.putInt(cr.chunk.getPosition().getZ());
            bb.putInt(orderedData.length);
            bb.put(orderedData);

            packets.add(new UniversalPacket(Arrays.copyOf(bb.toArray(), bb.getPosition()), ByteOrder.BIG_ENDIAN, address));
        } else if(response instanceof SpawnResponse) {
            SpawnResponse sr = (SpawnResponse) response;

            bb = BinaryBuffer.newInstance(5, ByteOrder.BIG_ENDIAN);
            bb.putByte(PLAY_STATUS_PACKET);
            bb.putInt(3); //PLAY_SPAWN

            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        } else if(response instanceof ChatResponse) {
        	// I have no idea if this should even EXIST here - SuperstarGamer, 2015
        }

        //Compress packets
        packets.stream().filter(packet -> packet.getBuffer().length >= 512 && packet.getBuffer()[0] != BATCH_PACKET).forEach(packet -> { //Compress packets
            byte[] compressed = CompressionUtils.zlibDeflate(packet.getBuffer(), 7);
            BinaryBuffer batch = BinaryBuffer.newInstance(5 + compressed.length, ByteOrder.BIG_ENDIAN);
            batch.putByte(BATCH_PACKET);
            batch.putInt(compressed.length);
            batch.put(compressed);

            packets.remove(packet);
            packets.add(new UniversalPacket(batch.toArray(), ByteOrder.BIG_ENDIAN, address));
        });
        return packets.toArray(new UniversalPacket[packets.size()]);
    }


    private Request[] processBatch(UniversalPacket up) {
        List<Request> requests = new ArrayList<>();
        int len = up.bb().getInt();
        byte[] compressed = up.bb().get(len);
        try {
            byte[] uncompressed = CompressionUtils.zlibInflate(compressed);
            BinaryBuffer bb = BinaryBuffer.wrapBytes(uncompressed, up.bb().getOrder());
            while(bb.getPosition() < uncompressed.length) {
                byte id = bb.getByte();
                if(id == BATCH_PACKET) {
                    throw new IllegalStateException("BatchPacket found inside BatchPacket!");
                }
                int start = bb.getPosition();
                UniversalPacket pk = new UniversalPacket(uncompressed, up.bb().getOrder(), up.getAddress());
                pk.bb().setPosition(start - 1); //subtract by one so the handler can read the packet ID
                requests.add(handlePacket(pk)[0]);
                bb.setPosition(pk.bb().getPosition());
            }
        } catch (DataFormatException e) {
            getProtocol().getManager().getServer().getLogger().warning(e.getClass().getName()+" while handling BatchPacket: "+e.getMessage());
            getProtocol().getManager().getServer().getLogger().trace(e);
        } finally {
            if(!requests.isEmpty()) {
                return requests.toArray(new Request[requests.size()]);
            }
            return new Request[] {null};
        }
    }

    @Override
    public String getMCPEVersion() {
        return MCPE_VERSION;
    }

    @Override
    public int getProtocolVersion() {
        return MCPE_PROTOCOL;
    }
}
