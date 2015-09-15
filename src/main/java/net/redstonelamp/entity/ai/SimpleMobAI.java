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
    public Entity mob;
    public int range;
    public boolean occupied = false;
    public Position position;

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
        this.mob = mob;
        this.range = range;
        this.position = position;
    }

    @Override
    public void setRange(int range){
        this.range = range;
    }

    @Override
    public int getRange(){
        return range;
    }

    @Override
    public boolean isOccupied(){
        return occupied;
    }

    @Override
    public void setOccupied(boolean occupied){
        this.occupied = occupied;
    }

    @Override
    public boolean playerInRange(){
        if(playerAt(mob.getPosition().getX(), mob.getPosition().getY(), mob.getPosition().getZ()))
            return true;
        for(double x = 0; x < range + 1; x++){
            x = position.getX() + x;
            if(playerAt(x, mob.getPosition().getY(), mob.getPosition().getZ()))
                return true;
            for(double z = 0; z < range + 1; z++){
                z = position.getX() + z;
                if(playerAt(x, mob.getPosition().getY(), z))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean canAttack(){
        if(playerAt(mob.getPosition().getX(), mob.getPosition().getY(), mob.getPosition().getZ()))
            return true;
        else if(playerAt(mob.getPosition().getX(), mob.getPosition().getY(), mob.getPosition().getZ() + 1))
            return true;
        else if(playerAt(mob.getPosition().getX() + 1, mob.getPosition().getY(), mob.getPosition().getZ()))
            return true;
        else if(playerAt(mob.getPosition().getX() + 1, mob.getPosition().getY(), mob.getPosition().getZ() + 1))
            return true;
        return false;
    }

    @Override
    public boolean playerAt(double x, double y, double z){
        for(Player p : RedstoneLamp.SERVER.getPlayers()){
            if(p.getPosition().getX() == x)
                if(p.getPosition().getY() == y)
                    if(p.getPosition().getZ() == z)
                        if(p.getPosition().getLevel() == mob.getPosition().getLevel())
                            return true;
        }
        return false;
    }
}
