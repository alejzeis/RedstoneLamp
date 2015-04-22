package redstonelamp.utils;

import java.nio.ByteBuffer;

public class Hex {
	public static String getHexString(byte b) {
		String hex = Integer.toHexString(0xFF & b);
		String hexString = "";
		if(hex.length() == 1) {
			hexString += "0";
		}
		hexString += hex;
		return hexString;
	}
	
	public static String getHexString(byte[] b, boolean w) {
		String str = "";
		for(int i = 0; i < b.length; i++) {
			if(i != 0 && w) {
				str += " ";
			}
			str += getHexString(b[i]);
		}
		return str;
	}
	
	public static byte[] intToBytes(int x, int n) {
		byte[] bytes = new byte[n];
		for(int i = 0; i < n; i++, x >>>= 8) {
			bytes[i] = (byte) (x & 0xFF);
		}
		return bytes;
	}
	
	public static String bytesToString(ByteBuffer bb, short nameLength) {
		byte[] b = new byte[nameLength];
		bb.get(b);
		return new String(b);
	}
	
	public static byte[] shortToByte(short s) {
		byte[] r = new byte[2];
		r[0] = (byte) ((s & 0xFF00) >> 8);
		r[1] = (byte) ((s & 0x00FF));
		return r;
	}
	
	public static byte[] intToBytesB(int i) {
		byte[] r = new byte[4];
		r[0] = (byte) ((i & 0xFF000000) >> 24);
		r[1] = (byte) ((i & 0x00FF0000) >> 16);
		r[2] = (byte) ((i & 0x0000FF00) >> 8);
		r[3] = (byte) (i & 0x000000FF);
		return r;
	}
}
