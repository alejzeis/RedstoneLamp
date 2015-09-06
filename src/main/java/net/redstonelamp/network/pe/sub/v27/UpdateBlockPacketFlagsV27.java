package net.redstonelamp.network.pe.sub.v27;

/**
 * Different flags for UpdateBlockPacket (protocol 27)
 * <br>
 * From https://github.com/PocketMine/PocketMine-MP/blob/master/src/pocketmine/network/protocol/UpdateBlockPacket.php
 *
 * @author RedstoneLamp Team & PocketMine Team
 */
public final class UpdateBlockPacketFlagsV27 {
    public static final byte FLAG_NONE      = 0b0000;
    public static final byte FLAG_NEIGHBORS = 0b0001;
    public static final byte FLAG_NETWORK   = 0b0010;
    public static final byte FLAG_NOGRAPHIC = 0b0100;
    public static final byte FLAG_PRIORITY  = 0b1000;
    public static final byte FLAG_ALL = (FLAG_NEIGHBORS | FLAG_NETWORK);
    public static final byte FLAG_ALL_PRIORITY = (FLAG_ALL | FLAG_PRIORITY);
}
