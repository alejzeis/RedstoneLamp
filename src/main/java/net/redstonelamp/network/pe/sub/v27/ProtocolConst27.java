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

/**
 * This "interface" contains the subprotocol public static final byteants for the MCPE protocol version 27
 * <br>
 * This file is based on the PocketMine-MP file at https://github.com/PocketMine/PocketMine-MP/blob/master/src/pocketmine/network/protocol/Info.php
 * PocketMine-MP is licensed under the LGPL v3. All credit to the PocketMine team
 * @author RedstoneLamp Team
 */
public interface ProtocolConst27 {
    /**
     * The Minecraft: Pocket Edition version this subprotocol implements.
     */
    public static final String MCPE_VERSION = "v0.11.1";

    /**
     * The Minecraft: Pocket Edition network protocol version this subprotocol implements.
     */
    public static final int MCPE_PROTOCOL = 27;

    public static final byte LOGIN_PACKET = (byte) 0x82;
    public static final byte PLAY_STATUS_PACKET = (byte) 0x83;
    public static final byte DISCONNECT_PACKET = (byte) 0x84;
    public static final byte TEXT_PACKET = (byte) 0x85;
    public static final byte SET_TIME_PACKET = (byte) 0x86;
    public static final byte START_GAME_PACKET = (byte) 0x87;
    public static final byte ADD_PLAYER_PACKET = (byte) 0x88;
    public static final byte REMOVE_PLAYER_PACKET = (byte) 0x89;
    public static final byte ADD_ENTITY_PACKET = (byte) 0x8a;
    public static final byte REMOVE_ENTITY_PACKET = (byte) 0x8b;
    public static final byte ADD_ITEM_ENTITY_PACKET = (byte) 0x8c;
    public static final byte TAKE_ITEM_ENTITY_PACKET = (byte) 0x8d;
    public static final byte MOVE_ENTITY_PACKET = (byte) 0x8e;
    public static final byte MOVE_PLAYER_PACKET = (byte) 0x8f;
    public static final byte REMOVE_BLOCK_PACKET = (byte) 0x90;
    public static final byte UPDATE_BLOCK_PACKET = (byte) 0x91;
    public static final byte ADD_PAINTING_PACKET = (byte) 0x92;
    public static final byte EXPLODE_PACKET = (byte) 0x93;
    public static final byte LEVEL_EVENT_PACKET = (byte) 0x94;
    public static final byte TILE_EVENT_PACKET = (byte) 0x95;
    public static final byte ENTITY_EVENT_PACKET = (byte) 0x96;
    public static final byte MOB_EFFECT_PACKET = (byte) 0x97;
    public static final byte PLAYER_EQUIPMENT_PACKET = (byte) 0x98;
    public static final byte PLAYER_ARMOR_EQUIPMENT_PACKET = (byte) 0x99;
    public static final byte INTERACT_PACKET = (byte) 0x9a;
    public static final byte USE_ITEM_PACKET = (byte) 0x9b;
    public static final byte PLAYER_ACTION_PACKET = (byte) 0x9c;
    public static final byte HURT_ARMOR_PACKET = (byte) 0x9d;
    public static final byte SET_ENTITY_DATA_PACKET = (byte) 0x9e;
    public static final byte SET_ENTITY_MOTION_PACKET = (byte) 0x9f;
    public static final byte SET_ENTITY_LINK_PACKET = (byte) 0xa0;
    public static final byte SET_HEALTH_PACKET = (byte) 0xa1;
    public static final byte SET_SPAWN_POSITION_PACKET = (byte) 0xa2;
    public static final byte ANIMATE_PACKET = (byte) 0xa3;
    public static final byte RESPAWN_PACKET = (byte) 0xa4;
    public static final byte DROP_ITEM_PACKET = (byte) 0xa5;
    public static final byte CONTAINER_OPEN_PACKET = (byte) 0xa6;
    public static final byte CONTAINER_CLOSE_PACKET = (byte) 0xa7;
    public static final byte CONTAINER_SET_SLOT_PACKET = (byte) 0xa8;
    public static final byte CONTAINER_SET_DATA_PACKET = (byte) 0xa9;
    public static final byte CONTAINER_SET_CONTENT_PACKET = (byte) 0xaa;
    //public static final byte CONTAINER_ACK_PACKET = (byte) 0xab;
    public static final byte ADVENTURE_SETTINGS_PACKET = (byte) 0xac;
    public static final byte TILE_ENTITY_DATA_PACKET = (byte) 0xad;
    //public static final byte PLAYER_INPUT_PACKET = (byte) 0xae;
    public static final byte FULL_CHUNK_DATA_PACKET = (byte) 0xaf;
    public static final byte SET_DIFFICULTY_PACKET = (byte) 0xb0;
    public static final byte BATCH_PACKET = (byte) 0xb1;
}
