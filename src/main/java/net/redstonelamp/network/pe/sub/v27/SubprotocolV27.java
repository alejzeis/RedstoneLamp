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
package net.redstonelamp.network.pe.sub.v27;

import net.redstonelamp.Player;
import net.redstonelamp.block.Block;
import net.redstonelamp.item.Item;
import net.redstonelamp.level.Level;
import net.redstonelamp.level.position.BlockPosition;
import net.redstonelamp.level.position.Position;
import net.redstonelamp.math.Vector3;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.pe.sub.PESubprotocolManager;
import net.redstonelamp.network.pe.sub.Subprotocol;
import net.redstonelamp.nio.BinaryBuffer;
import net.redstonelamp.request.*;
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
 * A subprotocol implementation for the MCPE version 0.11.1 (protocol 27)
 *
 * @author RedstoneLamp Team
 */
public class SubprotocolV27 extends Subprotocol implements ProtocolConst27{

    public SubprotocolV27(PESubprotocolManager manager){
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
                LoginRequest lr = new LoginRequest(username, "minecraft.pocket-011", UUID.nameUUIDFromBytes(username.getBytes()));
                up.bb().skip(8); //Skip protocol1, protocol 2 (int, int)
                lr.clientId = up.bb().getInt();
                lr.slim = up.bb().getByte() > 0;
                //lr.skin = up.bb().get(up.bb().getUnsignedShort()); //Skin written as a String, but the String class seems to corrupt the Skin
                up.bb().skip(2); //Skip skin "string" length
                lr.skin = up.bb().get(up.bb().remaining());

                requests.add(lr);
                break;
            case TEXT_PACKET:
                ChatRequest cr = new ChatRequest("");
                switch(up.bb().getByte()){
                    case TEXT_CHAT:
                        cr.source = up.bb().getString();
                    case TEXT_RAW:
                    case TEXT_POPUP:
                    case TEXT_TIP:
                        cr.message = up.bb().getString();
                        break;
                }
                requests.add(cr);
                break;
            case MOVE_PLAYER_PACKET:
                Position position = new Position(getProtocol().getServer().getPlayer(up.getAddress()).getPosition().getLevel());
                up.bb().skip(8); //Skip entity ID
                float x = up.bb().getFloat();
                float y = up.bb().getFloat();
                float z = up.bb().getFloat();
                float yaw = up.bb().getFloat();
                up.bb().skip(4); //Skip bodyYaw
                float pitch = up.bb().getFloat();
                up.bb().skip(1); //Skip mode
                boolean onGround = up.bb().getByte() > 0;
                position.setX(x);
                position.setY(y);
                position.setZ(z);
                position.setYaw(yaw);
                position.setPitch(pitch);

                PlayerMoveRequest pmr = new PlayerMoveRequest(position, onGround);
                requests.add(pmr);
                break;
            case PLAYER_EQUIPMENT_PACKET:
                up.bb().skip(8); //Skip entity ID
                short item = up.bb().getShort();
                short meta = up.bb().getShort();
                byte slot = up.bb().getByte();
                byte selectedSlot = up.bb().getByte();
                requests.add(new PlayerEquipmentRequest(Item.get(item, meta, 1)));
                break;
            case ANIMATE_PACKET:
                byte actionId = up.bb().getByte();
                up.bb().skip(8); //Skip entity ID
                switch(actionId){
                    case 1:
                        requests.add(new AnimateRequest(AnimateRequest.ActionType.SWING_ARM));
                        break;
                }
                break;
            case USE_ITEM_PACKET:
                int ax = up.bb().getInt();
                int ay = up.bb().getInt();
                int az = up.bb().getInt();
                byte face = up.bb().getByte();
                short item2 = up.bb().getShort();
                short meta2 = up.bb().getShort();
                up.bb().skip(8); //Skip entity ID
                float fx = up.bb().getFloat();
                float fy = up.bb().getFloat();
                float fz = up.bb().getFloat();
                float px = up.bb().getFloat();
                float py = up.bb().getFloat();
                float pz = up.bb().getFloat();

                if(face >= 0 && face <= 5){ //Use item on, Block Place
                    //TODO: Implement Item use, (pickaxe, sword, etc)
                    //Block block = new Block(item2, meta2, 1);
                    Block block = (Block) Block.get(item2, meta2, 1);
                    requests.add(new BlockPlaceRequest(block, new Vector3(ax, ay, az).getSide(face, 1)));
                }
                break;
            case REMOVE_BLOCK_PACKET:
                up.bb().skip(8); //Skip entityID
                int blockX = up.bb().getInt();
                int blockZ = up.bb().getInt();
                int blockY = up.bb().getByte();
                Level level = getProtocol().getServer().getPlayer(up.getAddress()).getPosition().getLevel();
                requests.add(new RemoveBlockRequest(new BlockPosition(blockX, blockY, blockZ, level)));
                break;
            default:
                System.out.println(String.format("Unknown: 0x%02x", id));
        }
        return requests.toArray(new Request[requests.size()]);
    }

    @Override
    public UniversalPacket[] translateResponse(Response response, Player player){
        SocketAddress address = player.getAddress();
        List<UniversalPacket> packets = new CopyOnWriteArrayList<>();
        BinaryBuffer bb;
        if(response instanceof LoginResponse){
            LoginResponse lr = (LoginResponse) response;
            if(lr.loginAllowed){
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
            }else{
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
                }

                bb = BinaryBuffer.newInstance(3 + message.getBytes().length, ByteOrder.BIG_ENDIAN);
                bb.putByte(DISCONNECT_PACKET);
                bb.putString(message);

                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
            }
        }else if(response instanceof DisconnectResponse){
            DisconnectResponse dr = (DisconnectResponse) response;
            if(dr.notifyClient){
                bb = BinaryBuffer.newInstance(3 + dr.reason.getBytes().length, ByteOrder.BIG_ENDIAN);
                bb.putByte(DISCONNECT_PACKET);
                bb.putString(dr.reason);

                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
            }
        }else if(response instanceof ChunkResponse){
            ChunkResponse cr = (ChunkResponse) response;

            BinaryBuffer ordered = BinaryBuffer.newInstance(83200, ByteOrder.BIG_ENDIAN);
            ordered.put(cr.chunk.getBlockIds());
            ordered.put(cr.chunk.getBlockMeta());
            ordered.put(cr.chunk.getSkylight());
            ordered.put(cr.chunk.getBlocklight());
            ordered.put(cr.chunk.getHeightmap());
            ordered.put(cr.chunk.getBiomeColors());

            byte[] orderedData = ordered.toArray();

            bb = BinaryBuffer.newInstance(83213, ByteOrder.BIG_ENDIAN);
            bb.putByte(FULL_CHUNK_DATA_PACKET);
            bb.putInt(cr.chunk.getPosition().getX());
            bb.putInt(cr.chunk.getPosition().getZ());
            bb.putInt(orderedData.length);
            bb.put(orderedData);

            packets.add(new UniversalPacket(Arrays.copyOf(bb.toArray(), bb.getPosition()), ByteOrder.BIG_ENDIAN, address));
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
        }else if(response instanceof TeleportResponse){
            TeleportResponse tr = (TeleportResponse) response;
            bb = BinaryBuffer.newInstance(35, ByteOrder.BIG_ENDIAN);
            bb.putByte(MOVE_PLAYER_PACKET);
            bb.putLong(player.getEntityID());
            bb.putFloat((float) tr.pos.getX());
            bb.putFloat((float) tr.pos.getY());
            bb.putFloat((float) tr.pos.getZ());
            bb.putFloat(tr.pos.getYaw());
            bb.putFloat(tr.bodyYaw);
            bb.putFloat(tr.pos.getPitch());
            bb.putByte((byte) 0); //MODE_NORMAL
            bb.putByte((byte) (tr.onGround ? 1 : 0));
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        }else if(response instanceof ChatResponse){
            ChatResponse cr = (ChatResponse) response;
            bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN); //Self-expand
            bb.putByte(TEXT_PACKET);
            if(cr.translation != ChatResponse.DEFAULT_translation){
                bb.putByte(TEXT_TRANSLATION); //TYPE_TRANSLATION
                bb.putString(cr.translation.message);
                bb.putByte((byte) cr.translation.params.length);
                for(String param : cr.translation.params){
                    bb.putString(param);
                }
            }else if(!cr.source.isEmpty()){
                bb.putByte(TEXT_CHAT); //TYPE_CHAT
                bb.putString(cr.source);
                bb.putString(cr.message);
            }else{
                bb.putByte(TEXT_RAW); //TYPE_RAW
                bb.putString(cr.message);
            }
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        }else if(response instanceof AddPlayerResponse){
            Player p = ((AddPlayerResponse) response).player;
            byte[] meta = p.getMetadata().toBytes();
            //bb = BinaryBuffer.newInstance(56 + p.getSkin().length + p.getNametag().getBytes().length + meta.length, ByteOrder.BIG_ENDIAN);
            bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
            bb.putByte(ADD_PLAYER_PACKET);
            bb.putLong(p.getEntityID()); //Prevent client from knowing the real clientID
            bb.putString(p.getName());
            bb.putLong(p.getEntityID());
            bb.putFloat((float) p.getPosition().getX());
            bb.putFloat((float) p.getPosition().getY());
            bb.putFloat((float) p.getPosition().getZ());
            bb.putFloat(0f); //Speed X
            bb.putFloat(0f); //Speed y
            bb.putFloat(0f); //speed z
            bb.putFloat(p.getPosition().getYaw());
            bb.putFloat(p.getPosition().getYaw()); //TODO: head yaw
            bb.putFloat(p.getPosition().getPitch());
            bb.putShort((short) 1); // item
            bb.putShort((short) 0); // meta item
            bb.putByte((byte) (p.isSlim() ? 1 : 0));
            bb.putShort((short) p.getSkin().length);
            bb.put(p.getSkin());
            bb.put(meta);
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        }else if(response instanceof PopupResponse){
            PopupResponse pr = (PopupResponse) response;
            bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
            bb.putByte(TEXT_PACKET);
            bb.putByte(TEXT_POPUP); // TYPE_POPUP
            bb.putString(pr.message);
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        }else if(response instanceof RemovePlayerResponse){
            RemovePlayerResponse rpp = (RemovePlayerResponse) response;
            bb = BinaryBuffer.newInstance(9, ByteOrder.BIG_ENDIAN);
            bb.putByte(REMOVE_PLAYER_PACKET);
            bb.putLong(rpp.player.getEntityID());
            bb.putLong(rpp.player.getEntityID());
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        }else if(response instanceof PlayerMoveResponse){
            PlayerMoveResponse pmr = (PlayerMoveResponse) response;
            bb = BinaryBuffer.newInstance(35, ByteOrder.BIG_ENDIAN);
            bb.putByte(MOVE_PLAYER_PACKET);
            bb.putLong(pmr.entityID);
            bb.putFloat((float) pmr.pos.getX());
            bb.putFloat((float) pmr.pos.getY());
            bb.putFloat((float) pmr.pos.getZ());
            bb.putFloat(pmr.pos.getYaw());
            bb.putFloat(pmr.bodyYaw);
            bb.putFloat(pmr.pos.getPitch());
            bb.putByte((byte) 0); //MODE_NORMAL
            bb.putByte((byte) (pmr.onGround ? 1 : 0));
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        }else if(response instanceof PlayerEquipmentResponse){
            PlayerEquipmentResponse er = (PlayerEquipmentResponse) response;
            bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
            bb.putByte(PLAYER_EQUIPMENT_PACKET);
            bb.putLong(player.getEntityID());
            bb.putShort((short) er.item.getId());
            bb.putShort(er.item.getMeta());
            bb.putByte((byte) 0); //slot
            bb.putByte((byte) 0); //selectedSlot
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        }else if(response instanceof AnimateResponse){
            AnimateResponse ar = (AnimateResponse) response;
            bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
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
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        }else if(response instanceof BlockPlaceResponse){
            BlockPlaceResponse bpr = (BlockPlaceResponse) response;
            bb = BinaryBuffer.newInstance(16, ByteOrder.BIG_ENDIAN);
            bb.putByte(UPDATE_BLOCK_PACKET);
            bb.putInt(1);
            bb.putInt(bpr.position.getX());
            bb.putInt(bpr.position.getZ());
            bb.putByte((byte) bpr.position.getY());
            bb.putByte((byte) bpr.block.getId());
            bb.putByte((byte) ((UpdateBlockPacketFlagsV27.FLAG_ALL_PRIORITY << 4) | (byte) bpr.block.getMeta()));
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        }else if(response instanceof RemoveBlockResponse){
            RemoveBlockResponse rbr = (RemoveBlockResponse) response;
            bb = BinaryBuffer.newInstance(16, ByteOrder.BIG_ENDIAN);
            bb.putByte(UPDATE_BLOCK_PACKET);
            bb.putInt(1);
            bb.putInt(rbr.position.getX());
            bb.putInt(rbr.position.getZ());
            bb.putByte((byte) rbr.position.getY());
            bb.putByte((byte) 0); //AIR
            bb.putByte((byte) ((UpdateBlockPacketFlagsV27.FLAG_ALL_PRIORITY << 4) | (byte) 0));
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
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

    @Override
    public UniversalPacket[] translateQueuedResponse(Response[] responses, Player player){
        List<UniversalPacket> packets = new CopyOnWriteArrayList<>();
        BinaryBuffer bb;
        if(responses[0] instanceof BlockPlaceResponse){
            List<UpdateBlockPacketRecordV27> records = new ArrayList<>();
            for(Response r : responses){
                BlockPlaceResponse bpr = (BlockPlaceResponse) r;
                records.add(new UpdateBlockPacketRecordV27(bpr.position.getX(), bpr.position.getY(), bpr.position.getZ(), (byte) bpr.block.getId(), (byte) bpr.block.getMeta(), UpdateBlockPacketFlagsV27.FLAG_ALL_PRIORITY));
            }
            bb = BinaryBuffer.newInstance(5 + (11 * records.size()), ByteOrder.BIG_ENDIAN);
            bb.putByte(UPDATE_BLOCK_PACKET);
            bb.putInt(records.size());
            for(UpdateBlockPacketRecordV27 record : records){
                bb.putInt(record.x);
                bb.putInt(record.z);
                bb.putByte((byte) record.y);
                bb.putByte(record.id);
                bb.putByte((byte) ((record.flags << 4) | (byte) record.meta));
            }
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, player.getAddress()));
        }else if(responses[0] instanceof RemoveBlockResponse){
            List<UpdateBlockPacketRecordV27> records = new ArrayList<>();
            for(Response r : responses){
                RemoveBlockResponse rbr = (RemoveBlockResponse) r;
                records.add(new UpdateBlockPacketRecordV27(rbr.position.getX(), rbr.position.getY(), rbr.position.getZ(), (byte) 0, (byte) 0, UpdateBlockPacketFlagsV27.FLAG_ALL_PRIORITY));
            }
            bb = BinaryBuffer.newInstance(5 + (11 * records.size()), ByteOrder.BIG_ENDIAN);
            bb.putByte(UPDATE_BLOCK_PACKET);
            bb.putInt(records.size());
            for(UpdateBlockPacketRecordV27 record : records){
                bb.putInt(record.x);
                bb.putInt(record.z);
                bb.putByte((byte) record.y);
                bb.putByte(record.id);
                bb.putByte((byte) ((record.flags << 4) | (byte) record.meta));
            }
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, player.getAddress()));
        }

        //Compress packets
        packets.stream().filter(packet -> packet.getBuffer().length >= 512 && packet.getBuffer()[0] != BATCH_PACKET).forEach(packet -> { //Compress packets
            byte[] compressed = CompressionUtils.zlibDeflate(packet.getBuffer(), 7);
            BinaryBuffer batch = BinaryBuffer.newInstance(5 + compressed.length, ByteOrder.BIG_ENDIAN);
            batch.putByte(BATCH_PACKET);
            batch.putInt(compressed.length);
            batch.put(compressed);

            packets.remove(packet);
            packets.add(new UniversalPacket(batch.toArray(), ByteOrder.BIG_ENDIAN, player.getAddress()));
        });
        return packets.toArray(new UniversalPacket[packets.size()]);
    }

    private Request[] processBatch(UniversalPacket up){
        List<Request> requests = new ArrayList<>();
        int len = up.bb().getInt();
        byte[] compressed = up.bb().get(len);
        try{
            byte[] uncompressed = CompressionUtils.zlibInflate(compressed);
            BinaryBuffer bb = BinaryBuffer.wrapBytes(uncompressed, up.bb().getOrder());
            while(bb.getPosition() < uncompressed.length){
                byte id = bb.getByte();
                if(id == BATCH_PACKET){
                    throw new IllegalStateException("BatchPacket found inside BatchPacket!");
                }
                int start = bb.getPosition();
                UniversalPacket pk = new UniversalPacket(uncompressed, up.bb().getOrder(), up.getAddress());
                pk.bb().setPosition(start - 1); //subtract by one so the handler can read the packet ID
                requests.add(handlePacket(pk)[0]);
                bb.setPosition(pk.bb().getPosition());
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