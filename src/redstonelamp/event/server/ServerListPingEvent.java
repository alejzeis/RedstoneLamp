package redstonelamp.event.server;

import redstonelamp.event.Event;
import redstonelamp.utils.ServerIcon;

public class ServerListPingEvent extends Event {
	private String tag = null;
	private int protocol = -1;
	private int maxPlayers = -1;
	private int onlinePlayers = -1;
	private String motd = null;
	private ServerIcon icon = null;

	public void setProtocolTag(String tag) {
		this.tag = tag;
	}

	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	public void setMaxPlayers(int max) {
		this.maxPlayers = max;
	}

	public void setOnlinePlayers(int online) {
		this.onlinePlayers = online;
	}

	public void setMotd(String motd) {
		this.motd = motd;
	}

	public void setIcon(ServerIcon icon) {
		this.icon = icon;
	}

	public String getProtocolTag() {
		return this.tag;
	}

	public int getProtocol() {
		return this.protocol;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public int getOnlinePlayers() {
		return this.onlinePlayers;
	}

	public String getMotd() {
		return this.motd;
	}

	public ServerIcon getIcon() {
		return this.icon;
	}

}
