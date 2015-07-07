package redstonelamp.network;

import net.beaconpe.jraklib.protocol.EncapsulatedPacket;
import net.beaconpe.jraklib.server.JRakLibServer;
import net.beaconpe.jraklib.server.ServerHandler;
import net.beaconpe.jraklib.server.ServerInstance;
import redstonelamp.Player;
import redstonelamp.Server;
import redstonelamp.network.packet.DataPacket;
import redstonelamp.utils.TextFormat;

/**
 * Interface for communicating with the JRakLib library.
 */
public class JRakLibInterface implements ServerInstance, NetworkInterface{
    private Server server;
    private JRakLibLogger rakLibLogger;
    private JRakLibServer rakLib;
    private ServerHandler interface_;

    private boolean rakLibCrashed = false;

    public JRakLibInterface(Server server){
        this.server = server;
        rakLibLogger = new JRakLibLogger();
        rakLib = new JRakLibServer(rakLibLogger, server.getBindPort(), server.getBindInterface());
        interface_ = new ServerHandler(rakLib, this);
        server.getLogger().info("Started server on "+server.getBindInterface()+":"+server.getBindPort() + " implementing MCPE v"+NetworkInfo.MCPE_VERSION+", protocol "+NetworkInfo.MCPE_PROTOCOL);
        setName(server.getProperties().getProperty("name", "A RedstoneLamp server"));
    }

    @Override
    public void openSession(String identifier, String address, int port, long clientID) {
        server.getLogger().debug("New session from "+identifier+" with clientID: "+clientID);
        Player player = new Player(server, identifier, address, port, clientID);
        server.addPlayer(player);
    }

    @Override
    public void closeSession(String identifier, String reason) {
        server.getLogger().debug("Session "+identifier+" closed: "+reason);
        if(server.getPlayer(identifier) != null){
            server.removePlayer(server.getPlayer(identifier));
        }
    }

    @Override
    public void handleEncapsulated(String identifier, EncapsulatedPacket packet, int flags) {
        if(server.getPlayer(identifier) != null){
            try{
                if(packet.buffer != null || packet.buffer.length > 0){
                    DataPacket pk = server.getNetwork().getPacket(packet.buffer[0]);
                    if(pk != null){
                        pk.decode(packet.buffer);
                        server.getPlayer(identifier).handleDataPacket(pk);
                    }
                }
            } catch(Exception e){

            }
        }
    }

    @Override
    public void handleRaw(String address, int port, byte[] payload) {

    }

    @Override
    public void notifyACK(String identifier, int identifierACK) {

    }

    @Override
    public void handleOption(String option, String value) {

    }

    public boolean isRakLibCrashed() {
        return rakLibCrashed;
    }

    @Override
    public void sendPacket(Player player, DataPacket packet, boolean needACK, boolean immediate) {

    }

    @Override
    public void close(Player player, String reason) {

    }

    @Override
    public void setName(String name) {
        interface_.sendOption("name", "MCPE;"+name+";"+NetworkInfo.MCPE_PROTOCOL+";"+NetworkInfo.MCPE_VERSION+";"+"0;20"); //TODO: Max and online players
    }

    @Override
    public void processData() {
        if(interface_.handlePacket()){
            while(interface_.handlePacket()) { } //Handle packets.
        }

        if(rakLib.getState() == Thread.State.TERMINATED){ //Check to see if thread crashed.
            rakLibLogger.emergency("(JRakLibInterface): JRakLib crashed!");
            rakLib.dumpStack(); //Dump stack trace
            rakLibCrashed = true;
        }
    }

    @Override
    public void shutdown() {
        interface_.shutdown();
    }

    @Override
    public void emergencyShutdown() {
        interface_.emergencyShutdown();
    }
}
