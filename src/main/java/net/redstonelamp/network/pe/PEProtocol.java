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
package net.redstonelamp.network.pe;

import net.redstonelamp.network.NetworkManager;
import net.redstonelamp.network.Protocol;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.request.Request;

/**
 * Created by jython234 on 8/23/2015.
 *
 * @author RedstoneLamp Team
 */
public class PEProtocol extends Protocol{

    /**
     * Construct new PEProtocol.
     *
     * @param manager
     */
    public PEProtocol(NetworkManager manager) {
        super(manager);
        _interface = new JRakLibInterface(manager.getServer());
    }

    @Override
    public String getName() {
        return "MCPE";
    }

    @Override
    public String getDescription() {
        return "Minecraft: Pocket Edition protocol, version "+PENetworkConst.MCPE_VERSION+" (protocol: "+PENetworkConst.MCPE_PROTOCOL+")";
    }

    @Override
    public Request handlePacket(UniversalPacket packet) {
        getManager().getServer().getLogger().debug("");
        return null;
    }
}
