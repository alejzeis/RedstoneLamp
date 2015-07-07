package redstonelamp.network;

import net.beaconpe.jraklib.protocol.EncapsulatedPacket;
import net.beaconpe.jraklib.server.JRakLibServer;
import net.beaconpe.jraklib.server.ServerHandler;
import net.beaconpe.jraklib.server.ServerInstance;
import redstonelamp.Server;

/**
 * Interface for communicating with the JRakLib library.
 */
public class JRakLibInterface implements ServerInstance{
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
    }

    public void process(){
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
    public void openSession(String identifier, String address, int port, long clientID) {

    }

    @Override
    public void closeSession(String identifier, String reason) {

    }

    @Override
    public void handleEncapsulated(String identifier, EncapsulatedPacket packet, int flags) {

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
}
