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
package net.redstonelamp.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to prevent spamming actions.
 * <br>
 * From BlockServerProject (https://github.com/BlockServerProject/BlockServer/blob/master/src/main/java/org/blockserver/utils/AntiSpam.java)
 *
 * @author PEMapModder
 */
public final class AntiSpam{
    private static Map<String, AntiSpam> map = new HashMap<>();
    private long timeout;

    private AntiSpam(long timeout){
        this.timeout = timeout;
    }

    private boolean canAct(){
        return timeout <= System.nanoTime();
    }

    public static void act(Runnable op, String key, long expireMillis){
        AntiSpam as = map.get(key);
        if(as != null){
            if(!as.canAct()){
                return;
            }
        }
        op.run();
        map.put(key, new AntiSpam(System.nanoTime() + expireMillis * 1000000));
    }

    public static boolean expire(String key){
        return map.remove(key) != null;
    }
}
