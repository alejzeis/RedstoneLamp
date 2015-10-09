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

    public CallableTask(String methodName, Object instance){
        this.instance = instance;
        try{
            method = instance.getClass().getMethod(methodName, long.class);
        }catch(NoSuchMethodException e){
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void onRun(long tick){
        try{
            method.invoke(instance, tick);
        }catch(IllegalAccessException | InvocationTargetException e){
            System.err.println("Error while executing CallableTask!");
            e.getCause().printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "CallableTask{instance: "+instance.getClass()+", method: "+method+"}";
    }
}
