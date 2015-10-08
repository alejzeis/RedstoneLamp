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
package net.redstonelamp.network.pe;

import io.github.jython234.jraklibplus.protocol.raknet.EncapsulatedPacket;
import io.github.jython234.jraklibplus.protocol.raknet.Reliability;
import io.github.jython234.jraklibplus.server.NioSession;
import io.github.jython234.jraklibplus.server.RakNetServer;
import io.github.jython234.jraklibplus.server.ServerInterface;

import io.github.jython234.jraklibplus.util.SystemAddress;
import net.redstonelamp.Player;
import net.redstonelamp.Server;
import net.redstonelamp.network.LowLevelNetworkException;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.netInterface.AdvancedNetworkInterface;
import net.redstonelamp.ui.ConsoleOut;
import net.redstonelamp.ui.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Network interface to communicate with JRakLib
 *
 * @author RedstoneLamp Team
 */
public class JRakLibPlusInterface implements ServerInterface, AdvancedNetworkInterface {
    private final Server server;
    private final PEProtocol protocol;
    private final RakNetServer rakServer;
    private Logger logger;

    private Queue<UniversalPacket> packetQueue = new ConcurrentLinkedQueue<>();

    public JRakLibPlusInterface(Server server, PEProtocol protocol) {
        this.server = server;
        this.protocol = protocol;
        setupLogger();
        RakNetServer.ServerOptions options = new RakNetServer.ServerOptions();
        options.workerThreads = 2;
        options.portChecking = false;
        options.disconnectInvalidProtocol = false;
        options.name = "MCPE;"+server.getConfig().getString("motd")+";"+PENetworkConst.MCPE_PROTOCOL+";"+PENetworkConst.MCPE_VERSION+";"+server.getPlayers().size()+";"+server.getMaxPlayers();

        InetSocketAddress address = new InetSocketAddress(server.getConfig().getString("server-ip"), server.getConfig().getInt("mcpe-port"));

        rakServer = new RakNetServer(LoggerFactory.getLogger("JRakLib"), address, options, this);
        rakServer.startup();
        logger.info("Started MCPE server on "+address);
    }

    private void setupLogger(){
        try{
            Constructor c = server.getLogger().getConsoleOutClass().getConstructor(String.class);
            logger = new net.redstonelamp.ui.Logger((ConsoleOut) c.newInstance("JRakLibInterface"));
            logger.debug("Logger created.");
        }catch(NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e){
            e.printStackTrace();
        }
    }

    @Override
    public void setName(String name) {
        //TODO
    }

    @Override
    public UniversalPacket readPacket() throws LowLevelNetworkException {
        if(!packetQueue.isEmpty()) {
            return packetQueue.remove();
        }
        return null;
    }

    @Override
    public synchronized void sendPacket(UniversalPacket packet, boolean immediate) throws LowLevelNetworkException {
        NioSession session = rakServer.getSession(SystemAddress.fromSocketAddress(packet.getAddress()));
        if(session == null) {
            throw new LowLevelNetworkException("Could not find session for address "+packet.getAddress());
        }
        EncapsulatedPacket pkt = new EncapsulatedPacket();
        pkt.reliability = Reliability.RELIABLE;
        pkt.payload = packet.getBuffer();
        session.addEncapsulatedToQueue(pkt, immediate);
        logger.buffer("("+session.getIpAddress()+":"+session.getPort()+") PACKET OUT: ", packet.getBuffer(), "");
    }

    @Override
    public void shutdown() throws LowLevelNetworkException {
        rakServer.interrupt();
    }

    @Override
    public void handleEncapsulatedPacket(EncapsulatedPacket encapsulatedPacket, NioSession session) {
        UniversalPacket packet = new UniversalPacket(encapsulatedPacket.payload, new InetSocketAddress(session.getIpAddress(), session.getPort()));
        logger.buffer("("+session.getIpAddress()+":"+session.getPort()+") PACKET IN: ", packet.getBuffer(), "");
        packetQueue.add(packet);
    }

    @Override
    public void sessionOpened(NioSession session) {
        logger.debug("(" + session.getIpAddress() + ":" + session.getPort() + ") Session Opened");
        protocol.openSession(session.getIpAddress() + ":" + session.getPort());
    }

    @Override
    public void sessionClosed(NioSession session, String reason) {
        logger.debug("("+session.getIpAddress()+":"+session.getPort()+") Session Closed: {reason: "+reason+"}");
        Player player = server.getPlayer(new InetSocketAddress(session.getIpAddress(), session.getPort()));
        if(player != null){
            if(player.isSpawned()){
                player.close("redstonelamp.translation.player.left", reason, false);
            }else{
                player.close("", reason, false);
            }
        }
    }

    /**
     * INTERNAL METHOD!
     * Close a session for no reason
     *
     * @param address
     * @param reason
     */
    public void _internalClose(SocketAddress address, String reason) {
        NioSession session = rakServer.getSession(SystemAddress.fromSocketAddress(address));
        if(session != null) {
            session.disconnect(reason);
        }
    }
}