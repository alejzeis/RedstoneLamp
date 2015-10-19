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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteOrder;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.beaconpe.jraklib.JRakLib;
import net.beaconpe.jraklib.protocol.EncapsulatedPacket;
import net.beaconpe.jraklib.server.JRakLibServer;
import net.beaconpe.jraklib.server.ServerHandler;
import net.beaconpe.jraklib.server.ServerInstance;
import net.redstonelamp.Player;
import net.redstonelamp.Server;
import net.redstonelamp.event.EventPlatform;
import net.redstonelamp.event.server.ServerReceivePacketEvent;
import net.redstonelamp.event.server.ServerSendPacketEvent;
import net.redstonelamp.network.LowLevelNetworkException;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.ui.ConsoleOut;
import net.redstonelamp.ui.Logger;

/**
 * An AdvancedNetworkInterface implementation for the JRakLib library.
 *
 * @author RedstoneLamp Team
 */
public class JRakLibInterface implements ServerInstance, PEInterface {
    private final Server server;
    private final PEProtocol protocol;
    private final JRakLibServer rakLibServer;
    private final ServerHandler handler;
    private final JRakLibPacketHandler packetHandler;
    private Logger logger;

    private Queue<UniversalPacket> packetQueue = new ConcurrentLinkedQueue<>();

    public JRakLibInterface(Server server, PEProtocol protocol) {
        this.server = server;
        this.protocol = protocol;

        setupLogger();

        String ip = server.getConfig().getString("server-ip");
        int port = server.getConfig().getInt("mcpe-port");

        rakLibServer = new JRakLibServer(new JRakLibLogger(setupLibraryLogger()), port, ip);
        handler = new ServerHandler(rakLibServer, this);
        packetHandler = new JRakLibPacketHandler(this);
        packetHandler.start();

        logger.info("MCPE server started on "+ip+":"+port);
    }

    private void setupLogger() {
        try{
            Constructor c = server.getLogger().getConsoleOutClass().getConstructor(String.class);
            logger = new net.redstonelamp.ui.Logger((ConsoleOut) c.newInstance("JRakLibInterface"));
            logger.debug("Logger created.");
        }catch(NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e){
            e.printStackTrace();
        }
    }

