package net.redstonelamp.network.pc.codec;

import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.pc.MinaInterface;
import net.redstonelamp.nio.BinaryBuffer;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.DemuxingProtocolDecoder;

import java.io.IOException;
import java.nio.ByteOrder;

/**
 * A DemuxingProtocolDecoder implementation that decodes Minecraft packet headers.
 *
 * @author RedstoneLamp Team
 */
public class MinecraftPacketHeaderDecoder extends DemuxingProtocolDecoder{
    private final MinaInterface mina;

    public MinecraftPacketHeaderDecoder(MinaInterface mina) {
        super();
        this.mina = mina;
    }

    @Override
    public boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        int len = readVarInt(in);
        if(len == -1) {
            return false; //Wait for more data to be read before trying again
        }

        if(in.remaining() >= len) {
            byte[] data = new byte[len];
            in.get(data);
            out.write(new UniversalPacket(data, ByteOrder.BIG_ENDIAN, session.getRemoteAddress()));
            return true;
        } else {
            return false;
        }
    }

    private int readVarInt(IoBuffer in) throws IOException {
        byte b = in.get();
        BinaryBuffer bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
        boolean readCorrect = false;
        while(in.hasRemaining()) {
            if (((b & 0xff) >> 7) > 0) { //Check if there is more
                bb.putByte(b);
                b = in.get();
            } else { //no more
                bb.putByte(b);
                readCorrect = true;
                break;
            }
        }
        if(!readCorrect) {
            return -1;
        }
        return BinaryBuffer.wrapBytes(bb.toArray(), ByteOrder.BIG_ENDIAN).getVarInt();
    }
}
