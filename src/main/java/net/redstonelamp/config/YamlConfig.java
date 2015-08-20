/**
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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

/**
 * Class to get configuration options in YAML Files
 * 
 * @author Philip
 */
public class YamlConfig {
	private YamlReader reader;
	private Object obj;
	private Map map;
	
	/**
	 * Reads the Yaml file from path for use of the class
	 * @param yaml
	 * @throws FileNotFoundException
	 * @throws YamlException
	 */
	public YamlConfig(String yaml) throws FileNotFoundException, YamlException {
		reader = new YamlReader(new FileReader(yaml));
		obj = reader.read();
		map = (Map) obj;
	}
	
	/**
	 * Returns the YamlReader class instance
	 * @return
	 */
	public YamlReader getReader() {
		return this.reader;
	}
	
	/**
	 * Returns an object from YamlReader.read();
	 * @return
	 */
	public Object getObject() {
		return this.obj;
	}
	
	/**
	 * Returns a Map of the YAML file
	 * @return
	 */
	public Map getMap() {
		return this.map;
	}
	
	/**
	 * Returns a Map of a Map in the YAML file
	 * @param map
	 * @return
	 */
	public Map getInMap(String map) {
		if(this.map.get(map) instanceof Map) {
			Map newMap = (Map) this.map.get(map);
			return newMap;
		}
		return this.map;
	}
}
