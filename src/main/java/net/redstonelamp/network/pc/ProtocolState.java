package net.redstonelamp.network.pc;

/**
 * Represents different states of the Minecraft protocol
 *
 * @author RedstoneLamp Team
 */
public enum ProtocolState {
    /**
     * The Handshake state is the first state when a client connects.
     * Currently there is only one packet sent in this state.
     */
    STATE_HANDSHAKE,
    /**
     * The Status state is the state when the client pings the server for the
     * MOTD.
     */
    STATE_STATUS,
    /**
     * The Login state is when the client and server authenticate.
     */
    STATE_LOGIN,
    /**
     * The Play state is the final state in which all game packets are sent.
     */
    STATE_PLAY;
}
