package redstonelamp.network.pc;

/**
 * Different protocol states for the Minecraft: PC protocol.
 *
 * @author jython234
 */
public enum ProtocolState {
    /**
     * The Handshake State. There is only one packet in this state, HandshakePacket (C -> S)
     */
    STATE_HANDSHAKE,
    /**
     * The Status state, in which the client queries the server for the MOTD and player counts.
     */
    STATE_STATUS,
    /**
     * The Login state, in which the client authenticates with the server.
     */
    STATE_LOGIN,
    /**
     * The Play state, where all the game packets are sent.
     */
    STATE_PLAY
}
