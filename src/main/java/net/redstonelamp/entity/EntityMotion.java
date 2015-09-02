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
