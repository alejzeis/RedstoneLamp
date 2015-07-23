package redstonelamp.network;

import redstonelamp.RedstoneLamp;

/**
 * Network constants class (packetID's, etc...)
 */
public class PENetworkInfo {
    public static int COMPRESSION_LIMIT = 512;
    public static int COMPRESSION_LEVEL = 7;
    /**
     * The version of MCPE that RedstoneLamp implements.
     */
    public static final String MCPE_VERSION = RedstoneLamp.MC_VERSION;
    /**
     * The MCPE protocol version RedstoneLamp implements.
     */
    public static final int MCPE_PROTOCOL = 27;

    public static final byte LOGIN_PACKET = (byte) 0x82;
    public static final byte PLAY_STATUS_PACKET = (byte) 0x83;
    public static final byte DISCONNECT_PACKET = (byte) 0x84;
    public static final byte SET_TIME_PACKET = (byte) 0x86;
    public static final byte START_GAME_PACKET = (byte) 0x87;
    public static final byte SET_ENTITY_DATA_PACKET = (byte) 0x9e;
    public static final byte SET_ENTITY_MOTION_PACKET = (byte) 0x9f;
    public static final byte SET_HEALTH_PACKET = (byte) 0xa1;
    public static final byte SET_SPAWN_POSITION_PACKET = (byte) 0xa2;
    public static final byte CONTAINTER_SET_CONTENT_PACKET = (byte) 0xaa;
    public static final byte FULL_CHUNK_DATA_PACKET = (byte) 0xaf;
    public static final byte SET_DIFFICULTY_PACKET = (byte) 0xb0;
    public static final byte BATCH_PACKET = (byte) 0xb1;
}
