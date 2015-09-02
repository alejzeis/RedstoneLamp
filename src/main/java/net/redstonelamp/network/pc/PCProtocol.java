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
import net.redstonelamp.level.Chunk;
import net.redstonelamp.level.generator.FlatGenerator;
import net.redstonelamp.network.NetworkManager;
import net.redstonelamp.network.Protocol;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.pc.codec.MinecraftBinaryUtils;
import net.redstonelamp.nio.BinaryBuffer;
import net.redstonelamp.request.LoginRequest;
import net.redstonelamp.request.Request;
import net.redstonelamp.request.SpawnRequest;
import net.redstonelamp.response.ChatResponse;
import net.redstonelamp.response.ChunkResponse;
import net.redstonelamp.response.LoginResponse;
import net.redstonelamp.response.Response;

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

    private PcChunkSender sender;

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
    protected void onClose(Player player){
        ((MinaInterface) _interface).close(player.getAddress());
    }

    @Override
    public Request[] handlePacket(UniversalPacket packet){
        List<Request> requests = new ArrayList<>();
        ProtocolState state = ((MinaInterface) _interface).getProtocolStateOfAddress(packet.getAddress());
        if(state == null){
            return new Request[0];
        }
        switch(state){
            case STATE_LOGIN:
                requests.addAll(Arrays.asList(handleLoginPacket(packet)));
                break;

            case STATE_PLAY:
                requests.addAll(Arrays.asList(handlePlayPacket(packet)));
        }

        return requests.toArray(new Request[requests.size()]);
    }

    private Request[] handleLoginPacket(UniversalPacket packet){
        List<Request> requests = new ArrayList<>();
        int id = packet.bb().getVarInt();
        switch(id){
            case LOGIN_LOGIN_START:
                //TODO: Implementing encryption: move login request to EncryptionResponse handle
                String name = packet.bb().getVarString();
                LoginRequest lr = new LoginRequest(name, UUID.nameUUIDFromBytes(name.getBytes()));
                lr.skin = new byte[64 * 64 * 4];
                requests.add(lr);
                break;
        }

        return requests.toArray(new Request[requests.size()]);
    }

    private Request[] handlePlayPacket(UniversalPacket packet){
        List<Request> requests = new ArrayList<>();
        int id = packet.bb().getVarInt();
        BinaryBuffer bb;
        switch(id){
            case PLAY_KEEP_ALIVE:
                int keepAliveId = packet.bb().getVarInt();
                bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
                bb.putVarInt(PLAY_KEEP_ALIVE);
                bb.putVarInt(keepAliveId);
                sendPacket(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, packet.getAddress()));
                break;

            case PLAY_SERVERBOUND_CHAT_MESSAGE:
                String message = packet.bb().getVarString();
                getServer().broadcastMessage("<" + getServer().getPlayer(packet.getAddress()).getNametag() + "> " + message);
                break;
        }
        return requests.toArray(new Request[requests.size()]);
    }

    @Override
    protected UniversalPacket[] _sendResponse(Response response, Player player){
        List<UniversalPacket> packets = new ArrayList<>();
        ProtocolState state = ((MinaInterface) _interface).getProtocolStateOfAddress(player.getAddress());
        if(state == null){
            return new UniversalPacket[0];
        }
        switch(state){
            case STATE_LOGIN:
                packets.addAll(Arrays.asList(translateLoginResponse(response, player)));
                break;

            case STATE_PLAY:
                packets.addAll(Arrays.asList(translatePlayResponse(response, player)));
                break;
        }

        return packets.toArray(new UniversalPacket[packets.size()]);
    }

    private UniversalPacket[] translateLoginResponse(Response response, Player player){
        List<UniversalPacket> packets = new ArrayList<>();
        if(response instanceof LoginResponse){
            LoginResponse lr = (LoginResponse) response;
            BinaryBuffer bb;
            if(!lr.loginAllowed){
                bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
                bb.putVarInt(LOGIN_DISCONNECT);
                String message;
                switch(lr.loginNotAllowedReason){
                    case "redstonelamp.loginFailed.serverFull":
                        message = "Server Full!";
                        break;
                    default:
                        message = lr.loginNotAllowedReason;
                }
                bb.putVarString("{\"text\":\"" + message + "\"}");
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, player.getAddress()));
            }else{
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

                packets.addAll(Arrays.asList(sendInitalLoginPackets(lr, player)));
            }
        }
        return packets.toArray(new UniversalPacket[packets.size()]);
    }

    private UniversalPacket[] translatePlayResponse(Response response, Player player){
        List<UniversalPacket> packets = new ArrayList<>();
        BinaryBuffer bb;
        if(response instanceof ChunkResponse){
            ChunkResponse cr = (ChunkResponse) response;
            bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
            bb.putVarInt(PLAY_CHUNK_DATA);
            bb.putInt(cr.chunk.getPosition().getX());
            bb.putInt(cr.chunk.getPosition().getZ());
            bb.putBoolean(true);
            bb.putShort((short) 0xFFFF);
            byte[] data = orderChunkData(cr.chunk);
            bb.putVarInt(data.length);
            bb.put(data);
        }else if(response instanceof ChatResponse){
            ChatResponse cr = (ChatResponse) response;
            bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
            bb.putVarInt(PLAY_CLIENTBOUND_CHAT_MESSAGE);
            bb.putVarString("{\"text\":\"" + cr.message + "\"}");
            bb.putByte((byte) 0);
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, player.getAddress()));
        }
        return packets.toArray(new UniversalPacket[packets.size()]);
    }

    private byte[] orderChunkData(Chunk chunk){
        //Calculate length of result array: ids & meta, skylight, biomes
        int length = 8*(8192+2048+2048)+256;
        //Lets create the byte array we want to return
        byte[] ret = new byte[length];
        int i = 0;
        //Set ids and metadata
        //Fill sections from the bottom up
        for(int section = 0; section < 8; section++){
        	for(int x = 0; x<16; x++){
        		for(int z = 0; z<16; z++){
        			for(int y = 0; y<16; y++){
        				ret[i++] = chunk.getBlockId(x, y+section*16, z);
        				ret[i++] = chunk.getBlockData(x, y+section*16, z);
        			}
        		}
        	}
        }
        //Set blocklight. Lower nibble - lower block, higher nibble - higher block
        for(int section = 0; section < 8; section++){
        	for(int x = 0; x<16; x++){
        		for(int z = 0; z<16; z++){
        			for(int y = 0; y<16; y+=2){
        				ret[i++] = (byte) (chunk.getBlocklight(x, y, z)<<4|chunk.getBlocklight(x, y+1, z));
        			}
        		}
        	}
        }
        //Set skylight. Lower nibble - lower block, higher nibble - higher block
        for(int section = 0; section < 8; section++){
        	for(int x = 0; x<16; x++){
        		for(int z = 0; z<16; z++){
        			for(int y = 0; y<16; y+=2){
        				ret[i++] = (byte) (chunk.getSkylight(x, y, z)<<4|chunk.getSkylight(x, y+1, z));
        			}
        		}
        	}
        }
        //Set biome ids
        for(int x = 0; x<15; x++){
        	 for(int z = 0; x<15; z++){
        		 ret[i++] = chunk.getBiomeId(x, z);
        	 }
        }
        return ret;
    }

    private UniversalPacket[] sendInitalLoginPackets(LoginResponse lr, Player player){
        List<UniversalPacket> packets = new ArrayList<>();

        BinaryBuffer bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
        bb.putVarInt(PLAY_JOIN_GAME);
        bb.putInt((int) lr.entityID);
        bb.putByte((byte) lr.gamemode);
        bb.putByte((byte) 0); //Dimension, TODO: Correct one
        bb.putByte((byte) 1); //Difficulty, TODO: correct one
        bb.putByte((byte) getServer().getMaxPlayers()); //Max Players, TODO: Limit if maxplayers over certain amount
        if(player.getPosition().getLevel().getGenerator() instanceof FlatGenerator){
            bb.putVarString("flat");
        }else{
            bb.putVarString("default");
        }
        bb.putBoolean(false); //Reduced debug info
        packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, player.getAddress()));

        bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
        bb.putVarInt(PLAY_SPAWN_POSITION);
        MinecraftBinaryUtils.writePosition(player.getPosition().getLevel().getSpawnPosition(), bb);
        packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, player.getAddress()));

        bb = BinaryBuffer.newInstance(10, ByteOrder.BIG_ENDIAN);
        bb.putVarInt(PLAY_CLIENTBOUND_PLAYER_ABILITIES);
        int flags = (player.getGamemode() == 1 ? 8 : 0) | (player.getGamemode() == 1 ? 4 : 0) | (player.getGamemode() == 1 ? 1 : 0);
        bb.putByte((byte) flags);
        bb.putFloat(1.0f);
        bb.putFloat(1.0f);
        packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, player.getAddress()));

        bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
        bb.putVarInt(PLAY_CLIENTBOUND_PLAYER_POSITION_LOOK);
        bb.putDouble(player.getPosition().getX());
        bb.putDouble(player.getPosition().getY());
        bb.putDouble(player.getPosition().getZ());
        bb.putFloat(player.getPosition().getYaw());
        bb.putFloat(player.getPosition().getPitch());
        bb.putBoolean(true); //TODO: onground
        packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, player.getAddress()));

        //sender.registerChunkRequests(player, 49);
        getServer().getTicker().addDelayedTask(tick -> {
            player.handleRequest(new SpawnRequest());
            player.sendMessage("\u00A74Sorry, Chunk Data got changed in the snapshots.");
            player.sendMessage("\u00A74And we have not implemented it yet.");
        }, 15);

        return packets.toArray(new UniversalPacket[packets.size()]);
    }
}
