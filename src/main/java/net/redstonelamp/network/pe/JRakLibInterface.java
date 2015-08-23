package net.redstonelamp.network.pe;

import net.beaconpe.jraklib.Logger;
import net.beaconpe.jraklib.protocol.EncapsulatedPacket;
import net.beaconpe.jraklib.server.JRakLibServer;
import net.beaconpe.jraklib.server.ServerHandler;
import net.beaconpe.jraklib.server.ServerInstance;
import net.redstonelamp.Server;
import net.redstonelamp.network.LowLevelNetworkException;
import net.redstonelamp.network.NetworkInterface;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.ticker.CallableTask;
import net.redstonelamp.ui.Log4j2ConsoleOut;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Network interface to communicate with JRakLib
 *
 * @author RedstoneLamp Team
 */
public class JRakLibInterface implements ServerInstance, NetworkInterface{
    private final Server server;
    private final JRakLibServer rakLibServer;
    private final ServerHandler handler;

    private Deque<UniversalPacket> packetQueue = new ConcurrentLinkedDeque<>();

    private CallableTask tickTask = new CallableTask("tick", this);

    public JRakLibInterface(Server server) {
        //TODO: Correct console out, port, and interface
        this.server = server;
        rakLibServer = new JRakLibServer(new JRakLibLogger(new net.redstonelamp.ui.Logger(new Log4j2ConsoleOut("JRakLib"))), 19132, "0.0.0.0");
        handler = new ServerHandler(rakLibServer, this);

        server.getTicker().addRepeatingTask(tickTask, 0);

        server.getLogger().info("Started JRakLib Network Interface on 0.0.0.0:19132");
    }

    public void tick(long tick) {
        while(handler.handlePacket()) {}

        if(rakLibServer.getState() == Thread.State.TERMINATED) {
            server.getTicker().cancelTask(tickTask);
            server.getLogger().error("[JRakLibInterface]: JRakLib Server crashed!");
        }
    }

    @Override
    public UniversalPacket readPacket() throws LowLevelNetworkException {
        if(!packetQueue.isEmpty()) {
            return packetQueue.removeLast();
        }
        return null;
    }

    @Override
    public void sendPacket(UniversalPacket packet) throws LowLevelNetworkException {

    }

    @Override
    public void openSession(String identifier, String address, int port, long clientID) {

    }

    @Override
    public void closeSession(String identifier, String reason) {

    }

    @Override
    public void handleEncapsulated(String identifier, EncapsulatedPacket encapsulatedPacket, int flags) {
        UniversalPacket packet = new UniversalPacket(encapsulatedPacket.buffer, new JRakLibIdentifierAddress(identifier));
        packetQueue.addLast(packet);
    }

    @Override
    public void handleRaw(String address, int port, byte[] payload) {
        UniversalPacket packet = new UniversalPacket(payload, new InetSocketAddress(address, port));
        packetQueue.addLast(packet);
    }

    @Override
    public void notifyACK(String identifier, int identifierACK) {

    }

    @Override
    public void handleOption(String option, String value) {

    }

    /**
     * Wrapper JRakLib logger that wraps around a UI logger.
     *
     * @author jython234
     */
    public static class JRakLibLogger implements Logger {
        private final net.redstonelamp.ui.Logger logger;

        public JRakLibLogger(net.redstonelamp.ui.Logger logger){
            this.logger = logger;
        }

        @Override
        public void notice(String s) {
            logger.info(s);
        }

        @Override
        public void critical(String s) {
            logger.error(s);
        }

        @Override
        public void emergency(String s) {
            logger.fatal(s);
        }
    }

    /**
     * A SocketAddress implementation of a JRakLib Identifier String.
     *
     * @author jython234
     */
    public static class JRakLibIdentifierAddress extends SocketAddress {
        private final String identifier;

        public JRakLibIdentifierAddress(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public String toString() {
            return getIdentifier();
        }

        public String getIdentifier() {
            return identifier;
        }
    }
}
