package net.redstonelamp.ticker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Represents a task that uses reflection to call a method from an object
 *
 * @author RedstoneLamp Team
 */
public class CallableTask implements Task{
    private final Object instance;
    private final Method method;

    public CallableTask(String methodName, Object instance) {
        this.instance = instance;
        try {
            method = instance.getClass().getMethod(methodName, long.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void onRun(long tick) {
        try {
            method.invoke(instance, tick);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
