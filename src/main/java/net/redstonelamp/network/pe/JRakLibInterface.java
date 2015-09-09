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

import net.beaconpe.jraklib.JRakLib;
import net.beaconpe.jraklib.Logger;
import net.beaconpe.jraklib.protocol.EncapsulatedPacket;
import net.beaconpe.jraklib.server.JRakLibServer;
import net.beaconpe.jraklib.server.ServerHandler;
import net.beaconpe.jraklib.server.ServerInstance;
import net.redstonelamp.Player;
import net.redstonelamp.Server;
import net.redstonelamp.network.LowLevelNetworkException;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.netInterface.AdvancedNetworkInterface;
import net.redstonelamp.ticker.CallableTask;
import net.redstonelamp.ticker.Task;
import net.redstonelamp.ui.ConsoleOut;
import net.redstonelamp.ui.Log4j2ConsoleOut;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Network interface to communicate with JRakLib
 *
 * @author RedstoneLamp Team
 */
public class JRakLibInterface implements ServerInstance, AdvancedNetworkInterface{
    private final Server server;
    private final PEProtocol protocol;
    private JRakLibServer rakLibServer;
    private ServerHandler handler;
    private net.redstonelamp.ui.Logger logger;

    private Deque<UniversalPacket> packetQueue = new ConcurrentLinkedDeque<>();

    private CallableTask tickTask = new CallableTask("tick", this);

    public JRakLibInterface(Server server, PEProtocol protocol){
        //TODO: Correct port and interface
        this.server = server;
        this.protocol = protocol;

        setupLogger();
        startupInterface();

        logger.info("Started JRakLib Interface on "+server.getConfig().getString("server-ip") + ":" + server.getConfig().getInt("mcpe-port"));
    }

    private void startupInterface() {
        String ip = server.getConfig().getString("server-ip");
        int port = server.getConfig().getInt("mcpe-port");

        rakLibServer = new JRakLibServer(new JRakLibLogger(new net.redstonelamp.ui.Logger(new Log4j2ConsoleOut("JRakLib"))), port, ip);
        handler = new ServerHandler(rakLibServer, this);

        handler.sendOption("name", "MCPE;RedstoneLamp test server;" + PENetworkConst.MCPE_PROTOCOL + ";0.12.1;0;1");

        server.getTicker().addRepeatingTask(tickTask, 1);
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

    public void tick(long tick){
        while(handler.handlePacket()){
        }

        if(rakLibServer.getState() == Thread.State.TERMINATED){
            server.getTicker().cancelTask(tickTask);
            server.getLogger().error("[JRakLibInterface]: JRakLib Server crashed!");
            server.getTicker().addDelayedTask(tick1 -> {
                server.getLogger().info("Restarting JRakLib Server (crashed!)...");
                startupInterface();
                server.getLogger().info("JRakLib Server restarted.");
            }, 20);
        }
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
        //Assume packet is to be encapsulated
        EncapsulatedPacket pk = new EncapsulatedPacket();
        pk.messageIndex = 0;
        pk.reliability = 2;
        pk.buffer = packet.getBuffer();
        logger.buffer("(" + packet.getAddress().toString() + ") PACKET OUT: ", pk.buffer, "");
        handler.sendEncapsulated(packet.getAddress().toString(), pk, immediate ? JRakLib.PRIORITY_IMMEDIATE : JRakLib.PRIORITY_NORMAL);
    }

    @Override
    public void openSession(String identifier, String address, int port, long clientID){
        logger.debug("(" + identifier + ") openSession: {clientID: " + clientID + "}");
        protocol.openSession(identifier);
    }

    @Override
    public void closeSession(String identifier, String reason){
        logger.debug("(" + identifier + ") closeSession: {reason: " + reason + "}");
        Player player = server.getPlayer(new JRakLibIdentifierAddress(identifier));
        if(player != null){
            if(player.isSpawned()){
                player.close(" left the game", reason, false);
            }else{
                player.close("", reason, false);
            }
        }
    }

    @Override
    public void handleEncapsulated(String identifier, EncapsulatedPacket encapsulatedPacket, int flags){
        UniversalPacket packet = new UniversalPacket(encapsulatedPacket.buffer, new JRakLibIdentifierAddress(identifier));
        logger.buffer("(" + identifier + ") PACKET IN: ", packet.getBuffer(), "");
        packetQueue.addLast(packet);
    }

    @Override
    public void handleRaw(String address, int port, byte[] payload){
        UniversalPacket packet = new UniversalPacket(payload, new InetSocketAddress(address, port));
        packetQueue.addLast(packet);
    }

    @Override
    public void notifyACK(String identifier, int identifierACK){

    }

    @Override
    public void exceptionCaught(String clazz, String message) {
        logger.warning("JRakLib caught an exception! "+clazz+": "+message);
    }

    @Override
    public void handleOption(String option, String value){

    }

    /**
     * INTERNAL METHOD!
     * Close a session for no reason
     *
     * @param identifier
     * @param reason
     */
    public void _internalClose(String identifier, String reason){
        handler.closeSession(identifier, reason);
    }

    @Override
    public void setName(String name){
        handler.sendOption("name", "MCPE;" + name + ";" + PENetworkConst.MCPE_PROTOCOL + ";" + PENetworkConst.MCPE_VERSION + ";" + server.getPlayers().size() + ";" + server.getMaxPlayers());
    }

    /**
     * Wrapper JRakLib logger that wraps around a UI logger.
     *
     * @author RedstoneLamp Team
     */
    public static class JRakLibLogger implements Logger{
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
    public static class JRakLibIdentifierAddress extends SocketAddress{
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
}
