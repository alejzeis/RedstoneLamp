package redstonelamp.event.network;

import redstonelamp.Player;
import redstonelamp.event.Cancellable;
import redstonelamp.event.Event;
import redstonelamp.network.packet.DataPacket;

/**
 * Event called whenever a DataPacket is received. If the event is canceled, the <code>Player</code> class will NOT handle the packet.
 *
 * @author jython234
 */
public class DataPacketReceiveEvent extends Event implements Cancellable{
    private DataPacket packet;
    private Player player;

    private boolean canceled = false;

    public DataPacketReceiveEvent(DataPacket packet, Player player){
        this.packet = packet;
        this.player = player;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    @Override
    public String getEventName() {
        return "DataPacketReceiveEvent";
    }
}
