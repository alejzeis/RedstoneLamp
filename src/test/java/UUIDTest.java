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
import net.redstonelamp.nio.BinaryBuffer;

import java.nio.ByteOrder;
import java.util.UUID;

/**
 * Created by jython234 on 9/5/2015.
 *
 * @author RedstoneLamp Team
 */
public class UUIDTest {

    public static void main(String[] args) {
        UUID id = UUID.nameUUIDFromBytes("jython234".getBytes());
        BinaryBuffer bb = BinaryBuffer.newInstance(16, ByteOrder.LITTLE_ENDIAN);
        bb.putUUID(id);
        byte[] data = bb.toArray();
        BinaryBuffer b2 = BinaryBuffer.wrapBytes(data, ByteOrder.LITTLE_ENDIAN);
        UUID id2 = b2.getUUID();
        System.out.println(id2.toString()+", "+id.toString());
        System.out.println(id2.toString().equals(id.toString()));
    }
}