    private Logger setupLibraryLogger() {
        try{
            Constructor c = server.getLogger().getConsoleOutClass().getConstructor(String.class);
            return new net.redstonelamp.ui.Logger((ConsoleOut) c.newInstance("JRakLib"));
        }catch(NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setName(String name) {
        handler.sendOption("name", "MCPE;" + name.split("\n")[0] + ";" + PENetworkConst.MCPE_PROTOCOL + ";" + PENetworkConst.MCPE_VERSION + ";" + server.getPlayers().size() + ";" + server.getMaxPlayers());
    }

    @Override
    public UniversalPacket readPacket() throws LowLevelNetworkException {
        if(!packetQueue.isEmpty()) {
            return packetQueue.remove();
        }
        return null;
    }

    @Override
    public void sendPacket(UniversalPacket packet, boolean immediate) throws LowLevelNetworkException {
        EncapsulatedPacket pk = new EncapsulatedPacket();
        pk.messageIndex = 0;
        pk.reliability = 2;
        pk.buffer = packet.getBuffer();
        ServerSendPacketEvent event = new ServerSendPacketEvent(packet);
        server.callEvent(EventPlatform.POCKET, event);
        if(!event.isCancelled()) {
        	logger.buffer("(" + packet.getAddress().toString() + ") PACKET OUT: ", pk.buffer, "");
        	handler.sendEncapsulated(packet.getAddress().toString(), pk, immediate ? JRakLib.PRIORITY_IMMEDIATE : JRakLib.PRIORITY_NORMAL);
        }
    }

    @Override
    public void shutdown() throws LowLevelNetworkException {
        handler.shutdown();
        packetHandler.shutdown();
        while(rakLibServer.getState() != Thread.State.TERMINATED);
    }

    @Override
    public void openSession(String identifier, String address, int port, long clientID) {
        logger.debug("(" + identifier + ") openSession: {clientID: " + clientID + "}");
        protocol.openSession(identifier);
    }

    @Override
    public void closeSession(String identifier, String reason) {
        logger.debug("(" + identifier + ") closeSession: {reason: " + reason + "}");
        Player player = server.getPlayer(new JRakLibIdentifierAddress(identifier));
        if(player != null){
            if(player.isSpawned()){
                player.close("redstonelamp.translation.player.left", reason, false);
            }else{
                player.close("", reason, false);
            }
        }
    }

    @Override
    public void handleEncapsulated(String identifier, EncapsulatedPacket packet, int flags) {
        UniversalPacket pk = new UniversalPacket(packet.buffer, ByteOrder.BIG_ENDIAN, new JRakLibIdentifierAddress(identifier));
    	ServerReceivePacketEvent event = new ServerReceivePacketEvent(pk);
        server.callEvent(EventPlatform.POCKET, event);
        logger.buffer("(" + identifier + ") PACKET IN: ", pk.getBuffer(), "");
        if(!event.isCancelled()) {
        	packetQueue.add(pk);
        }
    }

    @Override
    public void handleRaw(String address, int port, byte[] payload) {
        UniversalPacket packet = new UniversalPacket(payload, new InetSocketAddress(address, port));
    	ServerReceivePacketEvent event = new ServerReceivePacketEvent(packet);
        server.callEvent(EventPlatform.POCKET, event);
        if(!event.isCancelled()) {
        	packetQueue.add(packet);
        }
    }

    @Override
    public void notifyACK(String identifier, int identifierACK) {
    	// TODO: Create event
    }

    @Override
    public void exceptionCaught(String clazz, String message) {
        logger.error("JRakLib caught an exception! "+clazz+": "+message);
    }

    @Override
    public void handleOption(String option, String value) {
    	// TODO: Create event
    }


    @Override
    public void _internalClose(SocketAddress address, String reason){
        handler.closeSession(address.toString(), reason);
    }

    /**
     * Wrapper JRakLib logger that wraps around a UI logger.
     *
     * @author RedstoneLamp Team
     */
    public static class JRakLibLogger implements net.beaconpe.jraklib.Logger {
        private final net.redstonelamp.ui.Logger logger;

        public JRakLibLogger(net.redstonelamp.ui.Logger logger){
            this.logger = logger;
        }

        @Override
        public void notice(String s){
            logger.info(s);
        }

        @Override
        public void critical(String s){
            logger.error(s);
        }

        @Override
        public void emergency(String s){
            logger.fatal(s);
        }
    }

    /**
     * A SocketAddress implementation of a JRakLib Identifier String.
     *
     * @author RedstoneLamp Team
     */
    public static class JRakLibIdentifierAddress extends SocketAddress {
        private final String identifier;

        public JRakLibIdentifierAddress(String identifier){
            this.identifier = identifier;
        }

        @Override
        public String toString(){
            return getIdentifier();
        }

        public String getIdentifier(){
            return identifier;
        }
    }

    /**
     * A Thread that handles Thread-To-Main packets from JRakLib.
     *
     * @author RedstoneLamp Team
     */
    public static class JRakLibPacketHandler extends Thread {
        private boolean running = false;
        private JRakLibInterface jRakLibInterface;

        public JRakLibPacketHandler(JRakLibInterface jRakLibInterface) {
            this.jRakLibInterface = jRakLibInterface;
        }

        @Override
        public void run() {
            setName("JRakLib-ServerHandler");
            while(running) {
                jRakLibInterface.handler.handlePacket();
                if(jRakLibInterface.rakLibServer.getState() == State.TERMINATED) {
                    jRakLibInterface.logger.fatal("JRakLib Server crashed!");
                    shutdown();
                }
            }
        }

        @Override
        public void start() {
            running = true;
            super.start();
        }

        public void shutdown() {
            running = false;
        }
    }
}