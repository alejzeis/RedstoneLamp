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

import net.redstonelamp.block.Block;
import net.redstonelamp.math.Vector3;

/**
 * A Request to place a block at a position.
 *
 * @author RedstoneLamp Team
 */
public class BlockPlaceRequest extends Request{
    public Block block;
    public Vector3 blockPosition;

    public BlockPlaceRequest(Block block, Vector3 blockPosition) {
        this.block = block;
        this.blockPosition = blockPosition;
    }

    @Override
    public void execute() {

    }
}
