package net.redstonelamp.network.pe.sub.v34;

import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.network.pe.sub.PESubprotocolManager;
import net.redstonelamp.network.pe.sub.Subprotocol;
import net.redstonelamp.nio.BinaryBuffer;
import net.redstonelamp.request.Request;
import net.redstonelamp.response.Response;
import net.redstonelamp.utils.CompressionUtils;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 * A subprotocol implementation for the MCPE version 0.12.1 (protocol 34)
 *
 * @author RedstoneLamp Team
 */
public class SubprotocolV34 extends Subprotocol implements ProtocolConst34{

    public SubprotocolV34(PESubprotocolManager manager) {
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

    @Override
    public UniversalPacket[] translateResponse(Response response, SocketAddress address) {
        return new UniversalPacket[0];
    }


    private Request[] processBatch(UniversalPacket up) {
        List<Request> requests = new ArrayList<>();
        int len = up.bb().getInt();
        byte[] compressed = up.bb().get(len);
        try {
            byte[] uncompressed = CompressionUtils.zlibInflate(compressed);
            BinaryBuffer bb = BinaryBuffer.wrapBytes(uncompressed, up.bb().getOrder());
            while(bb.getPosition() < uncompressed.length) {
                int pkLen = bb.getInt();
                if(pkLen > bb.remaining()) {
                    break;
                }

                byte[] data = bb.get(pkLen);
                BinaryBuffer pk = BinaryBuffer.wrapBytes(data, up.bb().getOrder());

                byte id = pk.getByte();
                if(id == BATCH_PACKET) {
                    throw new IllegalStateException("BatchPacket found inside BatchPacket!");
                }
                UniversalPacket packet = new UniversalPacket(data, up.bb().getOrder(), up.getAddress());
                requests.addAll(Arrays.asList(handlePacket(packet)));
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
