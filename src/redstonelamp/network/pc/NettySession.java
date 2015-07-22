package redstonelamp.network.pc;

import com.flowpowered.networking.Message;
import com.flowpowered.networking.exception.ChannelClosedException;
import com.flowpowered.networking.processor.MessageProcessor;
import com.flowpowered.networking.protocol.AbstractProtocol;
import com.flowpowered.networking.protocol.Protocol;
import com.flowpowered.networking.session.BasicSession;
import com.flowpowered.networking.session.Session;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import redstonelamp.Server;
import redstonelamp.network.pc.protocol.Protocols;

/**
 * Created by jython234 on 7/22/2015.
 */
public class NettySession extends BasicSession{

    private Server server;

    public NettySession(NettyInterface nettyInterface, Channel channel){
        super(channel, (AbstractProtocol) Protocols.HANDSHAKE_PROTOCOL.getProtocol());

    }

    public NettySession(Channel channel, AbstractProtocol bootstrapProtocol) {
        super(channel, bootstrapProtocol);
    }
}
