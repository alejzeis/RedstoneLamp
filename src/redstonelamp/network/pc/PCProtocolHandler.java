package redstonelamp.network.pc;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.simple.JSONObject;

import redstonelamp.DesktopPlayer;
import redstonelamp.Player;
import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.event.EventExecutor;
import redstonelamp.event.server.ServerListPingEvent;
import redstonelamp.network.pc.packet.MinecraftPacket;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.network.pc.packet.handshake.HandshakePacket;
import redstonelamp.network.pc.packet.status.StatusResponse;

/**
 * Protocol handler.
 */
public class PCProtocolHandler extends IoHandlerAdapter {
	private PCInterface pcInterface;

	public PCProtocolHandler(PCInterface pcInterface) {
		this.pcInterface = pcInterface;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		MinecraftPacket pkt = (MinecraftPacket) message;

		switch (pkt.packetID) {
			case PCNetworkInfo.HANDHSAKE_HANDSHAKE: // Since the status request, and
													// handshake have the same id,
													// we must check the length
				if (pkt.payload.length < 15) { // It's a status request.
					sendStatusReply(session);
				} else { // It's a handshake
					HandshakePacket hp = new HandshakePacket();
					hp.decode(pkt.payload);
					if (hp.nextState == HandshakePacket.STATE_LOGIN) {
						DesktopPlayer player = new DesktopPlayer(pcInterface, pcInterface.getServer(), session);
						pcInterface.getServer().addPlayer(player);
					} // Send status reply once we get the status request
				}
				break;
		}

		Player player = pcInterface.getServer().getPlayer(session.getRemoteAddress().toString());
		if (player instanceof DesktopPlayer) {
			PCDataPacket packet = pcInterface.getPacket(pkt.packetID);
			if(packet != null) {
				packet.decode(pkt.payload);
				player.handleDataPacket(packet);
			} else {
				pcInterface.getServer().getLogger().warning("[PCProtocolHandler]: Dropped packet "+String.format("%02X", pkt.packetID));
			}
		} else { // No player class

		}
	}

	@SuppressWarnings("unchecked")
	private void sendStatusReply(IoSession session) {
		Server server = pcInterface.getServer();
		EventExecutor executor = server.getEventManager().getEventExecutor();

		JSONObject root = new JSONObject();
		JSONObject version = new JSONObject();
		JSONObject players = new JSONObject();
		JSONObject description = new JSONObject();

		ServerListPingEvent event = new ServerListPingEvent();
		event.setProtocolTag(RedstoneLamp.SOFTWARE + " "
				+ PCNetworkInfo.MC_VERSION);
		event.setProtocol(PCNetworkInfo.MC_PROTOCOL);
		event.setMaxPlayers(server.getMaxPlayers());
		event.setOnlinePlayers(server.getOnlinePlayers().size());
		event.setMotd(server.getMotd());
		event.setIcon(server.getIcon());
		executor.execute(event);

		version.put("name", event.getProtocolTag());
		version.put("protocol", event.getProtocol());

		players.put("max", event.getMaxPlayers());
		players.put("online", event.getOnlinePlayers());

		description.put("text", event.getMotd());

		root.put("version", version);
		root.put("players", players);
		root.put("description", description);

		ServerIcon icon = event.getIcon();
		if (icon != null)
			root.put("favicon", icon.toString());

		StatusResponse response = new StatusResponse();
		response.jsonResponse = root.toJSONString();
		session.write(response);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		System.out.println("IDLE " + session.getIdleCount(status));
	}
}
