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
