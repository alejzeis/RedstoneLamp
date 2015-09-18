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
package net.redstonelamp.network.pe.sub.v34;

import net.redstonelamp.Player;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.pe.sub.PESubprotocolManager;
import net.redstonelamp.network.pe.sub.Subprotocol;
import net.redstonelamp.nio.BinaryBuffer;
import net.redstonelamp.request.ChatRequest;
import net.redstonelamp.request.LoginRequest;
import net.redstonelamp.request.Request;
import net.redstonelamp.response.*;
import net.redstonelamp.utils.CompressionUtils;

import java.net.SocketAddress;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.zip.DataFormatException;

/**
 * A subprotocol implementation for the MCPE version 0.12.1 (protocol 34)
 *
 * @author RedstoneLamp Team
 */
public class SubprotocolV34 extends Subprotocol implements ProtocolConst34{

    public SubprotocolV34(PESubprotocolManager manager){
        super(manager);
    }

    @Override
    public Request[] handlePacket(UniversalPacket up){
        List<Request> requests = new ArrayList<>();
        byte id = up.bb().getByte();
        if(id == BATCH_PACKET){
            return processBatch(up);
        }

        switch(id){
            case LOGIN_PACKET:
                getProtocol().getServer().getLogger().debug("Got Login packet!");
                String username = up.bb().getString();
                up.bb().skip(8); //Skip protocols
                long clientId = up.bb().getLong();
                UUID clientUUid = up.bb().getUUID();
                up.bb().getString(); //server addresss
                String clientSecret = up.bb().getString();
                boolean slim = up.bb().getBoolean();
                byte[] skin = up.bb().get(up.bb().getUnsignedShort());

                LoginRequest lr = new LoginRequest(username, clientUUid);
                lr.clientId = clientId;
                lr.slim = slim;
                lr.skin = skin;
                requests.add(lr);
                break;

            case TEXT_PACKET:
                ChatRequest cr = new ChatRequest((byte) 0);
                switch (up.bb().getByte()) {
                    case TEXT_POPUP:
                    case TEXT_CHAT:
                        cr.source = up.bb().getString();
                    case TEXT_RAW:
                    case TEXT_TIP:
                    case TEXT_SYSTEM:
                        cr.message = up.bb().getString();
                        break;

                    case TEXT_TRANSLATION:
                        cr.message = up.bb().getString();
                        int count = up.bb().getByte();
                        cr.parameters = new String[count];
                        for(int i = 0; i < count; i++) {
                            cr.parameters[i] = up.bb().getString();
                        }
                        break;
                }
                requests.add(cr);
                break;
        }
        return requests.toArray(new Request[requests.size()]);
    }

    @Override
    public UniversalPacket[] translateResponse(Response response, Player player){
        List<UniversalPacket> packets = new CopyOnWriteArrayList<>();
        SocketAddress address = player.getAddress();
        BinaryBuffer bb;
        if(response instanceof LoginResponse){
            LoginResponse lr = (LoginResponse) response;
            if(!lr.loginAllowed){
                String message;
                switch(lr.loginNotAllowedReason){
                    case LoginResponse.DEFAULT_loginNotAllowedReason:
                        message = "disconnectionScreen.noReason";
                        break;
                    case "redstonelamp.loginFailed.serverFull":
                        message = "disconnectionScreen.serverFull";
                        break;
                    default:
                        message = lr.loginNotAllowedReason;
                        break;
                }
                bb = BinaryBuffer.newInstance(3 + message.getBytes().length, ByteOrder.BIG_ENDIAN);
                bb.putByte(DISCONNECT_PACKET);
                bb.putString(message);
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
            }else{
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
                bb.putInt(player.getPosition().getLevel().getTime());
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
                bb.putInt(lr.health);
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

                bb = BinaryBuffer.newInstance(5, ByteOrder.BIG_ENDIAN);
                bb.putByte(SET_DIFFICULTY_PACKET);
                bb.putInt(1); //TODO: Correct difficulty
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

                //TODO: If creative, send items

                getProtocol().getChunkSender().registerChunkRequests(getProtocol().getServer().getPlayer(address), 96);
            }
        }else if(response instanceof ChunkResponse){
            ChunkResponse cr = (ChunkResponse) response;

            BinaryBuffer ordered = BinaryBuffer.newInstance(83204, ByteOrder.BIG_ENDIAN);
            ordered.put(cr.chunk.getBlockIds());
            ordered.put(cr.chunk.getBlockMeta());
            ordered.put(cr.chunk.getSkylight());
            ordered.put(cr.chunk.getBlocklight());
            ordered.put(cr.chunk.getHeightmap());
            ordered.put(cr.chunk.getBiomeColors());
            ordered.putInt(cr.chunk.getExtraData().length);
            ordered.put(cr.chunk.getExtraData());

            byte[] orderedData = ordered.toArray();

            bb = BinaryBuffer.newInstance(83218, ByteOrder.BIG_ENDIAN);
            bb.putByte(FULL_CHUNK_DATA_PACKET);
            bb.putInt(cr.chunk.getPosition().getX());
            bb.putInt(cr.chunk.getPosition().getZ());
            bb.putByte((byte) 0); //ORDER_COLUMNS
            bb.putInt(orderedData.length);
            bb.put(orderedData);

            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        }else if(response instanceof SpawnResponse){
            SpawnResponse sr = (SpawnResponse) response;

            int flags = 0;
            flags |= 0x20;
            if(player.getGamemode() == 1){
                flags |= 0x80; //allow flight
            }
            bb = BinaryBuffer.newInstance(5, ByteOrder.BIG_ENDIAN);
            bb.putByte(ADVENTURE_SETTINGS_PACKET);
            bb.putInt(flags);
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));


