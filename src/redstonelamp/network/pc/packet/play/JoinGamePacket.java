package redstonelamp.network.pc.packet.play;

import redstonelamp.network.pc.PCNetworkInfo;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * JoinGamePacket 0x01 (STATE: PLAY, S -> C)
 *
 * @author jython234
 */
public class JoinGamePacket extends PCDataPacket{
    public final static int ID = PCNetworkInfo.PLAY_JOIN_GAME;

    public int entityID;
    public byte gamemode;
    public byte dimension;
    public byte difficulty;
    public byte maxPlayers;
    public String levelType;
    public boolean reducedDebugInfo = false;

    @Override
    public int getID() {
        return PCNetworkInfo.PLAY_JOIN_GAME;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putInt(entityID);
        bb.putByte(gamemode);
        bb.putByte(dimension);
        bb.putByte(difficulty);
        bb.putByte(maxPlayers);
        bb.putPCString(levelType);
        bb.putByte((byte) (reducedDebugInfo ? 1 : 0));
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }
}
