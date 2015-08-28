package net.redstonelamp.network.pc.codec;

import net.redstonelamp.network.LowLevelNetworkException;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.pc.MinaInterface;
import net.redstonelamp.nio.BinaryBuffer;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.DemuxingProtocolEncoder;

import java.nio.ByteOrder;

/**
 * A DemuxingProtocolEncoder implementation that encodes Minecraft Packet headers.
 *
 * @author RedstoneLamp Team
 */
public class MinecraftPacketHeaderEncoder extends DemuxingProtocolEncoder{
    private final MinaInterface mina;

    public MinecraftPacketHeaderEncoder(MinaInterface mina) {
        super();
        this.mina = mina;
    }

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        if(message instanceof UniversalPacket) {
            UniversalPacket up = (UniversalPacket) message;
            BinaryBuffer bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
            bb.putVarInt(up.getBuffer().length);
            bb.put(up.getBuffer());
            IoBuffer buf = IoBuffer.wrap(bb.toArray());
            out.write(buf);
        } else {
            throw new LowLevelNetworkException("Message must be instanceof UniversalPacket!");
        }
    }
}
