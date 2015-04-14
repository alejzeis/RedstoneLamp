package redstonelamp.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import redstonelamp.RedstoneLamp;

public class PluginLoader {

	private String PLUGIN_FOLDER                      = "";
	private String PLUGIN_FOLDER_CLASSES              = "";
	private String PLUGIN_FILE                        = ""; 
	private String JAVA_SDK                           = "";
	private HashMap<String, URLClassLoader> pluginMap = new HashMap<String, URLClassLoader>();
	private Iterable<String> options;
	private final String JAVA_HOME                    = "java.home";
	
	/*
	 * Loads a plug-in by its name
	 */
	public void loadPlugin(String plugin) {
		RedstoneLamp.server.getLogger().debug(": inside loadPlugin() method ");
		try {
			URL classUrl;
			URL pluginURL      = new File(PLUGIN_FOLDER_CLASSES + plugin).toURL();
			classUrl           = new URL(pluginURL.toExternalForm());
			URL[] classUrls    = { pluginURL };
			URLClassLoader ucl = new URLClassLoader(classUrls);
			Class<?> c         = ucl.loadClass(plugin);
			Object object      = c.newInstance();
			Object [] args     = new Object[] {};
			for (Field f : c.getDeclaredFields()) {
				RedstoneLamp.server.getLogger().info(": Field name" + f.getName());
			}
			
			for (Method m : c.getDeclaredMethods()) {
				RedstoneLamp.server.getLogger().info(": Method name" + m.getName());
				m.invoke(object, null);
			}
			ucl.close();
		} catch (Exception e) {
			e.printStackTrace();
			RedstoneLamp.server.getLogger().error(
					"Unable to load plugin " + plugin
							+ " (Plugins not supported)");
		}
		RedstoneLamp.server.getLogger().debug(": returns from loadPlugin() method ");
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
			cl         = URLClassLoader.newInstance(urls);
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
	
	/*
	 * loads plug-in description file.
	 */
	public PluginDescription loadPluginDescription(final File f) {
		PluginDescription description = null;
		JarFile jar    = null;
		InputStream is = null;
		try {
			jar = new JarFile(f);
			is  = jar.getInputStream(jar.getEntry(PLUGIN_FILE));
			description = new PluginDescription();
			description.setStream(is);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return description;
	}
	
	/*
	 * Compiles .java file and class file is generated in in-use folder for further loading
	 */
	public void loadPluginJavaFile(final File file) {
		System.setProperty(JAVA_HOME, JAVA_SDK);
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
	    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
	    Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(file.getPath()));
	    JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options,
	        null, compilationUnits);
	    boolean success = task.call();
	    try {
			fileManager.close();
			} catch (IOException e) {
			e.printStackTrace();
		}
		if(success) {
			RedstoneLamp.server.getLogger().info(" :Compilation Success");
			RedstoneLamp.server.getLogger().info(" :Class files are generated in in-use folder");
			// loading user plug-in
			loadPlugin(file.getName().substring(0, file.getName().length() - 5).trim());
		}
	}
	
	/*
	 * sets JAVA SDK location 
	 */
    public void setPluginOption(final String pfolder, final String folder, final String sdk) {
    	PLUGIN_FOLDER         = pfolder;
    	PLUGIN_FOLDER_CLASSES = folder;
    	options               = Arrays.asList( new String[] { "-d", folder} );
    	JAVA_SDK              = sdk;
    }
}
