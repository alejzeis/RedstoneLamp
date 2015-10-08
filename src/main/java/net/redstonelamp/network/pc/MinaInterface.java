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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteOrder;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.redstonelamp.Player;
import net.redstonelamp.Server;
import net.redstonelamp.network.LowLevelNetworkException;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.netInterface.AdvancedNetworkInterface;
import net.redstonelamp.network.pc.codec.MinecraftPacketHeaderDecoder;
import net.redstonelamp.network.pc.codec.MinecraftPacketHeaderEncoder;
import net.redstonelamp.network.pc.serializer.ChatSerializer;
import net.redstonelamp.network.pc.serializer.PingSerializer;
import net.redstonelamp.nio.BinaryBuffer;
import net.redstonelamp.ui.ConsoleOut;
import net.redstonelamp.ui.Logger;

/**
 * An AdvancedNetworkInterface implementation of an Apache MINA handler for
 * Minecraft: PC edition.
 *
 * @author RedstoneLamp Team
 */
public class MinaInterface extends IoHandlerAdapter implements AdvancedNetworkInterface{
    private final Server server;
    private final PCProtocol protocol;
    private final IoAcceptor acceptor;

    private Logger logger;
    private String name;
    private Map<String, IoSession> sessions = new ConcurrentHashMap<>();
    private Map<String, ProtocolState> states = new ConcurrentHashMap<>();
    private Deque<UniversalPacket> packetQueue = new ConcurrentLinkedDeque<>();
    private List<String> block = new CopyOnWriteArrayList<>();

