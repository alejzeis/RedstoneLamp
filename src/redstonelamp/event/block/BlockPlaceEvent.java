package redstonelamp.event.block;

import redstonelamp.event.Cancellable;
import redstonelamp.event.Event;
import redstonelamp.level.location.Location;

public class BlockPlaceEvent extends Event implements Cancellable {
	private int blockId;
	private Location location;
	private Object cause;
	private boolean canceled;
	
	public BlockPlaceEvent(int blockId, Location location, Object cause) {
		this.blockId = blockId;
		this.location = location;
		this.cause = cause;
	}
	
	public int getBlock() {
		return this.blockId;
	}
	
	public double getX() {
		return this.location.getX();
	}
	
	public double getY() {
		return this.location.getY();
	}
	
	public double getZ() {
		return this.location.getZ();
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public Object getCause() {
		return this.cause;
	}

	@Override
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	@Override
	public boolean isCanceled() {
		return this.canceled;
	}
}
