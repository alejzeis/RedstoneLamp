package net.redstonelamp.network.netInterface;

/**
 * An advanced network interface that allows setting and updating the server name, along with
 * more features.
 *
 * @author RedstoneLamp Team
 */
public interface AdvancedNetworkInterface extends NetworkInterface {
    /**
     * Set the name of the server, which should show up on any pings to the interface
     * @param name The name to be set to.
     */
    void setName(String name);
}
