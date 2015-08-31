package net.redstonelamp.entity;

public class EntityMotion{
    private double dx;
    private double dy;
    private double dz;
    /**
     * Warning: this variable is in <em>radians</em>!
     */
    private double yaw;
    private double pitch;
    private double speed;
    public EntityMotion(double dx, double dy, double dz, double yaw, double pitch, double speed){
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.yaw = yaw;
        this.pitch = pitch;
        this.speed = speed;
    }
    public static EntityMotion fromXYZ(double x, double y, double z){
        double v = Math.sqrt(x * x + y * y + z * z);
        double pitch = Math.asin(-y / v);
        double yaw = Math.asin(z / v / Math.cos(pitch));
        return new EntityMotion(x, y, z, yaw, pitch, v);
    }
    public static EntityMotion fromYPS(double yaw, double pitch, double speed){
        return fromYPS(yaw, pitch, speed, false);
    }
    public static EntityMotion fromYPS(double yaw, double pitch, double v, boolean inRadians){
        if(!inRadians){
            yaw *= Math.PI / 180;
            pitch *= Math.PI / 180;
        }
        double y = -Math.sin(pitch) * v;
        double h = Math.cos(pitch) * v;
        double x = -h * Math.sin(yaw);
        double z = h * Math.cos(yaw);
        return new EntityMotion(x, y, z, yaw, pitch, v);
    }
}
