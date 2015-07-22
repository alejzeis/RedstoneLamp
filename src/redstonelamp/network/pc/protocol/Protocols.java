package redstonelamp.network.pc.protocol;

import com.flowpowered.networking.protocol.Protocol;

/**
 * Created by jython234 on 7/22/2015.
 */
public enum Protocols {
    HANDSHAKE_PROTOCOL(new HandshakeProtocol());

    private Protocol protocol;

    Protocols(Protocol protocol){
        this.protocol = protocol;
    }

    public Protocol getProtocol(){
        return protocol;
    }
}
