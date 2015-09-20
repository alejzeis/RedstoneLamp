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
package net.redstonelamp.script;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import lombok.Getter;
import lombok.Setter;
import net.redstonelamp.Server;
import net.redstonelamp.ui.ConsoleOut;
import net.redstonelamp.ui.Logger;

public class ScriptManager {
    @Getter public File scriptDir = new File("./scripts");
    
    @Getter public ArrayList<Invocable> scripts = new ArrayList<Invocable>();
    @Getter public HashMap<String, ScriptAPI> scriptAPI = new HashMap<String, ScriptAPI>();
    
    @Getter @Setter public ScriptEngineManager manager;
    @Getter @Setter public ScriptEngine engine;
    
    @Getter public Server server;
    @Getter public ScriptLoader loader;
    
    public ScriptManager(Server server) {
        this.server = server;
        manager = new ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript");
        try {
            Constructor<?> c = server.getLogger().getConsoleOutClass().getConstructor(String.class);
            engine.put("api", new DefaultScriptAPI(server, new Logger((ConsoleOut) c.newInstance("Script"))));
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {}
        loader = new ScriptLoader(this);
    }
    
    public void addScriptAPI(ScriptAPI api) {
        addScriptAPI(api.getClass().getSimpleName().toLowerCase(), api);
    }
    
    public void addScriptAPI(String name, ScriptAPI api) {
        scriptAPI.put(name, api);
    }
    
    public void initScriptAPI() {
        for(ScriptAPI api : getScriptAPI().values()) {
            try {
                engine.put(api.getClass().getSimpleName().toLowerCase(), api.getClass().newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadScripts() {
        if(!scriptDir.isDirectory())
            scriptDir.mkdirs();
        for(File script : scriptDir.listFiles()) {
            loader.loadScript(script);
        }
    }
}
