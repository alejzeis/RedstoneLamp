package redstonelamp.network;

import redstonelamp.RedstoneLamp;

/**
 * Network constants class (packetID's, etc...)
 */
public class NetworkInfo {
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
    public static final byte BATCH_PACKET = (byte) 0xb1;
}
