package raknet.packets;

import redstonelamp.utils.MinecraftPacket;
import redstonelamp.utils.NetworkUtils;
import sun.nio.ch.Net;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * RakNet ID_CUSTOM_PACKET (0x80-0x8F)
 * @author jython234
 */
public class CustomPacket {
    public int sequenceNumber;
    public ArrayList<EncapsulatedPacket> packets;

    public static CustomPacket fromBuffer(byte[] buffer){
        CustomPacket cp = new CustomPacket();
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        bb.get(); //PID
        cp.sequenceNumber = NetworkUtils.readLTriad(bb);
        while(bb.hasRemaining()){
            cp.packets.add(EncapsulatedPacket.fromBuffer(bb));
        }
        return cp;
    }

    public byte[] toBytes(){
        ByteBuffer bb = ByteBuffer.allocate(getLength());
        bb.put((byte) MinecraftPacket.RakNetReliability);
        NetworkUtils.writeLTriad(sequenceNumber, bb);
        for(EncapsulatedPacket ep : packets){
            ep.append(bb);
        }
        return bb.array();
    }

    public int getLength(){
        int len = 4;
        for(EncapsulatedPacket ep : packets){
            len = len + ep.getLength();
        }
        return len;
    }

    public static class EncapsulatedPacket{
        public byte[] buffer;
        public byte reliability;
        public boolean hasSplit;
        public int messageIndex = -1;
        public int orderIndex = -1;
        public byte orderChannel = (byte)0xff;
        public int splitCount = -1;
        public short splitID = -1;
        public int splitIndex = -1;

        public static EncapsulatedPacket fromBuffer(ByteBuffer bb){
            EncapsulatedPacket ep = new EncapsulatedPacket();
            byte flag = bb.get();
            ep.reliability = (byte) (flag >> 5);
            ep.hasSplit = (flag & 0b00010000) == 16;
            int len = bb.getShort() / 8; //Length is in bits
            //int len = ((bb.getShort() + 7) >> 3); //Uncomment this for faster decoding
            if(ep.reliability == 2 || ep.reliability == 3 || ep.reliability == 4 || ep.reliability == 6 || ep.reliability == 7){
                ep.messageIndex = NetworkUtils.readLTriad(bb);
            }
            if(ep.reliability == 1 || ep.reliability == 3 || ep.reliability == 4 || ep.reliability == 7){
                ep.orderIndex = NetworkUtils.readLTriad(bb);
                ep.orderChannel = bb.get();
            }
            if(ep.hasSplit){
                ep.splitCount = bb.getInt();
                ep.splitID = bb.getShort();
                ep.splitIndex = bb.getInt();
            }
            ep.buffer = new byte[len];
            bb.get(ep.buffer);
            return ep;
        }

        public void append(ByteBuffer bb){
            bb.put((byte) ((reliability << 5) ^ (hasSplit ? 0b0001 : 0x00)));
            bb.putShort((short) (buffer.length * 8)); //Length is in bits
            //bb.putShort((short) (buffer.length << 3)); //Uncomment this for faster encoding
            if(reliability == 0x02 || reliability == 0x03 || reliability == 0x04 || reliability == 0x06 || reliability == 0x07){
                NetworkUtils.writeLTriad(messageIndex, bb);
            }
            if(reliability == 0x01 || reliability == 0x03 || reliability == 0x04 || reliability == 0x07){
                NetworkUtils.writeLTriad(orderIndex, bb);
                bb.put(this.orderChannel);
            }
            if(hasSplit){
                bb.putInt(splitCount);
                bb.putShort(splitID);
                bb.putInt(splitIndex);
            }
            bb.put(buffer);
        }

        public int getLength(){
            return 3 + buffer.length + (messageIndex != -1 ? 3:0) + (orderIndex != -1 ? 4:0) +  (hasSplit ? 10:0);
        }
    }
}
