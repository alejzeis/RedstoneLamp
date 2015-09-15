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
package net.redstonelamp.level.position;

import net.redstonelamp.level.Level;
import net.redstonelamp.math.Vector3;

/**
 * Created by jython234 on 9/6/2015.
 *
 * @author RedstoneLamp Team
 */
public class BlockPosition extends Vector3{
    private final Level level;

    public BlockPosition(Level level){
        this.level = level;
    }

    public BlockPosition(int x, int y, int z, Level level){
        super(x, y, z);
        this.level = level;
    }

    public Level getLevel(){
        return level;
    }

    public static BlockPosition fromVector3(Vector3 blockPosition, Level level){
        BlockPosition pos = new BlockPosition(level);
        pos.x = blockPosition.getX();
        pos.y = blockPosition.getY();
        pos.z = blockPosition.getZ();
        return pos;
    }
}
