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

    public BlockPosition(Level level) {
        this.level = level;
    }

    public BlockPosition(int x, int y, int z, Level level) {
        super(x, y, z);
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public static BlockPosition fromVector3(Vector3 blockPosition, Level level) {
        BlockPosition pos = new BlockPosition(level);
        pos.x = blockPosition.getX();
        pos.y = blockPosition.getY();
        pos.z = blockPosition.getZ();
        return pos;
    }
}
