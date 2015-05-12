package raknet.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;

public class AddPlayerPacket extends Packet {

	private long clientID;
	private String username;
	private int eid;
	private int x;
	private int y;
	private int z;
	private int pitch;
	private int yaw;
	public  short item;
	public  short meta;


	public boolean slim = false;
	public String skin = null;

    private Player player;
    
	public AddPlayerPacket(DatagramPacket p, Player player ) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		clientID = player.clientID;
		username = player.name;
		
		eid      = b.getInt();
		x        = b.getInt();
		y        = b.getInt();
		z        = b.getInt();
		
		yaw      = b.getInt();
		pitch    = b.getInt();
		item     = b.getShort();
		meta     = b.getShort();
		
		this.player = player;
	}
	
	@Override
	public ByteBuffer getPacket() {
		int skinSize    = skin.length();
		int tot         = username.length() + skinSize + 45; 
		ByteBuffer b    = ByteBuffer.allocate(tot);
		b.put((byte)0x89);
		b.put(username.getBytes());
		b.putLong(clientID);
		b.putInt(eid);
		b.putInt(x);
		b.putInt(y);
		b.putInt(z);
		b.putInt( yaw );
		b.putInt(pitch);
		b.putShort(item);
		b.putShort(meta);
		b.put(slim ? (byte)1 : (byte)0);
		b.put(skin.getBytes());
		return b;
	}

	@Override
	public void process(PacketHandler h) {
		h.sendPacket(getPacket());
		player.x = x;
		player.y = y;
		player.z = z;
	}

}
