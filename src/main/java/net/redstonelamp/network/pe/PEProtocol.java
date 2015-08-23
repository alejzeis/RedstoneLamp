package net.redstonelamp.network.pe;

import net.redstonelamp.network.NetworkManager;
import net.redstonelamp.network.Protocol;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.request.Request;

/**
 * Created by jython234 on 8/23/2015.
 *
 * @author RedstoneLamp Team
 */
public class PEProtocol extends Protocol{

    /**
     * Construct new PEProtocol.
     *
     * @param manager
     */
    public PEProtocol(NetworkManager manager) {
        super(manager);
        _interface = new JRakLibInterface(manager.getServer());
    }

    @Override
    public String getName() {
        return "MCPE";
    }

    @Override
    public String getDescription() {
        return "Minecraft: Pocket Edition protocol, version "+PENetworkConst.MCPE_VERSION+" (protocol: "+PENetworkConst.MCPE_PROTOCOL+")";
    }

    @Override
    public Request handlePacket(UniversalPacket packet) {
        return null;
    }
}
