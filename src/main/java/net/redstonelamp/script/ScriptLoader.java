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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptException;

import org.apache.commons.io.FilenameUtils;

import lombok.Getter;

public class ScriptLoader {
    @Getter private ScriptManager scriptManager;
    
    public ScriptLoader(ScriptManager scriptMgr) {
        scriptManager = scriptMgr;
    }
    
    public void loadScript(File script) {
        if(!scriptManager.scriptDir.isDirectory())
            scriptManager.scriptDir.mkdirs();
        String name = FilenameUtils.removeExtension(script.getName());
        try {
            scriptManager.server.getLogger().info("Loading script " + name + "...");
            Reader reader = new InputStreamReader(new FileInputStream(script));
            scriptManager.engine.eval(reader);
            Invocable inv = (Invocable) scriptManager.engine;
            scriptManager.getScripts().add(inv);
            inv.invokeFunction("onEnable");
        } catch (FileNotFoundException e) {
            scriptManager.server.getLogger().info("Unable to locate script file " + name + "!");
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {}
    }
    
    public void disableScript(Invocable script) {
        if(!scriptManager.scriptDir.isDirectory())
            scriptManager.scriptDir.mkdirs();
        try {
            for(int i = 0; i < scriptManager.getScripts().size() + 1; i++) {
                Invocable inv = scriptManager.getScripts().get(i);
                if(inv.equals(script))
                    scriptManager.getScripts().remove(i);
            }
            script.invokeFunction("onDisable");
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {}
    }
}
