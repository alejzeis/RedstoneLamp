package redstonelamp.network.pc.packet;


import redstonelamp.utils.Binary;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Abstract Minecraft packet.
 */
public class MinecraftPacket {
    public final int packetID;
    public final byte[] payload;

    public MinecraftPacket(byte pid, byte[] payload){
        packetID = pid;
        this.payload = payload;
    }

    public MinecraftPacket(byte[] fullData) throws IOException {
        ByteBuffer bb = ByteBuffer.wrap(fullData);
        packetID = Binary.getDefaultInstance().readVarInt(bb);
        payload = new byte[bb.remaining()];
        bb.get(payload);
    }
}
