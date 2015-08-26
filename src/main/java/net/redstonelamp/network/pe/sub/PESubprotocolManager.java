/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.redstonelamp.network.pe.sub;

import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.pe.PEProtocol;
import net.redstonelamp.nio.BinaryBuffer;
import net.redstonelamp.utils.CompressionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.DataFormatException;

/**
 * Manager for different MCPE subprotocols
 *
 * @author RedstoneLamp Team
 */
public class PESubprotocolManager{
    private final PEProtocol protocol;
    private final Map<Integer, Subprotocol> subprotocols = new ConcurrentHashMap<>();

    public PESubprotocolManager(PEProtocol protocol){
        this.protocol = protocol;
    }

    public void registerSubprotocol(Subprotocol subprotocol){
        subprotocols.put(subprotocol.getProtocolVersion(), subprotocol);
    }

    /**
     * Finds a subprotocol for the specified LoginPacket, and then handles the LoginPacket
     *
     * @param up The UniversalPacket that contains the LoginPacket
     * @return The correct Subprotocol, or null if none are found
     */
    public Subprotocol findSubprotocol(UniversalPacket up){
        byte id = up.bb().getByte();
        switch(id){
            case (byte) 0xb1: //Batch Packet ID BEFORE protocol 34
                byte[] compressedData = up.bb().get(up.bb().getInt());
                try{
                    byte[] uncompressedData = CompressionUtils.zlibInflate(compressedData);
                    return findSubprotocol(new UniversalPacket(uncompressedData, up.bb().getOrder(), up.getAddress()));
                }catch(DataFormatException e){
                    protocol.getManager().getServer().getLogger().error(e.getClass().getName() + " while processing BatchPacket 0xb1!");
                    protocol.getManager().getServer().getLogger().trace(e);
                }
                break;

            case (byte) 0x92: //Batch Packet ID AFTER protocol 34
                compressedData = up.bb().get(up.bb().getInt());
                try{
                    byte[] uncompressedData = CompressionUtils.zlibInflate(compressedData);
                    BinaryBuffer bb = BinaryBuffer.wrapBytes(uncompressedData, up.bb().getOrder());
                    int pkLen = bb.getInt();
                    return findSubprotocol(new UniversalPacket(bb.get(pkLen), up.bb().getOrder(), up.getAddress()));
                }catch(DataFormatException e){
                    protocol.getManager().getServer().getLogger().error(e.getClass().getName() + " while processing BatchPacket 0xb1!");
                    protocol.getManager().getServer().getLogger().trace(e);
                }
                break;

            case (byte) 0x82: //Login Packet ID BEFORE protocol 34
                up.bb().getString(); //Username
                int protocol1 = up.bb().getInt();
                int protocol2 = up.bb().getInt();
                if(!(subprotocols.containsKey(protocol1) || subprotocols.containsKey(protocol2))){
                    return null;
                }else{
                    if(subprotocols.containsKey(protocol1)){
                        return subprotocols.get(protocol1);
                    }else{
                        return subprotocols.get(protocol2);
                    }
                }
        }
        return null;
    }

    public PEProtocol getProtocol(){
        return protocol;
    }
}
