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
package net.redstonelamp.math;

/**
 * Created by jython234 on 9/6/2015.
 *
 * @author RedstoneLamp Team
 */
public class Vector3 {
    public int x = 0;
    public int y = 0;
    public int z = 0;

    public Vector3() {}

    public Vector3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public int getFloorX() {
        return (int) Math.floor(x);
    }

    public int getFloorY() {
        return (int) Math.floor(y);
    }

    public int getFloorZ() {
        return (int) Math.floor(z);
    }

    public int getRight() {
        return x;
    }

    public int getUp() {
        return y;
    }

    public int getForward() {
        return z;
    }

    public int getSouth() {
        return x;
    }

    public int getWest() {
        return z;
    }

    public Vector3 add(int x, int y, int z) {
        return new Vector3(this.x + x, this.y + y, this.z + z);
    }

    public Vector3 subtract(int x, int y, int z) {
        return add(-x, -y, -z);
    }

    public Vector3 multiply(int number) {
        return new Vector3(x * number, y * number, z * number);
    }

    public Vector3 divide(int number) {
        return new Vector3(x / number, y / number, z / number);
    }

    public Vector3 ceil() {
        return new Vector3((int) Math.ceil(x), (int) Math.ceil(y), (int) Math.ceil(z));
    }

    public Vector3 floor() {
        return new Vector3((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
    }

    public Vector3 round() {
        return new Vector3((int) Math.round(x), (int) Math.round(y), (int) Math.round(z));
    }

    public Vector3 abs() {
        return new Vector3(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public Vector3 getSide(int side, int step) {
        switch((int) side) {
            case Side.DOWN:
                return new Vector3(x, y - step, z);
            case Side.UP:
                return new Vector3(x, y + step, z);
            case Side.NORTH:
                return new Vector3(x, y, z - step);
            case Side.SOUTH:
                return new Vector3(x, y, z + step);
            case Side.WEST:
                return new Vector3(x - step, y, z);
            case Side.EAST:
                return new Vector3(x + step, y, z);
            default:
                return this;
        }
    }

    public static int getOppositeSide(int side) {
        switch((int) side){
            case Side.DOWN:
                return Side.UP;
            case Side.UP:
                return Side.DOWN;
            case Side.NORTH:
                return Side.SOUTH;
            case Side.SOUTH:
                return Side.NORTH;
            case Side.WEST:
                return Side.EAST;
            case Side.EAST:
                return Side.WEST;
            default:
                return -1;
        }
    }

    public double distance(Vector3 pos) {
        return Math.sqrt(distanceSquared(pos));
    }

    public double distanceSquared(Vector3 pos) {
        return Math.pow(x - pos.x, 2) + Math.pow(y - pos.y, 2) + Math.pow(z - pos.z, 2);
    }

    public int maxPlainDistance(int x, int z) {
        return Math.max(Math.abs(this.x - x), Math.abs(this.z - z));
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public int lengthSquared() {
        return x * x + y * y + z * z;
    }

    public Vector3 normalize() {
        int len = lengthSquared();
        if(len > 0)
            return divide((int) Math.sqrt(len));
        return new Vector3(0, 0, 0);
    }

    public int dot(Vector3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vector3 cross(Vector3 v) {
        return new Vector3(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
        );
    }

    public boolean equals(Vector3 v) {
        return x == v.x && y == v.y && z == v.z;
    }

    public Vector3 getIntermediateWithXValue(Vector3 v, int x) {
        int xDiff = v.x - this.x;
        int yDiff = v.y - y;
        int zDiff = v.z - z;
        if((xDiff * xDiff) < 0.0000001)
            return null;
        int f = (x - this.x) / xDiff;
        if(f < 0 || f > 1)
            return null;
        else
            return new Vector3(this.x + xDiff * f, y + yDiff * f, z + zDiff * f);
    }

    public Vector3 getIntermediateWithYValue(Vector3 v, int y) {
        int xDiff = v.x - x;
        int yDiff = v.y - this.y;
        int zDiff = v.z - z;
        if((yDiff * yDiff) < 0.0000001)
            return null;
        int f = (y - this.y) / yDiff;
        if(f < 0 || f > 1)
            return null;
        else
            return new Vector3(x + xDiff * f, this.y + yDiff * f, z + zDiff * f);
    }

    public Vector3 getIntermediateWithZValue(Vector3 v, int z){
        int xDiff = v.x - x;
        int yDiff = v.y - y;
        int zDiff = v.z - this.z;
        if((zDiff * zDiff) < 0.0000001)
            return null;
        int f = (z - this.z) / zDiff;
        if(f < 0 || f > 1)
            return null;
        else
            return new Vector3(x + xDiff * f, y + yDiff * f, this.z + zDiff * f);
    }

    public Vector3 setComponents(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public String toString(){
        return "Vector3(X=" + x + ", Y=" + y + ", Z=" + z + ")";
    }
}
