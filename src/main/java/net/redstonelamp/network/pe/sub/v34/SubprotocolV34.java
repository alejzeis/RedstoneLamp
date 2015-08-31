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
package net.redstonelamp.network.pe.sub.v34;

import net.redstonelamp.Player;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.pe.sub.PESubprotocolManager;
import net.redstonelamp.network.pe.sub.Subprotocol;
import net.redstonelamp.nio.BinaryBuffer;
import net.redstonelamp.request.LoginRequest;
import net.redstonelamp.request.Request;
import net.redstonelamp.response.Response;
import net.redstonelamp.utils.CompressionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.zip.DataFormatException;

/**
 * A subprotocol implementation for the MCPE version 0.12.1 (protocol 34)
 *
 * @author RedstoneLamp Team
 */
public class SubprotocolV34 extends Subprotocol implements ProtocolConst34{

    public SubprotocolV34(PESubprotocolManager manager){
        super(manager);
    }

    @Override
    public Request[] handlePacket(UniversalPacket up){
        List<Request> requests = new ArrayList<>();
        byte id = up.bb().getByte();
        if(id == BATCH_PACKET){
            return processBatch(up);
        }

        switch(id){
            case LOGIN_PACKET:
                getProtocol().getServer().getLogger().debug("Got Login packet!");
                String username = up.bb().getString();
                up.bb().skip(8); //Skip protocols
                long clientId = up.bb().getLong();
                UUID clientUUid = up.bb().getUUID();
                up.bb().getString(); //server addresss
                String clientSecret = up.bb().getString();
                boolean slim = up.bb().getBoolean();
                byte[] skin = up.bb().get(up.bb().getUnsignedShort());

                LoginRequest lr = new LoginRequest(username, clientUUid);
                lr.clientId = clientId;
                lr.slim = slim;
                lr.skin = skin;
                requests.add(lr);
                break;
        }
        return requests.toArray(new Request[requests.size()]);
    }

    @Override
    public UniversalPacket[] translateResponse(Response response, Player player){
        return new UniversalPacket[0];
    }


    private Request[] processBatch(UniversalPacket up){
        List<Request> requests = new ArrayList<>();
        int len = up.bb().getInt();
        byte[] compressed = up.bb().get(len);
        try{
            byte[] uncompressed = CompressionUtils.zlibInflate(compressed);
            BinaryBuffer bb = BinaryBuffer.wrapBytes(uncompressed, up.bb().getOrder());
            while(bb.getPosition() < uncompressed.length){
                int pkLen = bb.getInt();
                if(pkLen > bb.remaining()){
                    break;
                }

                byte[] data = bb.get(pkLen);
                BinaryBuffer pk = BinaryBuffer.wrapBytes(data, up.bb().getOrder());

                byte id = pk.getByte();
                if(id == BATCH_PACKET){
                    throw new IllegalStateException("BatchPacket found inside BatchPacket!");
                }
                UniversalPacket packet = new UniversalPacket(data, up.bb().getOrder(), up.getAddress());
                requests.addAll(Arrays.asList(handlePacket(packet)));
            }
        }catch(DataFormatException e){
            getProtocol().getManager().getServer().getLogger().warning(e.getClass().getName() + " while handling BatchPacket: " + e.getMessage());
            getProtocol().getManager().getServer().getLogger().trace(e);
        }finally{
            if(!requests.isEmpty()){
                return (Request[]) requests.stream().toArray();
            }
            return new Request[]{null};
        }
    }

    @Override
    public String getMCPEVersion(){
        return MCPE_VERSION;
    }

    @Override
    public int getProtocolVersion(){
        return MCPE_PROTOCOL;
    }
}
