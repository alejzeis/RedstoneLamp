/*
    Copyright (C) 2015  RedstoneLamp Project

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package redstonelamp.network.packet;

import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * Created by jython234 on 7/28/2015.
 */
public class AdventureSettingsPacket extends DataPacket{
    public final static byte ID = PENetworkInfo.ADVENTURE_SETTINGS_PACKET;

    public int flags;

    @Override
    public byte getPID() {
        return PENetworkInfo.ADVENTURE_SETTINGS_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putInt(flags);
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }
}
