package redstonelamp.network.pc.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.DemuxingProtocolEncoder;
import redstonelamp.DesktopPlayer;
import redstonelamp.Player;
import redstonelamp.network.packet.DataPacket;
import redstonelamp.network.pc.PCInterface;
import redstonelamp.network.pc.packet.MinecraftPacket;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.utils.Binary;
import redstonelamp.utils.CompressionUtils;
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
            boolean compression = false;
            int threshold = -1;

            Player player = pcInterface.getServer().getPlayer(session.getRemoteAddress().toString());
            if(player != null){
                DesktopPlayer dp = (DesktopPlayer) player;
                compression = dp.isCompressionActivated();
                threshold = dp.getCompressionThreshold();
            }

            DynamicByteBuffer bb = DynamicByteBuffer.newInstance(ByteOrder.BIG_ENDIAN);
            if(!compression){ //Compression is disabled
                bb.putVarInt(buffer.length);
                bb.put(buffer);
                return;
            } else if(buffer.length < threshold){ //Compression is enabled, but the packet is less than the threshold
                byte[] uncompressedLenBytes = Binary.newInstance(ByteOrder.BIG_ENDIAN).writeVarInt(0);
                bb.putVarInt(buffer.length + uncompressedLenBytes.length);
                bb.put(uncompressedLenBytes);
                bb.put(buffer);
            } else { //Compression is enabled, and the packet is greater than the threshold
                byte[] compressed = CompressionUtils.zlibDeflate(buffer, 7); //TODO: correct level
                byte[] uncompressedLenBytes = Binary.newInstance(ByteOrder.BIG_ENDIAN).writeVarInt(buffer.length);
                bb.putVarInt(compressed.length + uncompressedLenBytes.length);
                bb.put(uncompressedLenBytes);
                bb.put(compressed);
            }

            IoBuffer buffer2 = IoBuffer.wrap(bb.toArray());
            System.out.println("(Compression: "+compression+", wasCompressed: "+(buffer.length < threshold)+") Wrote: "+buffer2.getHexDump());
            out.write(buffer2);
        } else {
            throw new IllegalArgumentException("Expected PCDataPacket.");
        }
    }
}