            //byte[] metadata = EntityMetadata.write(player.getMetadata());
            byte[] metadata = player.getMetadata().toBytes();
            bb = BinaryBuffer.newInstance(9 + metadata.length, ByteOrder.BIG_ENDIAN);
            bb.putByte(SET_ENTITY_DATA_PACKET);
            bb.putLong(0); //Player Entity ID is always zero to themselves
            bb.put(metadata);
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));


            bb = BinaryBuffer.newInstance(6, ByteOrder.BIG_ENDIAN);
            bb.putByte(SET_TIME_PACKET);
            bb.putInt(player.getPosition().getLevel().getTime());
            bb.putByte((byte) 1);
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

            bb = BinaryBuffer.newInstance(13, ByteOrder.BIG_ENDIAN);
            bb.putByte(RESPAWN_PACKET);
            bb.putFloat((float) sr.spawnPosition.getX());
            bb.putFloat((float) sr.spawnPosition.getY());
            bb.putFloat((float) sr.spawnPosition.getZ());
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

            bb = BinaryBuffer.newInstance(5, ByteOrder.BIG_ENDIAN);
            bb.putByte(PLAY_STATUS_PACKET);
            bb.putInt(3); //PLAY_SPAWN
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        }else if(response instanceof AnimateResponse){
            AnimateResponse ar = (AnimateResponse) response;
            bb = BinaryBuffer.newInstance(10, ByteOrder.LITTLE_ENDIAN);
            bb.putByte(ANIMATE_PACKET);
            switch(ar.actionType){
                case SWING_ARM:
                    bb.putByte((byte) 1);
                    break;
                case WAKE_UP:
                    bb.putByte((byte) 3);
                    break;
            }
            bb.putLong(player.getEntityID());
        }

        //Compress packets

        List<UniversalPacket> toBeCompressed = new CopyOnWriteArrayList<>();
        packets.stream().filter(packet -> packet.getBuffer().length >= 512 && packet.getBuffer()[0] != BATCH_PACKET).forEach(packet -> {
            toBeCompressed.add(packet);
            packets.remove(packet);
        });

        BinaryBuffer batch = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN); //Batch PAYLOAD
        for(UniversalPacket packet : toBeCompressed){
            batch.putInt(packet.getBuffer().length);
            batch.put(packet.getBuffer());
        }
        if(batch.toArray().length > 1){
            byte[] compressedPayload = CompressionUtils.zlibDeflate(batch.toArray(), 7);
            bb = BinaryBuffer.newInstance(compressedPayload.length + 5, ByteOrder.BIG_ENDIAN);
            bb.putByte(BATCH_PACKET);
            bb.putInt(compressedPayload.length);
            bb.put(compressedPayload);
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        }

        return packets.toArray(new UniversalPacket[packets.size()]);
    }

    @Override
    public UniversalPacket[] translateQueuedResponse(Response[] responses, Player player){
        return null;
    }


    private Request[] processBatch(UniversalPacket up){
        List<Request> requests = new ArrayList<>();
        int len = up.bb().getInt();
        byte[] compressed = up.bb().get(len);
        try{
            byte[] uncompressed = CompressionUtils.zlibInflate(compressed);
            BinaryBuffer bb = BinaryBuffer.wrapBytes(uncompressed, up.bb().getOrder());
            while(bb.getPosition() < uncompressed.length){
                int pkLen = bb.getInt();
                if(pkLen > bb.remaining()){
                    break;
                }

                byte[] data = bb.get(pkLen);
                BinaryBuffer pk = BinaryBuffer.wrapBytes(data, up.bb().getOrder());

                byte id = pk.getByte();
                if(id == BATCH_PACKET){
                    throw new IllegalStateException("BatchPacket found inside BatchPacket!");
                }
                UniversalPacket packet = new UniversalPacket(data, up.bb().getOrder(), up.getAddress());
                requests.addAll(Arrays.asList(handlePacket(packet)));
            }
        }catch(DataFormatException e){
            getProtocol().getManager().getServer().getLogger().warning(e.getClass().getName() + " while handling BatchPacket: " + e.getMessage());
            getProtocol().getManager().getServer().getLogger().trace(e);
        }finally{
            if(!requests.isEmpty()){
                return requests.toArray(new Request[requests.size()]);
            }
            return new Request[]{null};
        }
    }

    @Override
    public String getMCPEVersion(){
        return MCPE_VERSION;
    }

    @Override
    public int getProtocolVersion(){
        return MCPE_PROTOCOL;
    }
}
