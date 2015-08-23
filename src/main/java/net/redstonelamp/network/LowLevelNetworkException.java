package net.redstonelamp.network;

import java.io.IOException;

/**
 * This exception represents a low-level exception while reading/writing a packet.
 *
 * @author RedstoneLamp Team
 */
public class LowLevelNetworkException extends IOException {

    public LowLevelNetworkException(String message) {
        super(message);
    }
}