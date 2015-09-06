package net.redstonelamp.request;

import net.redstonelamp.level.position.Position;

/**
 * Created by gunnar on 06.09.15.
 */
public class UseItemRequest extends Request {
    private final int x, y, z;
    private final byte face;
    private final short item, meta;
    private final float fx, fy, fz;
    private final float positionX, positionY, positionZ;

    public UseItemRequest(int x, int y, int z, byte face, short item, short meta, float fx, float fy, float fz, float positionX, float positionY, float positionZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.face = face;
        this.item = item;
        this.meta = meta;
        this.fx = fx;
        this.fy = fy;
        this.fz = fz;
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public byte getFace() {
        return face;
    }

    public short getItem() {
        return item;
    }

    public short getMeta() {
        return meta;
    }

    public float getFx() {
        return fx;
    }

    public float getFy() {
        return fy;
    }

    public float getFz() {
        return fz;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public float getPositionZ() {
        return positionZ;
    }

    @Override
    public void execute() {

    }

    @Override
    public String toString() {
        return "UseItemRequest{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", face=" + face +
                ", item=" + item +
                ", meta=" + meta +
                ", fx=" + fx +
                ", fy=" + fy +
                ", fz=" + fz +
                ", positionX=" + positionX +
                ", positionY=" + positionY +
                ", positionZ=" + positionZ +
                '}';
    }
}
