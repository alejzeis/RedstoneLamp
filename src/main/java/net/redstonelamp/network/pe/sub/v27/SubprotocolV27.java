/**
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
package net.redstonelamp.network.pe.sub.v27;

import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.pe.sub.PESubprotocolManager;
import net.redstonelamp.network.pe.sub.Subprotocol;
import net.redstonelamp.nio.BinaryBuffer;
import net.redstonelamp.request.Request;
import net.redstonelamp.utils.CompressionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 * A subprotocol implementation for the MCPE version 0.11.1 (protocol 27)
 *
 * @author RedstoneLamp Team
 */
public class SubprotocolV27 extends Subprotocol implements ProtocolConst27{

    public SubprotocolV27(PESubprotocolManager manager) {
        super(manager);
    }

    @Override
    public Request[] handlePacket(UniversalPacket up) {
        byte id = up.bb().getByte();
        if(id == BATCH_PACKET) {
            return processBatch(up);
        }

        switch (id) {
            case LOGIN_PACKET:
                getProtocol().getServer().getLogger().debug("Got Login packet!");
                break;
        }
        return new Request[] {null};
    }


    private Request[] processBatch(UniversalPacket up) {
        List<Request> requests = new ArrayList<>();
        int len = up.bb().getInt();
        byte[] compressed = up.bb().get(len);
        try {
            byte[] uncompressed = CompressionUtils.zlibInflate(compressed);
            BinaryBuffer bb = BinaryBuffer.wrapBytes(uncompressed, up.bb().getOrder());
            while(bb.getPosition() < uncompressed.length) {
                byte id = bb.getByte();
                if(id == BATCH_PACKET) {
                    throw new IllegalStateException("BatchPacket found inside BatchPacket!");
                }
                int start = bb.getPosition();
                UniversalPacket pk = new UniversalPacket(uncompressed, up.bb().getOrder(), up.getAddress());
                pk.bb().setPosition(start);
                requests.add(handlePacket(pk)[0]);
                bb.setPosition(pk.bb().getPosition());
            }
        } catch (DataFormatException e) {
            getProtocol().getManager().getServer().getLogger().warning(e.getClass().getName()+" while handling BatchPacket: "+e.getMessage());
            getProtocol().getManager().getServer().getLogger().trace(e);
        } finally {
            if(!requests.isEmpty()) {
                return (Request[]) requests.stream().toArray();
            }
            return new Request[] {null};
        }
    }

    @Override
    public String getMCPEVersion() {
        return MCPE_VERSION;
    }

    @Override
    public int getProtocolVersion() {
        return MCPE_PROTOCOL;
    }
}
