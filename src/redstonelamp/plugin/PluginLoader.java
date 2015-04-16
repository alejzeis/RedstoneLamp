package redstonelamp.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import redstonelamp.RedstoneLamp;

public class PluginLoader {
	private String PLUGIN_CLASS_FOLDER = "";
	private String PLUGIN_FILE = "";
	private String JAVA_SDK = "";
	private HashMap<String, Object> pluginMap = new HashMap<String, Object>();
	private Iterable<String> options;
	private final String JAVA_HOME = "java.home";
	private URL pluginURL;
	private HashMap<String, String> pkgMap = new HashMap<String, String>();
	private Set<String> clsNames = new TreeSet<String>();

	/*
	 * Loads a plug-in by its name.
	 */
	public void loadPlugin(String plugin) {
		RedstoneLamp.server.getLogger().debug(": inside loadPlugin() method ");
		try {
			URL[] classUrls = { pluginURL };
			URLClassLoader ucl = new URLClassLoader(classUrls);
			RedstoneLamp.server.getLogger().debug(": loading " + plugin);
			Class<?> c = ucl.loadClass(plugin);
			//checks loaded plug-in is a valid type,
			if(Plugin.class.isAssignableFrom(c)) {
				pluginMap.put(plugin, c);
				//following lines of code only for testing
				Object object = c.newInstance();
				Object[] args = new Object[] {};
				for(Method m : c.getDeclaredMethods()) {
					RedstoneLamp.server.getLogger().info(": Method name " + m.getName());
					m.invoke(object, args);
				}
				//close class loader
				ucl.close();
			} else {
				//invalid plug-in, so logs the reason
				RedstoneLamp.server.getLogger().info(": " + plugin + " is not a valid plugin. It should implement Plugin interface ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			RedstoneLamp.server.getLogger().error("Unable to load plugin " + plugin + " (Plugins not supported)");
		}
		RedstoneLamp.server.getLogger().debug(": returns from loadPlugin() method ");
	}

	/*
	 * Loads .jar file that contains all the Plug-ins
	 */
	public JarFile loadPluginJar(final File file) {
		if(file == null || file.exists() == false)
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
		} catch(MalformedURLException ex) {
			ex.printStackTrace();
		}
		while(e.hasMoreElements()) {
			JarEntry entry = e.nextElement();
			if(entry.getName().endsWith("class")) {
				String className = entry.getName().substring(0, entry.getName().length() - 6);
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
		JarFile jar = null;
		InputStream is = null;
		try {
			jar = new JarFile(f);
			is = jar.getInputStream(jar.getEntry(PLUGIN_FILE));
			description = new PluginDescription();
			description.setStream(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return description;
	}

	/*
	 * Compiles .java file and class file is generated in in-use folder
	 */
	private void generatePluginJavaClassFile(final File file) {
		System.setProperty(JAVA_HOME, JAVA_SDK);
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(file.getPath()));
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
		boolean success = task.call();
		try {
			fileManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(success) {
			RedstoneLamp.server.getLogger().info(" :Compilation Success");
			RedstoneLamp.server.getLogger().info(" :Class files are generated in in-use folder");
		} else {
			RedstoneLamp.server.getLogger().error(" :Compilation Failed");
			throw new IllegalArgumentException(" Compilation failed.....");
		}
	}

	/*
	 * loads fully plug-ins from class folder
	 */
	public void loadJavaPlugins() {
		getFullyQualifiedName();
		for(String plugin : clsNames)
			loadPlugin(plugin);
	}
	
    /*
     * compile java plug-in to generate class file
     */
	public void preparePluginFiles(File file) {
		generatePluginJavaClassFile(file);
	}

	/*
	 * sets JAVA SDK location
	 */
	@SuppressWarnings("deprecation")
	public void setPluginOption(final String pfolder, final String folder, final String sdk) {
		PLUGIN_CLASS_FOLDER = folder;
		options = Arrays.asList(new String[] { "-d", folder });
		JAVA_SDK = sdk;
		try {
			pluginURL = new File(PLUGIN_CLASS_FOLDER).toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * gets fully qualified name of a class file and stores
	 */
	private void getFullyQualifiedName() {
		File f = new File(PLUGIN_CLASS_FOLDER);
		String path = f.getAbsolutePath();
		listFiles(path, path);
		RedstoneLamp.server.getLogger().info(" fully qualified plugins " + clsNames);
	}
	
    /*
     * constructs package name and stores
     */
	private void listFiles(String path, String orig) {
		File root = new File(path);
		File[] list = root.listFiles();
		if(list == null)
			return;
		for(File f : list) {
			if(f.isDirectory()) {
				listFiles(f.getAbsolutePath(), orig);
			} else if(f.isFile()) {
				String dir = f.getAbsolutePath();
				String pkg = dir.substring(dir.indexOf(orig) + orig.length() + 1, dir.length());
				pkg = pkg.substring(0, pkg.indexOf(".class")).replaceAll("\\\\", ".");
				clsNames.add(pkg);
			}
		}
	}
}
