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
import net.redstonelamp.block.Block;
import net.redstonelamp.block.Transparent;
import net.redstonelamp.item.Item;
import net.redstonelamp.level.Level;
import net.redstonelamp.level.position.BlockPosition;
import net.redstonelamp.level.position.Position;
import net.redstonelamp.math.Side;
import net.redstonelamp.math.Vector3;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.pe.sub.PESubprotocolManager;
import net.redstonelamp.network.pe.sub.Subprotocol;
import net.redstonelamp.network.pe.sub.v27.UpdateBlockPacketFlagsV27;
import net.redstonelamp.network.pe.sub.v27.UpdateBlockPacketRecordV27;
import net.redstonelamp.nio.BinaryBuffer;
import net.redstonelamp.request.*;
import net.redstonelamp.response.*;
import net.redstonelamp.utils.CompressionUtils;
import net.redstonelamp.utils.TextFormat;

import java.net.SocketAddress;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;
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

                LoginRequest lr = new LoginRequest(username, "minecraft.pocket-012", clientUUid);
                lr.clientId = clientId;
                lr.slim = slim;
                lr.skin = skin;
                requests.add(lr);
                break;

            case TEXT_PACKET:
                ChatRequest cr = new ChatRequest("");
                switch (up.bb().getByte()) {
                    case TEXT_POPUP:
                    case TEXT_CHAT:
                        cr.source = up.bb().getString();
                    case TEXT_RAW:
                    case TEXT_TIP:
                    case TEXT_SYSTEM:
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

            case ANIMATE_PACKET:
                byte actionId = up.bb().getByte();
                //entityID
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
                float fx = up.bb().getFloat();
                float fy = up.bb().getFloat();
                float fz = up.bb().getFloat();
                float px = up.bb().getFloat();
                float py = up.bb().getFloat();
                float pz = up.bb().getFloat();
                Item item = up.bb().getSlot();
                if(face >= 0 && face <= 5){ //Use item on, Block Place
                    //TODO: Implement Item use, (pickaxe, sword, etc)
                    Block block = (Block) Block.get(item.getId(), item.getMeta(), 1);
                    Vector3 target = new Vector3(ax, ay, az);
                    //System.out.print("Attempting to place: "+target+" block is: "+getProtocol().getServer().getLevelManager().getMainLevel().getBlock(BlockPosition.fromVector3(target, getProtocol().getServer().getLevelManager().getMainLevel())).getId());
                    //System.out.print(" Face: "+face+"\n");
                    Level l = getProtocol().getServer().getPlayer(up.getAddress()).getPosition().getLevel();
                    if(l.getBlock(BlockPosition.fromVector3(target, l)) instanceof Transparent) {
                        requests.add(new BlockPlaceRequest(block, target));
                    } else {
                        requests.add(new BlockPlaceRequest(block, target.getSide(face, 1)));
                    }
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

            case MOB_EQUIPMENT_PACKET:
                up.bb().skip(8); //entity ID
                requests.add(new SetHeldItemRequest(up.bb().getSlot(), up.bb().getByte(), up.bb().getByte()));
                break;

            case PLAYER_ACTION_PACKET:
                up.bb().skip(8); //entity ID
                int action = up.bb().getInt();
                switch (action) {
                    case PlayerActionsV34.ACTION_START_SPRINT:
                        requests.add(new SprintRequest(true));
                        break;

                    case PlayerActionsV34.ACTION_STOP_SPRINT:
                        requests.add(new SprintRequest(false));
                        break;
                }
                break;
        }
        return requests.toArray(new Request[requests.size()]);
    }

    @Override
    public UniversalPacket[] translateResponse(Response response, Player player){
        List<UniversalPacket> packets = new CopyOnWriteArrayList<>();
        SocketAddress address = player.getAddress();
        BinaryBuffer bb;
        if(response instanceof LoginResponse) {
            LoginResponse lr = (LoginResponse) response;
            if (!lr.loginAllowed) {
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
                        break;
                }
                bb = BinaryBuffer.newInstance(3 + message.getBytes().length, ByteOrder.BIG_ENDIAN);
                bb.putByte(DISCONNECT_PACKET);
                bb.putString(message);
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
            } else {
                bb = BinaryBuffer.newInstance(5, ByteOrder.BIG_ENDIAN);
                bb.putByte(PLAY_STATUS_PACKET);
                bb.putInt(0); //LOGIN_SUCCESS
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

                bb = BinaryBuffer.newInstance(50, ByteOrder.BIG_ENDIAN);
                bb.putByte(START_GAME_PACKET);
                bb.putInt(-1); //seed
                bb.putByte((byte) 0); //Dimension, 0: overworld, 1: nether
                bb.putInt(lr.generator);
                bb.putInt(lr.gamemode);
                bb.putLong(0); //Use zero for actual player
                bb.putInt(lr.spawnX);
                bb.putInt(lr.spawnY);
                bb.putInt(lr.spawnZ);
                bb.putFloat(lr.x);
                bb.putFloat(lr.y);
                bb.putFloat(lr.z);
                bb.putByte((byte) 0);
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

                bb = BinaryBuffer.newInstance(6, ByteOrder.BIG_ENDIAN);
                bb.putByte(SET_TIME_PACKET);
                bb.putInt(player.getPosition().getLevel().getTime());
                bb.putBoolean(true);
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

                bb = BinaryBuffer.newInstance(13, ByteOrder.BIG_ENDIAN);
                bb.putByte(SET_SPAWN_POSITION_PACKET);
                bb.putInt(lr.spawnX);
                bb.putInt(lr.spawnY);
                bb.putInt(lr.spawnZ);
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

                bb = BinaryBuffer.newInstance(5, ByteOrder.BIG_ENDIAN);
                bb.putByte(SET_HEALTH_PACKET);
                bb.putInt(lr.health);
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

                bb = BinaryBuffer.newInstance(5, ByteOrder.BIG_ENDIAN);
                bb.putByte(SET_DIFFICULTY_PACKET);
                bb.putInt(1); //TODO: Correct difficulty
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

                if(lr.gamemode == 1) {
                    bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
                    bb.putByte(CONTAINER_SET_CONTENT_PACKET);
                    bb.putByte((byte) 0x79); //SPECIAL_CREATIVE
                    bb.putShort((short) Item.getCreativeItems().size());
                    Item.getCreativeItems().forEach(bb::putSlot);
                    bb.putShort((short) 0);
                    packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
                }

                getProtocol().getChunkSender().registerChunkRequests(getProtocol().getServer().getPlayer(address), 96);
            }
        } else if(response instanceof DisconnectResponse) {
            DisconnectResponse dr = (DisconnectResponse) response;
            if(dr.notifyClient) {
                bb = BinaryBuffer.newInstance(3 + dr.reason.getBytes().length, ByteOrder.BIG_ENDIAN);
                bb.putByte(DISCONNECT_PACKET);
                if(dr.reason.startsWith("!")) {
                    bb.putString(translateTranslationToPE(new ChatResponse.ChatTranslation(dr.reason.replaceAll(Pattern.quote("!"), ""), new String[0])).message);
                } else {
                    bb.putString(dr.reason);
                }
                packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
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
            //TODO: Implement extra data
            ordered.setOrder(ByteOrder.LITTLE_ENDIAN);
            ordered.putInt(0);

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
            bb.putBoolean(true);
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
            bb = BinaryBuffer.newInstance(10, ByteOrder.BIG_ENDIAN);
            bb.putByte(ANIMATE_PACKET);
            switch(ar.actionType){
                case SWING_ARM:
                    bb.putByte((byte) 1);
                    break;
                case WAKE_UP:
                    bb.putByte((byte) 3);
                    break;
            }
            bb.putLong(ar.entityID);
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        } else if(response instanceof ChatResponse) {
            ChatResponse cr = (ChatResponse) response;
            bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
            bb.putByte(TEXT_PACKET);
            if(cr.translation != null) {
                ChatResponse.ChatTranslation translation = translateTranslationToPE(cr.translation);
                bb.putByte(TEXT_TRANSLATION);
                bb.putString(translation.message);
                bb.putByte((byte) translation.params.length);
                for(String param : translation.params) {
                    bb.putString(param);
                }
            } else if(cr.source != null){
                bb.putByte(TEXT_CHAT);
                bb.putString(cr.source);
                bb.putString(cr.message);
            } else {
                bb.putByte(TEXT_RAW);
                bb.putString(cr.message);
            }
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        } else if(response instanceof PopupResponse) {
            PopupResponse pr = (PopupResponse) response;
            bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
            bb.putByte(TEXT_PACKET);
            bb.putByte(TEXT_POPUP);
            bb.putString(pr.message);
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        } else if(response instanceof AddPlayerResponse) {
            Player p = ((AddPlayerResponse) response).player;
            bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
            bb.putByte(ADD_PLAYER_PACKET);
            bb.putUUID(p.getUuid());
            bb.putString(p.getName()); //TODO: getUsername()
            bb.putLong(p.getEntityID());
            bb.putFloat(p.getPosition().getX());
            bb.putFloat(p.getPosition().getY());
            bb.putFloat(p.getPosition().getZ());
            bb.putFloat(0f); //Speed x
            bb.putFloat(0f); //Speed y TODO: work on these
            bb.putFloat(0f); //Speed z
            bb.putFloat(p.getPosition().getYaw());
            bb.putFloat(p.getPosition().getYaw()); //TODO: head yaw/rot
            bb.putFloat(p.getPosition().getPitch());
            bb.putSlot(p.getInventory().getItemInHand());
            bb.put(p.getMetadata().toBytes());

            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

            bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
            bb.putByte(PLAYER_LIST_PACKET);
            bb.putByte((byte) 0);
            bb.putInt(1);
            bb.putUUID(p.getUuid());
            bb.putLong(p.getEntityID());
            bb.putString(p.getName());
            bb.putBoolean(p.isSlim());
            bb.putShort((short) p.getSkin().length);
            bb.put(p.getSkin());
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

        } else if(response instanceof RemovePlayerResponse) {
            Player p = ((RemovePlayerResponse) response).player;
            bb = BinaryBuffer.newInstance(25, ByteOrder.BIG_ENDIAN);
            bb.putByte(REMOVE_PLAYER_PACKET);
            bb.putLong(p.getEntityID());
            bb.putUUID(p.getUuid());

            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));

            bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
            bb.putByte(PLAYER_LIST_PACKET);
            bb.putByte((byte) 1);
            bb.putInt(1);
            bb.putUUID(p.getUuid());
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        } else if(response instanceof PlayerMoveResponse) {
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
        } else if(response instanceof BlockPlaceResponse) {
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
        } else if(response instanceof RemoveBlockResponse) {
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
        } else if(response instanceof SetHeldItemResponse) {
            SetHeldItemResponse shir = (SetHeldItemResponse) response;
            bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
            bb.putByte(MOB_EQUIPMENT_PACKET);
            bb.putLong(shir.entityID);
            bb.putSlot(shir.item);
            bb.putByte((byte) shir.inventorySlot);
            bb.putByte((byte) shir.hotbarSlot);
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
        } else if(response instanceof SprintResponse) {
            SprintResponse sr = (SprintResponse) response;
            bb = BinaryBuffer.newInstance(29, ByteOrder.BIG_ENDIAN);
            bb.putByte(PLAYER_ACTION_PACKET);
            bb.putLong(sr.player.getEntityID());
            bb.putInt(sr.starting ? PlayerActionsV34.ACTION_START_SPRINT : PlayerActionsV34.ACTION_STOP_SPRINT);
            bb.putInt(Math.round(sr.player.getPosition().getX()));
            bb.putInt(Math.round(sr.player.getPosition().getY()));
            bb.putInt(Math.round(sr.player.getPosition().getZ()));
            bb.putInt(Side.UP);
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, address));
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

    private ChatResponse.ChatTranslation translateTranslationToPE(ChatResponse.ChatTranslation translation) {
        return getManager().getProtocol().getServer().getTranslationManager().translate(getProtocol(), translation);
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
        }else if(responses[0] instanceof RemoveBlockResponse) {
            List<UpdateBlockPacketRecordV27> records = new ArrayList<>();
            for (Response r : responses) {
                RemoveBlockResponse rbr = (RemoveBlockResponse) r;
                records.add(new UpdateBlockPacketRecordV27(rbr.position.getX(), rbr.position.getY(), rbr.position.getZ(), (byte) 0, (byte) 0, UpdateBlockPacketFlagsV27.FLAG_ALL_PRIORITY));
            }
            bb = BinaryBuffer.newInstance(5 + (11 * records.size()), ByteOrder.BIG_ENDIAN);
            bb.putByte(UPDATE_BLOCK_PACKET);
            bb.putInt(records.size());
            for (UpdateBlockPacketRecordV27 record : records) {
                bb.putInt(record.x);
                bb.putInt(record.z);
                bb.putByte((byte) record.y);
                bb.putByte(record.id);
                bb.putByte((byte) ((record.flags << 4) | (byte) record.meta));
            }
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, player.getAddress()));
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
            packets.add(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, player.getAddress()));
        }
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