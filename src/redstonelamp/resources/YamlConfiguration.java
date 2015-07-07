package redstonelamp.resources;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YamlConfiguration {
	private File file;
	private FileWriter writer;
	private Map<String, Object> data;
	private Yaml yaml = new Yaml();
	
	public YamlConfiguration(String file) throws IOException {
		this.data = new HashMap<String, Object>();
		this.file = new File(file);
		this.writer = new FileWriter(this.file);
	}
	
	public YamlConfiguration set(String key, Object value) {
		this.data.put(key, value);
		return this;
	}
	
	public Object get(Object key) {
		return this.data.get(key);
	}
	
	public void save() {
		this.yaml.dump(this.data, this.writer);
	}
	
	public boolean close() {
		try {
			this.writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
