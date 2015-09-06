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
package net.redstonelamp.level;

import net.redstonelamp.Server;
import net.redstonelamp.level.generator.FlatGenerator;
import net.redstonelamp.level.generator.Generator;
import net.redstonelamp.level.provider.LevelProvider;
import net.redstonelamp.level.provider.leveldb.LevelDBProvider;
import net.redstonelamp.ticker.CallableTask;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for different Levels.
 *
 * @author RedstoneLamp Team
 */
public class LevelManager{
    private final Server server;
    private final Map<String, Constructor<? extends LevelProvider>> providers = new ConcurrentHashMap<>();
    private final Map<String, Constructor<? extends Generator>> generators = new ConcurrentHashMap<>();
    private final Map<String, Level> levels = new ConcurrentHashMap<>();

    public LevelManager(Server server){
        this.server = server;
    }

    /**
     * INTERNAL METHOD!
     * Used in Server to initialize the LevelManager class.
     */
    public void init(){
        server.getTicker().addRepeatingTask(new CallableTask("tick", this), 1);
        try{
            registerProvider("leveldb", LevelDBProvider.class);
            registerGenerator("flat", FlatGenerator.class);
        }catch(NoSuchMethodException e){
            e.printStackTrace();
        }
        String name = server.getConfig().getString("level-name");
        File levelDir = new File("worlds" + File.separator + name);
        if(!levelDir.isDirectory()){
            levelDir.mkdirs();
        }
        Level.LevelParameters params = new Level.LevelParameters();
        params.name = name;
        params.levelDir = levelDir;
        String format = autoDetectFormat(levelDir, server.getConfig().getString("level-default-format"));
        String gen = autoDetectGenerator(levelDir, server.getConfig().getString("level-default-generator"));
        synchronized (levels) {
            levels.clear();
            levels.put(name, new Level(this, format, gen, params)); //TODO: correct provider
        }
    }

    public void tick(long tick) {
        levels.values().stream().forEach(Level::tick);
    }

    public String autoDetectGenerator(File levelDir, String defaultGenerator){
        for(Map.Entry<String, Constructor<? extends Generator>> entry : generators.entrySet()){
            Class<? extends Generator> clazz = entry.getValue().getDeclaringClass();
            try{
                Method method = clazz.getMethod("isValid", File.class);
                int mod = method.getModifiers();
                if(!Modifier.isStatic(mod) || !Modifier.isPublic(mod) || !method.getReturnType().equals(boolean.class)){
                    continue;
                }
                try{
                    boolean result = (boolean) method.invoke(null, levelDir);
                    if(result){
                        return entry.getKey();
                    }
                }catch(IllegalAccessException | InvocationTargetException e){
                    e.printStackTrace();
                }
            }catch(NoSuchMethodException e){
            }
        }
        return defaultGenerator;
    }

    public String autoDetectFormat(File levelDir, String defaultFormat){
        for(Map.Entry<String, Constructor<? extends LevelProvider>> entry : providers.entrySet()){
            Class<? extends LevelProvider> clazz = entry.getValue().getDeclaringClass();
            try{
                Method method = clazz.getMethod("isValid", File.class);
                int mod = method.getModifiers();
                if(!Modifier.isStatic(mod) || !Modifier.isPublic(mod) || !method.getReturnType().equals(boolean.class)){
                    continue;
                }
                try{
                    boolean result = (boolean) method.invoke(null, levelDir);
                    if(result){
                        return entry.getKey();
                    }
                }catch(IllegalAccessException | InvocationTargetException e){
                    e.printStackTrace();
                }
            }catch(NoSuchMethodException e){
            }
        }
        return defaultFormat;
    }

    public boolean registerProvider(String name, Class<? extends LevelProvider> clazz) throws NoSuchMethodException{
        if(providers.containsKey(name)){
            return false;
        }
        Constructor<? extends LevelProvider> constructor = clazz.getConstructor(Level.class, Level.LevelParameters.class);
        providers.put(name, constructor);
        return true;
    }

    public boolean registerGenerator(String name, Class<? extends Generator> clazz) throws NoSuchMethodException{
        if(providers.containsKey(name)){
            return false;
        }
        Constructor<? extends Generator> constructor = clazz.getConstructor(Level.class, Level.LevelParameters.class);
        generators.put(name, constructor);
        return true;
    }
    public Constructor<? extends LevelProvider> getProvider(String providerName){
        return providers.get(providerName);
    }

    public Constructor<? extends Generator> getGenerator(String generatorName){
        return generators.get(generatorName);
    }

    public Server getServer(){
        return server;
    }

    public Level getMainLevel(){
        return levels.get(server.getConfig().getString("level-name"));
    }

    public Level getLevelByName(String name) {
        if(levels.containsKey(name)) {
            return levels.get(name);
        }
        return null;
    }

    public Collection<Level> getLevels() {
        return levels.values();
    }
}
