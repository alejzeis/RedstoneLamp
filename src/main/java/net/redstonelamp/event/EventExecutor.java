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
package net.redstonelamp.event;

import java.lang.reflect.Method;

import javax.script.Invocable;
import javax.script.ScriptException;

import net.redstonelamp.RedstoneLamp;

public class EventExecutor {
    public static void throwEvent(Event e) {
        for(EventListener l : RedstoneLamp.SERVER.getEventManager().getListeners()) {
            l.onEvent(e);
            Method[] methods = l.getClass().getDeclaredMethods();
            for(Method method : methods) {
                method.setAccessible(true);
                Class<?>[] params = method.getParameterTypes();
                if(params.length == 1) {
                    if(params[0].equals(e.getClass())) {
                        try {
                            method.invoke(l, e);
                        } catch(Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            for(Invocable i : RedstoneLamp.SERVER.getScriptManager().getScripts()) {
                try {
                    String eventName = "on" + e.getClass().getSimpleName();
                    i.invokeFunction(eventName, e.getClass());
                } catch (NoSuchMethodException ex) {} catch (ScriptException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
