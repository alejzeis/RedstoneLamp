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

import net.redstonelamp.Player;
import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.entity.Entity;
import net.redstonelamp.level.position.Position;

/**
 * A Simple implementation of AI for mobs.
 *
 * @author RedstoneLamp Team
 */
public class SimpleMobAI implements AI{
    public static Entity MOB;
    public static int RANGE;
    public static boolean OCCUPIED = false;
    public static Position POSITION;

    /**
     * Creates an instance of Mob AI with a set view range
     *
     * NOTE:
     * - Passive Mobs: 0 Blocks
     * - Most Monsters: 16 Blocks
     * - Enderman: 64 Blocks
     * - Ghast: 100 Blocks
     *
     * @param range
     */
    public SimpleMobAI(Entity mob, int range, Position position){
        MOB = mob;
        RANGE = range;
        POSITION = position;
    }

    @Override
    public void setRange(int range){
        RANGE = range;
    }

    @Override
    public int getRange(){
        return RANGE;
    }

    @Override
    public boolean isOccupied(){
        return OCCUPIED;
    }

    @Override
    public void setOccupied(boolean occupied){
        OCCUPIED = occupied;
    }

    @Override
    public boolean playerInRange(){
        if(playerAt(MOB.getPosition().getX(), MOB.getPosition().getY(), MOB.getPosition().getZ()))
            return true;
        for(double x = 0; x < RANGE + 1; x++){
            x = POSITION.getX() + x;
            if(playerAt(x, MOB.getPosition().getY(), MOB.getPosition().getZ()))
                return true;
            for(double z = 0; z < RANGE + 1; z++){
                z = POSITION.getX() + z;
                if(playerAt(x, MOB.getPosition().getY(), z))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean canAttack(){
        if(playerAt(MOB.getPosition().getX(), MOB.getPosition().getY(), MOB.getPosition().getZ()))
            return true;
        else if(playerAt(MOB.getPosition().getX(), MOB.getPosition().getY(), MOB.getPosition().getZ() + 1))
            return true;
        else if(playerAt(MOB.getPosition().getX() + 1, MOB.getPosition().getY(), MOB.getPosition().getZ()))
            return true;
        else if(playerAt(MOB.getPosition().getX() + 1, MOB.getPosition().getY(), MOB.getPosition().getZ() + 1))
            return true;
        return false;
    }

    @Override
    public boolean playerAt(double x, double y, double z){
        for(Player p : RedstoneLamp.SERVER.getPlayers()){
            if(p.getPosition().getX() == x)
                if(p.getPosition().getY() == y)
                    if(p.getPosition().getZ() == z)
                        if(p.getPosition().getLevel() == MOB.getPosition().getLevel())
                            return true;
        }
        return false;
    }
}
