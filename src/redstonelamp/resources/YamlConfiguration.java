package redstonelamp.resources;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

public class YamlConfiguration {
	private YamlReader reader;
	private Object obj;
	private Map map;
	
	public YamlConfiguration(String yaml) throws FileNotFoundException, YamlException {
		reader = new YamlReader(new FileReader(yaml));
		obj = reader.read();
		map = (Map) obj;
	}
	
	public YamlReader getReader() {
		return this.reader;
	}
	
	public Object getObject() {
		return this.obj;
	}
	
	public Map getMap() {
		return this.map;
	}
}