    public MinaInterface(Server server, PCProtocol protocol){
        this.server = server;
        this.protocol = protocol;

        setupLogger();

        acceptor = new NioSocketAcceptor();
        //acceptor.getFilterChain().addLast("logger", new LoggingFilter()); //TODO: fix the debug output
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MinecraftPacketHeaderEncoder(this), new MinecraftPacketHeaderDecoder(this)));

        acceptor.setHandler(this);

        acceptor.getSessionConfig().setReadBufferSize(4096);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, 30);

        try{
            acceptor.bind(new InetSocketAddress(server.getConfig().getString("server-ip"), server.getConfig().getInt("mcpc-port")));
            logger.info("Started Apache MINA Interface on " + server.getConfig().getString("server-ip") + ":" + server.getConfig().getInt("mcpc-port"));
        }catch(IOException e){
            logger.error("*** FAILED TO BIND TO PORT! " + e.getClass().getName() + ": " + e.getMessage());
            logger.error("Perhaps there is another server running on that port?");
            logger.trace(e);
        }
    }

    private void setupLogger(){
        try{
            Constructor c = server.getLogger().getConsoleOutClass().getConstructor(String.class);
            logger = new Logger((ConsoleOut) c.newInstance("MinaInterface"));
            logger.debug("Logger created.");
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e){
            server.getLogger().warning("Failed to create Logger for MinaInterface: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public ProtocolState getProtocolStateOfAddress(SocketAddress address){
        if(states.containsKey(address.toString())){
            return states.get(address.toString());
        }
        return null;
    }

    protected void updateProtocolState(ProtocolState state, SocketAddress address){
        states.put(address.toString(), state);
    }

    public void close(SocketAddress address){
        IoSession session = sessions.get(address.toString());
        if(session != null){
            session.close(false); //Close after all write requests are complete
        }
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception{
        sessions.put(session.getRemoteAddress().toString(), session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception{
        if(status == IdleStatus.READER_IDLE){ //The client hasn't sent any packets
            Player player = server.getPlayer(session.getRemoteAddress());
            if(player != null){
                ProtocolState state = getProtocolStateOfAddress(session.getRemoteAddress());
                if(state != null && state == ProtocolState.STATE_LOGIN){
                    player.close("", "connection timed out", true);
                }else if(state != null && state == ProtocolState.STATE_PLAY){
                    player.close("redstonelamp.translation.player.left", "connection timed out", true);
                }
            }
        }
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception{
        sessions.remove(session.getRemoteAddress().toString());
        ProtocolState oldState = states.get(session.getRemoteAddress().toString());
        states.remove(session.getRemoteAddress().toString());
        Player player = server.getPlayer(session.getRemoteAddress());
        if(player != null){
            if(!player.isConnected()){
                return;
            }
            if(oldState != null && oldState == ProtocolState.STATE_PLAY){
                player.close("redstonelamp.translation.player.left", "connection closed", false);
            }else if(oldState != null && oldState == ProtocolState.STATE_LOGIN){
                player.close("", "connection closed", false);
            }
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception{
        logger.warning("Apache MINA caught an exception! " + cause.getClass().getName() + ": " + cause.getMessage());
        logger.trace(cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception{
        if(!(message instanceof UniversalPacket)){
            throw new LowLevelNetworkException("Message must be instanceof UniversalPacket!");
        }
        if(block.contains(session.getRemoteAddress().toString())){
            return;
        }
        UniversalPacket up = (UniversalPacket) message;
        int id;
        if(up.getBuffer().length < 1){ //Ignore "empty" packets
            return;
        }
        try{
            id = up.bb().getVarInt();
        }catch(BufferUnderflowException e){
            logger.warning(e.getClass().getName() + " while reading ID, Dump: " + up.bb().singleLineHexDump());
            throw new LowLevelNetworkException("BufferUnderflowException while reading ID");
        }
        if(!states.containsKey(session.getRemoteAddress().toString())){
            switch(id){
                case PCNetworkConst.HANDSHAKE_HANDSHAKE:
                    int protocol = up.bb().getVarInt();
                    up.bb().getVarString();
                    up.bb().getUnsignedShort();
                    int nextState = up.bb().getVarInt();
                    if(nextState == 2){
                        packetQueue.addLast(up); //Let PCProtocol handle the login
                        if(protocol != PCNetworkConst.MC_PROTOCOL){
                            BinaryBuffer bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
                            bb.putVarInt(PCNetworkConst.LOGIN_DISCONNECT);
                            System.out.println("Sent!");
                            bb.putVarString(ChatSerializer.toChat("Outdated Client! I'm on: " + PCNetworkConst.MC_VERSION + " " + PCNetworkConst.MC_PROTOCOL + ". You are on: " + protocol));
                            sendPacket(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, session.getRemoteAddress()), true);
                            block.add(session.getRemoteAddress().toString());
                            final String a = session.getRemoteAddress().toString();
                            server.getTicker().addDelayedTask(tick -> block.remove(a), 40);
                            session.close(false);
                            return;
                        }
                        states.put(session.getRemoteAddress().toString(), ProtocolState.STATE_LOGIN);
                    }else if(nextState == 1){
                        //Wait for a Status Request until sending the MOTD
                        states.put(session.getRemoteAddress().toString(), ProtocolState.STATE_STATUS);
                    }
                    break;
            }
        }

        if(states.get(session.getRemoteAddress().toString()) == ProtocolState.STATE_STATUS){
        	BinaryBuffer bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
        	switch(id){
                case PCNetworkConst.STATUS_REQUEST:
                    bb.putVarInt(PCNetworkConst.STATUS_RESPONSE);
                    bb.putVarString(PingSerializer.getStatusResponse(server, name));
                    sendPacket(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, session.getRemoteAddress()), true);
                    break;
                case PCNetworkConst.STATUS_PING:
                    long payload = up.bb().getLong();
                    bb.putVarInt(PCNetworkConst.STATUS_PONG);
                    bb.putLong(payload);
                    sendPacket(new UniversalPacket(bb.toArray(), ByteOrder.BIG_ENDIAN, session.getRemoteAddress()), true);
                    break;
            }
        }else{
            up.bb().setPosition(0);
            packetQueue.add(up);
        }
    }

    @Override
    public void setName(String name){
        this.name = name;
    }

    @Override
    public UniversalPacket readPacket() throws LowLevelNetworkException{
        if(!packetQueue.isEmpty()){
            return packetQueue.removeLast();
        }
        return null;
    }

    @Override
    public void sendPacket(UniversalPacket packet, boolean immediate) throws LowLevelNetworkException{
        if(sessions.containsKey(packet.getAddress().toString())){
            sessions.get(packet.getAddress().toString()).write(packet);
            return;
        }
        throw new LowLevelNetworkException("Failed to find session for: " + packet.getAddress().toString());
    }

    @Override
    public void shutdown() throws LowLevelNetworkException {
        this.acceptor.unbind();
    }
}