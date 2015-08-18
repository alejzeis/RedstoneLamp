package redstonelamp.network.pc.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.DemuxingProtocolDecoder;
import redstonelamp.DesktopPlayer;
import redstonelamp.Player;
import redstonelamp.network.pc.PCInterface;
import redstonelamp.network.pc.packet.MinecraftPacket;
import redstonelamp.utils.Binary;
import redstonelamp.utils.CompressionUtils;
import redstonelamp.utils.DynamicByteBuffer;

import java.io.IOException;

/**
 * Decodes the minecraft packet header.
 */
public class HeaderDecoder extends DemuxingProtocolDecoder{
    private PCInterface pcInterface;

    public HeaderDecoder(PCInterface pcInterface){
        this.pcInterface = pcInterface;
    }

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        System.out.println(in.getHexDump());

        boolean compression = false;

        Player player = pcInterface.getServer().getPlayer(session.getRemoteAddress().toString());
        if(player != null){
            DesktopPlayer dp = (DesktopPlayer) player;
            compression = dp.isCompressionActivated();
        }

        int len = readVarInt(in);
        System.out.println("length is: "+len);

        if(compression) {
            int oldPos = in.position();
            int uncompressedLen = readVarInt(in);
            in.position(oldPos);

            if(in.remaining() >= len){
                byte[] data = new byte[len];
                in.get(data);
                data = CompressionUtils.zlibInflate(data);
                out.write(new MinecraftPacket(data));
                return true;
            } else {
                return false;
            }
        } else {
            if (in.remaining() >= len) {
                byte[] data = new byte[len];
                in.get(data);
                out.write(new MinecraftPacket(data));
                return true;
            } else {
                return false;
            }
        }
    }

    private int readVarInt(IoBuffer in) throws IOException {
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
        return Binary.getDefaultInstance().readVarInt(bb.toArray());
    }
}
