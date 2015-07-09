package redstonelamp.network.packet;

import redstonelamp.network.NetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * Play Status Packet (0x83)
 */
public class PlayStatusPacket extends DataPacket{
    public static byte ID = NetworkInfo.PLAY_STATUS_PACKET;

    public Status status;

    @Override
    public byte getPID() {
        return NetworkInfo.PLAY_STATUS_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putInt(status.getAsInt());
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }

    public static enum Status {
        LOGIN_SUCCESS(0),
        LOGIN_FAILED_CLIENT(1),
        LOGIN_FAILED_SERVER(2),
        PLAYER_SPAWN(3);

        private int status;

        private Status(int status){
            this.status = status;
        }

        public int getAsInt(){
            return status;
        }
    }
}
