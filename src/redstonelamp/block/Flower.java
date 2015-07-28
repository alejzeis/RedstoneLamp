/*
    Copyright (C) 2015  RedstoneLamp Project

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package redstonelamp.block;

/**
 * A flower.
 */
public class Flower extends Flowable{
    public final static short TYPE_POPPY = 0;
    public final static short TYPE_BLUE_ORCHID = 1;
    public final static short TYPE_ALLIUM = 2;
    public final static short TYPE_AZURE_BLUET = 3;
    public final static short TYPE_RED_TULIP = 4;
    public final static short TYPE_ORANGE_TULIP = 5;
    public final static short TYPE_WHITE_TULIP = 6;
    public final static short TYPE_PINK_TULIP = 7;
    public final static short TYPE_OXEYE_DAISY = 8;

    public Flower(int id, short metadata, int count) {
        super(id, metadata, count);
    }
}
