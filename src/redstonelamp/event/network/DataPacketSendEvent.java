package redstonelamp.event.network;

import redstonelamp.Player;
import redstonelamp.event.Cancellable;
import redstonelamp.event.Event;
import redstonelamp.network.packet.DataPacket;

/**
 * Event called whenever a DataPacket is to be sent. If the event is canceled, the packet will not be sent.
 *
 * @author jython234
 */
public class DataPacketSendEvent extends Event implements Cancellable{
    private DataPacket packet;
    private Player player;

    private boolean canceled = false;

    /**
     * Create a new DataPacketSendEvent belonging to the specified <code>DataPacket</code> and <code>Player</code>.
     * @param packet The DataPacket to be sent.
     * @param player The Player the DataPacket is being sent to.
     */
    public DataPacketSendEvent(DataPacket packet, Player player){
        this.packet = packet;
        this.player = player;
    }

    @Override
    public String getEventName() {
        return "DataPacketSendEvent";
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
