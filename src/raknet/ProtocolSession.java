package raknet;

import raknet.packets.CustomPacket;
import redstonelamp.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Session class for handling a MCPE client
 */
public class ProtocolSession {

    private int nextSeqNum = 1;
    private int lastSeqNum = -1;
    private int nextMessageIndex = 1;

    private CustomPacket currentQueue = new CustomPacket();
    private ArrayList<Integer> ACKQueue;
    private ArrayList<Integer> NACKQueue;
    private Map<Integer, CustomPacket> recoveryQueue = new HashMap();

    private SocketAddress clientAddress;
    private Server server;

    /**
     * Encapsulate a packet into an Encapsulated Packet, and adds it to the CustomPacket queue.
     * @param buffer Packet buffer.
     * @param reliability The RakNet Encapsulated Packet reliability (use 2 if you don't know what this is)
     */
    public void addToQueue(byte[] buffer, int reliability){
        //TODO: Wrap packet, ie. split and find correct reliability?
        CustomPacket.EncapsulatedPacket ep = new CustomPacket.EncapsulatedPacket();
        ep.buffer = buffer;
        ep.hasSplit = false;
        ep.reliability = (byte) reliability;
        ep.messageIndex = nextMessageIndex;
        nextMessageIndex++;
        synchronized (currentQueue){
            currentQueue.packets.add(ep);
        }
    }

    /**
     * Construct a new ProtocolSession for handling.
     * @param clientAddress The client's address.
     * @param server The Server object.
     */
    public ProtocolSession(SocketAddress clientAddress, Server server) {
        this.clientAddress = clientAddress;
        this.server = server;
    }

    /**
     * INTERNAL METHOD
     * Must be called by ticker, less than 10 ticks in between
     */
    public void updateQueues(){
        //TODO: Called every 5-10 ticks
        synchronized (ACKQueue){
            if(!ACKQueue.isEmpty()) {
                //TODO: Send ACK Packet
            }
        }
        synchronized (NACKQueue){
            if(!NACKQueue.isEmpty()){
                //TODO: Send NACK Packet
            }
        }
        synchronized (currentQueue){
            if(!currentQueue.packets.isEmpty()){
                currentQueue.sequenceNumber = nextSeqNum;
                try {
                    sendPacket(currentQueue.toBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Handle a CustomPacket sent from client.
     * INTERNAL USE ONLY
     * @param cp CustomPacket
     */
    public void handleDataPacket(CustomPacket cp){
        synchronized ((Integer) lastSeqNum){
            if(cp.sequenceNumber - lastSeqNum == 1){
                //All good
                lastSeqNum = cp.sequenceNumber;
                synchronized ((Integer) nextSeqNum){
                    nextSeqNum++;
                }
            } else {
                //Not so good
                if(cp.sequenceNumber - lastSeqNum < 1){
                    //Client is behind
                    int diff = lastSeqNum - cp.sequenceNumber; //To make sure its not negative
                    int num = lastSeqNum;
                    for(int i = 0; i < diff; i++){
                        CustomPacket recoveredPacket = recoveryQueue.get(num);
                        num = num - 1;
                    }
                } else {
                    //We are behind
                    int diff = cp.sequenceNumber - lastSeqNum;
                    int num = cp.sequenceNumber - 1;
                    for(int i = 0; i < diff; i++){
                        synchronized (NACKQueue){
                            NACKQueue.add(num);
                            num = num - 1;
                        }
                    }
                }
            }
        }
        for(CustomPacket.EncapsulatedPacket ep : cp.packets){
            if(ep.messageIndex != -1){
                nextMessageIndex = ep.messageIndex + 1;
            }
            byte pid = ep.buffer[0];
            switch(pid){
                //TODO: Handle packets
            }
        }
    }

    /**
     * Send a RAW packet out. Does not encapsulate into a CustomPacket
     * @param buffer The packet buffer
     * @throws java.io.IOException If the packet can not be sent.
     */
    public void sendPacket(byte[] buffer) throws IOException {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length, clientAddress);
        server.socket.send(dp);
    }
}
