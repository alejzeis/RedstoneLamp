package redstonelamp.plugin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import redstonelamp.RedstoneLamp;

public class PluginLoader {
	private String PLUGIN_CLASS_FOLDER = "";
	private String JAVA_SDK = "";
	private HashMap<String, Plugin> pluginMap = new HashMap<String, Plugin>();
	private Iterable<String> options;
	private final String JAVA_HOME = "java.home";
	private URL pluginURL;
	private Set<String> clsNames = new TreeSet<String>();
    
	/*
	 * Loads a plug-in by its qualified name.
	 */
	public Plugin loadPlugin(String plugin) {
		RedstoneLamp.server.getLogger().debug(": inside loadPlugin() method ");
		PluginBase base = null;
		try {
			URL[] classUrls    = { pluginURL };
			URLClassLoader ucl = new URLClassLoader(classUrls);
			Class<?> c         = ucl.loadClass(plugin);
			//checks loaded plug-in is a valid type,
			if(Plugin.class.isAssignableFrom(c)) {
				RedstoneLamp.server.getLogger().info(": Loading "  + plugin);
				base = (PluginBase) c.newInstance();
				String name = removeDotFromString(plugin);
			    base.setName(name);
				initPlugin(base);
				enablePlugin(base);
				pluginMap.put(name, base);
				ucl.close();
			} else {
				RedstoneLamp.server.getLogger().info(": " + plugin + " is not a valid plugin. It should implement Plugin interface ");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			RedstoneLamp.server.getLogger().error("Unable to load plugin " + plugin + " (Plugins not supported)");
		}
		RedstoneLamp.server.getLogger().debug(": returns from loadPlugin() method ");
		return base;
	}

	/*
	 * initializing plug-in
	 */
	private void initPlugin(PluginBase base) {
		base.onLoad();
	}

	/*
	 * enable plug-in
	 */
	public void enablePlugin(PluginBase base) {
		RedstoneLamp.server.getLogger().debug( ":  Enabling " + base.getName());
		base.setEnabled(true);
	}

	/*
	 * disable plug-in
	 */
	public void disablePlugin(PluginBase base) {
		if( base.isEnabled()) {
			base.onDisable();
		}
	}
	
	/*
	 * returns Specific plug-in name;
	 */
	public Plugin getPlugin(final String name) {
		return pluginMap.get(name);
	}
	
	private String removeDotFromString(String str) {
		if( str.lastIndexOf('.') >0) {
			str = str.substring(str.lastIndexOf('.') + 1, str.length());
			return str;
		}
		return str;
	} 
	
	public Plugin getPluginCommand(final String command) {
		 Iterator<String> iter = pluginMap.keySet().iterator();
		 while(iter.hasNext()) {
			 String key        = iter.next();
			 PluginBase base   = (PluginBase)pluginMap.get(key);
			 List<String> list =  base.getCommands();
			 for(String s: list) {
				 if(s.equals(command)) return base; 
			 }
		 }
		return null;
	}
	
	/*
	 * Compiles .java file and class file is generated in in-use folder
	 */
	@SuppressWarnings("unchecked")
	private void generatePluginJavaClassFile(final File file) {
		System.setProperty(JAVA_HOME, JAVA_SDK);
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		PluginDiagnosticListener plistener = new PluginDiagnosticListener();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(file.getPath()));
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, plistener, options, null, compilationUnits);
		boolean success = task.call();
		try {
			fileManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(success) {
			RedstoneLamp.server.getLogger().info(" :Compilation Success! Class files are generated in in-use folder");
		} else {
			RedstoneLamp.server.getLogger().error(" :Compilation Failed");
			throw new IllegalArgumentException(" Compilation failed.....");
		}
	}

	/*
	 * loads fully plug-ins from class folder
	 */
	public ArrayList<Plugin> loadJavaPlugins() {
		getFullyQualifiedName();
		ArrayList<Plugin> list = new ArrayList<Plugin>();
		for(String plugin : clsNames) {
			list.add(loadPlugin(plugin));
		}
		return list;
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
		options             = Arrays.asList(new String[] { "-d", folder});
		JAVA_SDK            = sdk;
		try {
			pluginURL       = new File(PLUGIN_CLASS_FOLDER).toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * gets fully qualified name of a class file
	 */
	private void getFullyQualifiedName() {
		File f      = new File(PLUGIN_CLASS_FOLDER);
		String path = f.getAbsolutePath();
		listFiles(path, path);
		RedstoneLamp.server.getLogger().info(" fully qualified plugins " + clsNames);
	}

	/*
	 * constructs package name and stores
	 */
	private void listFiles(String path, String orig) {
		File root   = new File(path);
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
	
	class PluginDiagnosticListener implements DiagnosticListener {

		@Override
		public void report(Diagnostic diagnostic) {
			System.out.println("Code->" +  diagnostic.getCode());
				        System.out.println("Column Number->" + diagnostic.getColumnNumber());
				        System.out.println("End Position->" + diagnostic.getEndPosition());
				        System.out.println("Kind->" + diagnostic.getKind());
				        System.out.println("Line Number->" + diagnostic.getLineNumber());
				        System.out.println("Message->"+ diagnostic.getMessage(Locale.ENGLISH));
				        System.out.println("Position->" + diagnostic.getPosition());
				        System.out.println("Source" + diagnostic.getSource());
				        System.out.println("Start Position->" + diagnostic.getStartPosition());
				        	        System.out.println("\n");

			
		}
	}
}
