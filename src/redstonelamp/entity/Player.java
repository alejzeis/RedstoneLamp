package redstonelamp.entity;

import java.net.InetSocketAddress;

public interface Player {
	public String getDisplayName();
	
	public void setDisplayName(String name);
	
	public String getPlayerListName();
	
	public void setPlayerListName(String name);
	
	public void setCompassTarget(Location loc);
	
	public Location getCompassTarget();
	
	public InetSocketAddress getAddress();
	
	public void sendRawMessage(String message);
	
	public void kickPlayer(String message);
	
	public void chat(String msg);
	
	public boolean performCommand(String command);
	
	public boolean isSneaking();
	
	public void setSneaking(boolean sneak);
	
	public boolean isSprinting();
	
	public void setSprinting(boolean sprinting);
	
	public void saveData();
	
	public void loadData();
	
	public void setSleepingIgnored(boolean isSleeping);
	
	public boolean isSleepingIgnored();
	
	@Deprecated
	public void playNote(Location loc, byte instrument, byte note);
	
	public double getHealthScale();
}
