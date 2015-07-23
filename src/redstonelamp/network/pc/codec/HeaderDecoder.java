package redstonelamp.network.pc.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.DemuxingProtocolDecoder;
import redstonelamp.network.pc.packet.MinecraftPacket;
import redstonelamp.utils.Binary;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * Decodes the minecraft packet header.
 */
public class HeaderDecoder extends DemuxingProtocolDecoder{

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        System.out.println(in.getHexDump());
        byte b = in.get();
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance();
        while(in.hasRemaining()) {
            if (((b & 0xff) >> 7) > 0) { //Check if there is more
                bb.putByte(b);
                b = in.get();
            } else { //no more
                bb.putByte(b);
                break;
            }
        }

        int len = Binary.getDefaultInstance().readVarInt(bb.toArray());
        System.out.println("length is: "+len);
        if(in.remaining() >= len){
            byte[] data = new byte[len];
            in.get(data);
            out.write(new MinecraftPacket(data));
            return true;
        } else {
            return false;
        }
    }
}
