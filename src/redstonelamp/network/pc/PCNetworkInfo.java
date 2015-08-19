package redstonelamp.network.pc;

/**
 * Network Constants class for MCPC.
 */
public class PCNetworkInfo {
    public final static String MC_VERSION = "1.8.8";
    public final static int MC_PROTOCOL = 47;

    //Handshake packets
    public final static int HANDHSAKE_HANDSHAKE = 0x00;

    //Status packets
    public final static int STATUS_REQUEST = 0x00;
    public final static int STATUS_RESPONSE = 0x00;
    public final static int STATUS_PING = 0x01;
    public final static int STATUS_PONG = 0x01;

    //Login packets
    public final static int LOGIN_LOGIN_START = 0x00;
    public final static int LOGIN_ENCRYPTION_REQUEST = 0x01;
    public final static int LOGIN_ENCRYPTION_RESPONSE = 0x01;
    public final static int LOGIN_DISCONNECT = 0x00;
    public final static int LOGIN_LOGIN_SUCCESS = 0x02;
    public final static int LOGIN_SET_COMPRESSION = 0x03;

    //Play packets
    public final static int PLAY_JOIN_GAME = 0x01;
}
