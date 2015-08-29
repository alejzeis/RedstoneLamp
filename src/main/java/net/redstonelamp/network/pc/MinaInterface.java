/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.redstonelamp.network.pc;

import net.redstonelamp.Server;
import net.redstonelamp.network.LowLevelNetworkException;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.netInterface.AdvancedNetworkInterface;
import net.redstonelamp.network.pc.codec.MinecraftPacketHeaderDecoder;
import net.redstonelamp.network.pc.codec.MinecraftPacketHeaderEncoder;
import net.redstonelamp.ui.ConsoleOut;
import net.redstonelamp.ui.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * An AdvancedNetworkInterface implementation of an Apache MINA handler for
 * Minecraft: PC edition.
 *
 * @author RedstoneLamp Team
 */
public class MinaInterface extends IoHandlerAdapter implements AdvancedNetworkInterface {
    private final Server server;
    private final PCProtocol protocol;
    private final IoAcceptor acceptor;

    private Logger logger;
    private Map<String, IoSession> sessions = new ConcurrentHashMap<>();
    private Map<String, ProtocolState> states = new ConcurrentHashMap<>();
    private Deque<UniversalPacket> packetQueue = new ConcurrentLinkedDeque<>();

    public MinaInterface(Server server, PCProtocol protocol) {
        this.server = server;
        this.protocol = protocol;

        setupLogger();

        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger", new LoggingFilter()); //TODO: fix the debug output
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MinecraftPacketHeaderEncoder(this), new MinecraftPacketHeaderDecoder(this)));

        acceptor.setHandler(this);

        acceptor.getSessionConfig().setReadBufferSize(4096);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

        try {
            acceptor.bind(new InetSocketAddress(server.getConfig().getInt("mcpc-port")));
            logger.info("Started Apache MINA Interface on port "+server.getConfig().getInt("mcpc-port"));
        } catch (IOException e) {
            logger.error("*** FAILED TO BIND TO PORT! "+e.getClass().getName()+": "+e.getMessage());
            logger.error("Perhaps there is another server running on that port?");
            logger.trace(e);
        }
    }

    private void setupLogger() {
        try {
            Constructor c = server.getLogger().getConsoleOutClass().getConstructor(String.class);
            logger = new Logger((ConsoleOut) c.newInstance("MinaInterface"));
            logger.debug("Logger created.");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            server.getLogger().warning("Failed to create Logger for MinaInterface: "+e.getClass().getName()+": "+e.getMessage());
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.warning("Apache MINA caught an exception! " + cause.getClass().getName() + ": " + cause.getMessage());
        logger.trace(cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if(!(message instanceof UniversalPacket)) {
            throw new LowLevelNetworkException("Message must be instanceof UniversalPacket!");
        }
        UniversalPacket up = (UniversalPacket) message;
        if(!states.containsKey(session.getRemoteAddress().toString())) {
            byte id = up.getBuffer()[0];
            switch(id) {
                case PCNetworkConst.HANDSHAKE_HANDSHAKE:
                    //TODO: Decode handshake and add the sessions remote address to the states map depending if Status or LOGIN
                    break;
            }
        }

        if(states.get(session.getRemoteAddress().toString()) == ProtocolState.STATE_STATUS) {
            //TODO: Reply with Status Response, but MAKE SURE TO CHECK THAT IT'S A STATUS REQUEST
        } else {
            packetQueue.add(up);
        }
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public UniversalPacket readPacket() throws LowLevelNetworkException {
        if(!packetQueue.isEmpty()) {
            return packetQueue.removeLast();
        }
        return null;
    }

    @Override
    public void sendPacket(UniversalPacket packet, boolean immediate) throws LowLevelNetworkException {
        if(sessions.containsKey(packet.getAddress().toString())) {
            sessions.get(packet.getAddress().toString()).write(packet);
            return;
        }
        throw new LowLevelNetworkException("Failed to find session for: "+packet.getAddress().toString());
    }
}
