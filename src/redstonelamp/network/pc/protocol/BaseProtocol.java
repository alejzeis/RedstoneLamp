package redstonelamp.network.pc.protocol;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.Message;
import com.flowpowered.networking.MessageHandler;
import com.flowpowered.networking.exception.IllegalOpcodeException;
import com.flowpowered.networking.exception.UnknownPacketException;
import com.flowpowered.networking.protocol.AbstractProtocol;
import com.flowpowered.networking.service.CodecLookupService;
import com.flowpowered.networking.service.HandlerLookupService;
import com.flowpowered.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import redstonelamp.RedstoneLamp;
import redstonelamp.utils.Binary;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Base protocol for all protocols in the <code>protocol</code> package.
 * <br>
 * Portions of code from: https://github.com/GlowstoneMC/Glowstone/blob/master/src/main/java/net/glowstone/net/protocol/GlowProtocol.java
 */
public abstract class BaseProtocol extends AbstractProtocol{

    private final CodecLookupService inboundCodecs;
    private final CodecLookupService outboundCodecs;
    private final HandlerLookupService handlers;

    public BaseProtocol(String name, int highestOpCode) {
        super(name);
        inboundCodecs = new CodecLookupService(highestOpCode + 1);
        outboundCodecs = new CodecLookupService(highestOpCode + 1);
        handlers = new HandlerLookupService();
    }

    protected <M extends Message, C extends Codec<? super M>, H extends MessageHandler<?, ? super M>> void registerInbound(int opcode, Class<M> message, Class<C> codec, Class<H> handler){
        try {
            inboundCodecs.bind(message, codec, opcode);
        } catch (InstantiationException e) {
            getLogger().error("("+getName()+") Failed to register inbound packet: "+opcode);
        } catch (IllegalAccessException e) {
            getLogger().error("("+getName()+") Failed to register inbound packet: "+opcode);
        } catch (InvocationTargetException e) {
            getLogger().error("("+getName()+") Failed to register inbound packet: "+opcode);
        }
    }

    protected <M extends Message, C extends Codec<? super M>> void registerOutbound(int opcode, Class<M> message, Class<C> codec) {
        try {
            outboundCodecs.bind(message, codec, opcode);
        } catch (InstantiationException e) {
            getLogger().error("("+getName()+") Failed to register outbound packet: "+opcode);
        } catch (IllegalAccessException e) {
            getLogger().error("("+getName()+") Failed to register outbound packet: "+opcode);
        } catch (InvocationTargetException e) {
            getLogger().error("("+getName()+") Failed to register outbound packet: "+opcode);
        }
    }

    @Override
    public <M extends Message> MessageHandler<?, M> getMessageHandle(Class<M> clazz) {
        MessageHandler<?, M> handler = handlers.find(clazz);
        if(handler == null){
            RedstoneLamp.getServerInstance().getLogger().warning("Failed to find handler for "+clazz.getName());
        }
        return handler;
    }

    @Override
    public Codec<?> readHeader(ByteBuf buffer) throws UnknownPacketException {
        int length = -1;
        int opcode = -1;
        try {
            length = ByteBufUtils.readVarInt(buffer);

            buffer.markReaderIndex();

            opcode = ByteBufUtils.readVarInt(buffer);
            return inboundCodecs.find(opcode);
        } catch (IOException e) {
            throw new UnknownPacketException("Failed to read packet data (corrupt?)", opcode, length);
        } catch (IllegalOpcodeException e) {
            buffer.resetReaderIndex();
            throw new UnknownPacketException("Illegal Opcode: not registered.", opcode, length);
        }
    }

    @Override
    public <M extends Message> Codec.CodecRegistration getCodecRegistration(Class<M> clazz) {
        Codec.CodecRegistration reg = outboundCodecs.find(clazz);
        if(reg == null){
            RedstoneLamp.getServerInstance().getLogger().warning("Failed to find codec to write: "+clazz.getName()+" in "+getName());
        }
        return reg;
    }

    @Override
    public ByteBuf writeHeader(ByteBuf out, Codec.CodecRegistration codec, ByteBuf data) {
        ByteBuf opcodeBuffer = Unpooled.buffer(5);
        opcodeBuffer.writeBytes(Binary.getDefaultInstance().writeVarInt(codec.getOpcode()));
        out.writeBytes(Binary.getDefaultInstance().writeVarInt(opcodeBuffer.readableBytes() + data.readableBytes()));
        out.writeBytes(Binary.getDefaultInstance().writeVarInt(codec.getOpcode()));
        return out;
    }
}
