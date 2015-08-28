package net.redstonelamp.network.pc;

import net.redstonelamp.Player;
import net.redstonelamp.network.NetworkManager;
import net.redstonelamp.network.Protocol;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.request.Request;
import net.redstonelamp.response.Response;

/**
 * A Protocol implementation of the Minecraft: PC protocol.
 *
 * @author RedstoneLamp Team
 */
public class PCProtocol extends Protocol implements PCNetworkConst{

    public PCProtocol(NetworkManager manager) {
        super(manager);
        _interface = new MinaInterface(manager.getServer());
    }

    @Override
    public String getName() {
        return "MCPC";
    }

    @Override
    public String getDescription() {
        return "Minecraft: PC edition protocol, version "+MC_VERSION+" (protocol "+MC_PROTOCOL+")";
    }

    @Override
    public Request[] handlePacket(UniversalPacket packet) {
        return new Request[0];
    }

    @Override
    protected UniversalPacket[] _sendResponse(Response response, Player player) {
        return new UniversalPacket[0];
    }
}
