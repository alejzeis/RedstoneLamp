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
package net.redstonelamp.io;

import net.redstonelamp.nio.BinaryBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;

/**
 * Created by jython234 on 9/5/2015.
 *
 * @author RedstoneLamp Team
 */
public class BinaryBufferInputStream extends InputStream{
    private BinaryBuffer bb;

    public BinaryBufferInputStream(BinaryBuffer bb){
        this.bb = bb;
    }

    @Override
    public int read() throws IOException{
        try{
            return (int) bb.getUnsignedByte();
        }catch(BufferUnderflowException e){
            return -1;
        }
    }

    @Override
    public void close() throws IOException{
        super.close();
        bb = null;
    }
}
