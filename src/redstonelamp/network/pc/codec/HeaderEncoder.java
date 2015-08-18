package redstonelamp.network.pc.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.DemuxingProtocolEncoder;
import redstonelamp.network.packet.DataPacket;
import redstonelamp.network.pc.PCInterface;
import redstonelamp.network.pc.packet.MinecraftPacket;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.utils.Binary;
import redstonelamp.utils.DynamicByteBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Encodes the Minecraft packet headers.
 */
public class HeaderEncoder extends DemuxingProtocolEncoder{
    private PCInterface pcInterface;

    public HeaderEncoder(PCInterface pcInterface){
        this.pcInterface = pcInterface;
    }

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        if(message instanceof PCDataPacket){
            byte[] buffer = ((PCDataPacket) message).encode();
            DynamicByteBuffer bb = DynamicByteBuffer.newInstance(ByteOrder.BIG_ENDIAN);
            bb.putVarInt(buffer.length);
            bb.put(buffer);
            IoBuffer buffer2 = IoBuffer.wrap(bb.toArray());
            System.out.println("Wrote: "+buffer2.getHexDump());
            out.write(buffer2);
        } else {
            throw new IllegalArgumentException("Expected PCDataPacket.");
        }
    }
}
