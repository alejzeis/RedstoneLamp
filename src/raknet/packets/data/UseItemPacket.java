package raknet.packets.data;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;

public class UseItemPacket extends Packet {
	
	private int entityID;
	private int x, y, z;
	private byte blockData;
	private short blockID;
	private int unknown;
	private float fx, fy, fz;
	private Player player;
	
	public UseItemPacket(DatagramPacket p, Player player) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		b.get();
		x = b.getInt();
		y = b.getInt();
		z = b.getInt();
		unknown = b.getInt();
		blockID = b.getShort();
		blockData = b.get();
		b.getInt();
		fx = b.getFloat();
		fy = b.getFloat();
		fz = b.getFloat();
		this.player = player;
	}
	
	@Override
	public ByteBuffer getPacket() {
		int size = 12 * 8;
		ByteBuffer response = ByteBuffer.allocate(3 + 12);
		response.put((byte) 0x00);
		response.putShort((short) size);
		response.put((byte) 0x97);
		response.putInt(getX());
		response.putInt(getZ());
		response.put((byte) getY());
		response.put((byte) blockID);
		response.put((byte) blockData);
		return response;
	}
	
	private int getX() {
		int r = x;
		if(fx == 1.0f) {
			r += 1;
		} else if(fx == 0.0f) {
			r -= 1;
		}
		return r;
	}
	
	private int getY() {
		int r = y;
		if(fy == 1.0f) {
			r += 1;
		} else if(fy == 0.0f) {
			r -= 1;
		}
		return r;
	}
	
	private int getZ() {
		int r = z;
		if(fz == 1.0f) {
			r += 1;
		} else if(fz == 0.0f) {
			r -= 1;
		}
		return r;
	}
	
	@Override
	public void process(PacketHandler h) {
		if(!currentPos()) {
			getBlockData();
			h.addToQueueForAll(getPacket());
		}
	}
	
	private boolean currentPos() {
		if((int) player.x == getX() && (int) player.y == getY() && (int) player.z == getZ()) { return true; }
		return false;
	}
	
	private void getBlockData() {
		System.out.println("ID: " + blockID + " X: " + fx + " Y: " + fy + " Z: " + fz);
		if(blockID == 50) {
			if(fx == 0.0f) {
				blockData = 2;
			} else if(fx == 1.0f) {
				blockData = 1;
			}
			
			if(fy == 0.0f || fy == 1.0f) {
				blockData = 0;
			}
			
			if(fz == 0.0f) {
				blockData = 4;
			} else if(fz == 1.0f) {
				blockData = 3;
			}
		} else if(blockID == 53 || blockID == 67 || blockID == 108) {
			int a = floor_double((double) ((player.yaw * 4f) / 360f) + 0.5D) & 3;
			
			if(a == 0) {
				blockData = 2;
			}
			if(a == 1) {
				blockData = 1;
			}
			if(a == 2) {
				blockData = 3;
			}
			if(a == 3) {
				blockData = 0;
			}
		}
	}
	
	public static int floor_double(double d) {
		int i = (int) d;
		return d >= (double) i ? i:i - 1;
	}
}
