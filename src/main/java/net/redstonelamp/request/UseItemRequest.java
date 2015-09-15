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
package net.redstonelamp.request;

import net.redstonelamp.level.position.Position;
import net.redstonelamp.math.Vector3;

/**
 * Represents a Request to use an item.
 *
 * @author RedstoneLamp Team
 */
@Deprecated
public class UseItemRequest extends Request{

    public Vector3 blockVector;
    public byte face;
    public Position fPosition;

    public UseItemRequest(Vector3 blockVector, byte face, Position fPosition){
        this.blockVector = blockVector;
        this.face = face;
        this.fPosition = fPosition;
    }

    @Override
    public void execute(){

    }

    @Override
    public String toString(){
        return "UseItemRequest{" +
                blockVector.toString() + ", " +
                "face: " + face + ", " +
                fPosition.toString() +
                '}';
    }
}
