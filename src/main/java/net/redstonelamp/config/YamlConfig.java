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
package net.redstonelamp.config;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Class to get configuration options in YAML Files
 *
 * @author Philip
 */
public class YamlConfig{
    private YamlReader reader;
    private Object obj;
    private Map<String, Object> map;

    /**
     * Reads the Yaml file from path for use of the class
     *
     * @param yaml
     * @throws FileNotFoundException
     * @throws YamlException
     */
    @SuppressWarnings("unchecked")
    public YamlConfig(String yaml) throws FileNotFoundException, YamlException{
        reader = new YamlReader(new FileReader(yaml));
        obj = reader.read();
        map = (Map<String, Object>) obj;
    }

    /**
     * Returns the YamlReader class instance
     *
     * @return
     */
    public YamlReader getReader(){
        return reader;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getMapInMap(String key, Map<String, Object> map) throws ConfigException{
        Object value = map.get(key);
        if(!(value instanceof Map)){
            throw new ConfigException("Value is not in map!");
        }
        return (Map<String, Object>) value;
    }

    /**
     * Gets an Object from the configuration with the specified <code>path</code>.
     * The Path is separated by (.). For example, if I want to get the value of myValue inside of
     * myConfig, the path would be "myConfig.myValue"
     *
     * @param path The Path of the value
     * @return The value as an Object if found, null if not.
     */
    public Object get(String path){
        if(path.indexOf('.') == -1){ //Check if its a root element
            return map.get(path);
        }
        String[] splitPath = path.split(Pattern.quote("."));
        Map<String, Object> map = this.map;
        for(int i = 0; i < splitPath.length - 1; i++){
            String element = splitPath[i];
            try{
                map = getMapInMap(element, map);
            }catch(ConfigException e){
                return null;
            }
        }
        return map.get(splitPath[splitPath.length - 1]);
    }

    @SuppressWarnings("unchecked")
    public void putString(String path, String s){ //TODO: TEST THIS
        if(path.indexOf('.') == -1){ //Check if its a root element
            map.put(path, s);
            return;
        }
        String[] splitPath = path.split(Pattern.quote("."));
        Map<String, Object> map = this.map;
        Map[] maps = new Map[splitPath.length];
        for(int i = 0; i < splitPath.length - 1; i++){
            String element = splitPath[i];
            try{
                map = getMapInMap(element, map);
            }catch(ConfigException e){
                e.printStackTrace();
            }
        }
        map.put(splitPath[splitPath.length - 1], s);
        for(int i = splitPath.length - 1; i > 0; i--){
            putMapInMap(splitPath[i], map, maps[i - 1]);
        }
    }

    public String getString(String path){
        return (String) get(path);
    }

    public boolean getBoolean(String path){
        return Boolean.parseBoolean(getString(path));
    }

    public int getInt(String path){
        return Integer.parseInt(getString(path));
    }

    /**
     * Returns a Map of the YAML file
     *
     * @return
     */
    public Map<String, Object> getMap(){
        return map;
    }

    /**
     * Returns a Map of a Map in the YAML file
     *
     * @param mapName
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getInMap(String mapName){
        Object value = map.get(mapName);
        if(value instanceof Map){
            return (Map<String, Object>) value;
        }
        return map;
    }

    public void putMapInMap(String key, Map<String, Object> mapToPut, Map<String, Object> map){
        map.put(key, mapToPut);
    }
}
