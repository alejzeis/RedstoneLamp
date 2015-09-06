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
package net.redstonelamp.response;

import net.redstonelamp.block.Block;
import net.redstonelamp.level.position.BlockPosition;

/**
 * A Response to place a block.
 *
 * @author RedstoneLamp Team
 */
public class BlockPlaceResponse extends Response{
    public static final boolean DEFAULT_placeAllowed = true;

    public boolean placeAllowed = DEFAULT_placeAllowed;
    public Block block;
    public BlockPosition position;

    public BlockPlaceResponse(Block block, BlockPosition position) {
        this.block = block;
        this.position = position;
    }
}
