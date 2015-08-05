package redstonelamp.network.packet;

import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * TextPacket (0x85)
 */
public class TextPacket extends DataPacket{
    public final static byte ID = PENetworkInfo.TEXT_PACKET;

    public final static byte TYPE_RAW = 0;
    public final static byte TYPE_CHAT = 1;
    public final static byte TYPE_TRANSLATION = 2;
    public final static byte TYPE_POPUP = 3;
    public final static byte TYPE_TIP = 4;

    public byte type;
    public String source;
    public String message;
    public String[] parameters;

    @Override
    public byte getPID() {
        return PENetworkInfo.TEXT_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putByte(type);
        switch(type){
            case TYPE_CHAT:
                bb.putString(source);
            case TYPE_RAW:
            case TYPE_POPUP:
            case TYPE_TIP:
                bb.putString(message);
                break;
            case TYPE_TRANSLATION:
                bb.putString(message);
                bb.putByte((byte) parameters.length);
                for(String p : parameters){
                    bb.putString(p);
                }

        }
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {
        type = bb.getByte();
        switch(type){
            case TYPE_CHAT:
                source = bb.getString();
            case TYPE_RAW:
            case TYPE_POPUP:
            case TYPE_TIP:
                message = bb.getString();
                break;
            case TYPE_TRANSLATION:
                message = bb.getString();
                int count = bb.getByte();
                parameters = new String[count];
                for(int i = 0;i < count; i++){
                    parameters[i] = bb.getString();
                }
        }
    }
}
