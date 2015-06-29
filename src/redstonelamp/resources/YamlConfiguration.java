package redstonelamp.resources;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YamlConfiguration {
	private File yaml;
	private Map<String, Object> data;
	private Yaml config;
	private FileWriter writer;
	
	public YamlConfiguration(String file) throws IOException {
		this.yaml = new File(file);
		this.data = new HashMap<String, Object>();
		this.config = new Yaml();
		this.writer = new FileWriter(file);
	}
	
	public YamlConfiguration set(String key, Object value) {
		this.data.put(key, value);
		return this;
	}
	
	public YamlConfiguration get(String key) {
		this.data.get(key);
		return this;
	}
	
	public YamlConfiguration dump() {
		this.config.dump(this.data);
		return this;
	}
	
	public void write() {
		this.config.dump(data, writer);
	}
}
