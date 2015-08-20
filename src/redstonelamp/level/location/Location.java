package redstonelamp.level.location;

import redstonelamp.RedstoneLamp;
import redstonelamp.io.Storable;
import redstonelamp.level.Level;
import redstonelamp.utils.DynamicByteBuffer;

import java.nio.ByteOrder;

/**
 * Represents a location in a world.
 */
public class Location implements Storable{
    private double x = 0;
    private double y = 0;
    private double z = 0;

    private float yaw = 0;
    private float pitch = 0;

    private Level level;

    public Location(double x, double y, double z, Level level){
        this.x = x;
        this.y = y;
        this.z = z;
        this.level = level;
    }

    public Location(double x, double y, double z, float yaw, float pitch, Level level){
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.level = level;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public byte[] store() {
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance(ByteOrder.LITTLE_ENDIAN);
        bb.putDouble(x);
        bb.putDouble(y);
        bb.putDouble(z);
        bb.putFloat(yaw);
        bb.putFloat(pitch);
        bb.putString(level.getName());
        return bb.toArray();
    }

    @Override
    public void load(byte[] source) {
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance(source, ByteOrder.LITTLE_ENDIAN);
        x = bb.getDouble();
        y = bb.getDouble();
        z = bb.getDouble();
        yaw = bb.getFloat();
        pitch = bb.getFloat();
        level = RedstoneLamp.getServerInstance().getMainLevel(); //TODO: Set correct level.
    }
}
