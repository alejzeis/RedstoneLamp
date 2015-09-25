package net.redstonelamp.network.pe;

import net.redstonelamp.network.netInterface.AdvancedNetworkInterface;

import java.net.SocketAddress;

/**
 * Extension from AdvancedNetworkInterface, has internal methods used by PEProtocol
 *
 * @author RedstoneLamp Team
 */
public interface PEInterface extends AdvancedNetworkInterface{
    void _internalClose(SocketAddress address, String reason);
}
