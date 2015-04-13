package redstonelamp.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import redstonelamp.RedstoneLamp;

public class PluginLoader {

	private String folder = "./plugins/"; // default plug-in folder name

	private HashMap<String, URLClassLoader> pluginMap = new HashMap<String, URLClassLoader>();

	/*
	 * Loads a plug-in by its name
	 */
	public void loadPlugin(String plugin) {

		RedstoneLamp.server.getLogger().debug("..inside loadPlugin() method..");

		try {
			URL classUrl;
			URL pluginURL = new File(folder + plugin).toURL();
			classUrl = new URL(pluginURL.toExternalForm());
			URL[] classUrls = { classUrl };
			URLClassLoader ucl = new URLClassLoader(classUrls);
			Class c = ucl.loadClass(plugin.substring(0, plugin.indexOf(".")));
			Object object = c.newInstance();
			for (Field f : c.getDeclaredFields()) {
				System.out.println("Field name" + f.getName());
			}
			for (Method m : c.getDeclaredMethods()) {
				RedstoneLamp.server.getLogger().info(
						" Method name " + m.getName());
				m.invoke(object, null);
			}
		} catch (Exception e) {
			RedstoneLamp.server.getLogger().info(
					"Unable to load plugin " + plugin
							+ " (Plugins not supported)");
		}
	}

	/*
	 * Loads .jar file that contains all the Plug-ins
	 */
	public JarFile loadPluginJar(final File file) {
		if (file == null || file.exists() == false)
			throw new IllegalStateException(" jar file is empty...");
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jarFile;
	}

	/*
	 * Loads plug-ins from jar file
	 */
	private void loadPlugin(final File file) {
		Enumeration<JarEntry> e = loadPluginJar(file).entries();
		URLClassLoader cl = null;
		try {
			URL[] urls = { new URL("jar:file:" + file + "!/") };
			cl = URLClassLoader.newInstance(urls);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
		while (e.hasMoreElements()) {
			JarEntry entry = e.nextElement();
			if (entry.getName().endsWith("class")) {
				String className = entry.getName().substring(0,
						entry.getName().length() - 6);
				className = className.replace('/', '.');
				// checks whether the class is valid plug-in, if yes then load

				pluginMap.put("pluginname", cl);
			}
		}
	}

	public PluginDescription loadPluginDescription(final File f) {
		PluginDescription description = null;
		JarFile jar = null;
		InputStream is = null;
		try {
			jar = new JarFile(f);
			is = jar.getInputStream(jar.getEntry("plugin.yaml"));
			description = new PluginDescription();
			description.setStream(is);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return description;
	}

}
