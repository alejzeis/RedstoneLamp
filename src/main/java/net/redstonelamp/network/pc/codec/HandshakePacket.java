package net.redstonelamp.network.pc.codec;

import net.redstonelamp.network.UniversalPacket;

public class HandshakePacket{

    public int protocol;
    public String address;
    public int port;
    public int nextState;

    public final int STATUS = 1;
    public final int LOGIN = 2;

    public HandshakePacket(UniversalPacket packet){
        this.protocol = packet.bb().getVarInt();
        this.address = packet.bb().getVarString();
        this.port = packet.bb().getUnsignedShort();
        this.nextState = packet.bb().getVarInt();
    }

}
