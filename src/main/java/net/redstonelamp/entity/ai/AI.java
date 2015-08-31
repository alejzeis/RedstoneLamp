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
package net.redstonelamp.entity.ai;

/**
 * Represents an AI
 */
public interface AI {
    /**
     * Changes the range that a mob can see
     * 
     * @param range
     */
    public void setRange(int range);
    
    /**
     * Returns the mobs range
     * 
     * @return
     */
    public int getRange();
    
    /**
     * Returns true if the mob is busy doing something
     * 
     * @return
     */
    public boolean isOccupied();
    
    /**
     * Sets if the mob is busy or not
     * 
     * @param occupied
     */
    public void setOccupied(boolean occupied);
    
    /**
     * Calculates if a player is within the mobs range
     * 
     * @return
     */
    public boolean playerInRange();
    
    /**
     * Returns true if a player is close enough to attack
     * 
     * @return
     */
    public boolean canAttack();
    
    /**
     * Calculates if the player is at a position
     * 
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean playerAt(double x, double y, double z);
}
