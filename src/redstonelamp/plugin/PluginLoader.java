package redstonelamp.plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
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
import redstonelamp.event.Event;
import redstonelamp.event.EventException;
import redstonelamp.event.EventHandler;
import redstonelamp.event.EventPriority;
import redstonelamp.event.Listener;

public class PluginLoader {
	private String PLUGIN_CLASS_FOLDER = "";
	private String JAVA_SDK = "";
	private HashMap<String, Plugin> pluginMap = new HashMap<String, Plugin>();
	private Iterable<String> options;
	private final String JAVA_HOME = "java.home";
	private URL pluginURL;
	private Set<String> clsNames = new TreeSet<String>();
	
	public Plugin loadPlugin(String plugin) {
		RedstoneLamp.server.getLogger().debug(": inside loadPlugin() method ");
		PluginBase base = null;
		try {
			URL[] classUrls = {
				pluginURL
			};
			URLClassLoader ucl = new URLClassLoader(classUrls);
			Class<?> c = ucl.loadClass(plugin);
			if(Plugin.class.isAssignableFrom(c)) {
				RedstoneLamp.server.getLogger().info(": Loading " + plugin);
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
		} catch(Exception e) {
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
		RedstoneLamp.server.getCommandRegistrationManager().setPlugin(base);
		base.onLoad();
	}
	
	/*
	 * enable plug-in
	 */
	public void enablePlugin(PluginBase base) {
		RedstoneLamp.server.getLogger().debug(":  Enabling " + base.getName());
		base.setEnabled(true);
	}
	
	/*
	 * disable plug-in
	 */
	public void disablePlugin(Plugin base) {
		((PluginBase) base).setEnabled(false);
	}
	
	/*
	 * returns Specific plug-in name;
	 */
	public Plugin getPlugin(final String name) {
		Plugin plugin = pluginMap.get(name);
		if(plugin.isEnabled())
			return pluginMap.get(name);
		else
			return null;
	}
	
	private String removeDotFromString(String str) {
		if(str.lastIndexOf('.') > 0) {
			str = str.substring(str.lastIndexOf('.') + 1, str.length());
			return str;
		}
		return str;
	}
	
	public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, final Plugin plugin) {
		Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<Class<? extends Event>, Set<RegisteredListener>>();
		Set<Method> methods;
		try {
			Method[] publicMethods = listener.getClass().getMethods();
			methods = new HashSet<Method>(publicMethods.length, Float.MAX_VALUE);
			for(Method method : publicMethods) {
				methods.add(method);
			}
			for(Method method : listener.getClass().getDeclaredMethods()) {
				methods.add(method);
			}
		} catch(NoClassDefFoundError e) {
			RedstoneLamp.server.getLogger().error("Plugin " + plugin.getName() + " has failed to register events for " + listener.getClass() + " because " + e.getMessage() + " does not exist.");
			return ret;
		}
		
		for(final Method method : methods) {
			final EventHandler eh = method.getAnnotation(EventHandler.class);
			EventPriority ep = EventPriority.HIGH;
			final Class<?> checkClass;
			if(method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
				//RedstoneLamp.server.getLogger().error(plugin.getName() + " attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
				continue;
			} else {
				RedstoneLamp.server.getLogger().info(plugin.getName() + " registered a method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
			}
			final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
			method.setAccessible(true);
			Set<RegisteredListener> eventSet = ret.get(eventClass);
			if(eventSet == null) {
				eventSet = new HashSet<RegisteredListener>();
				ret.put(eventClass, eventSet);
			}
			
			EventExecutor executor = new EventExecutor() {
				
				@Override
				public void execute(Listener listener, Event event) throws EventException {
					try {
						if(!eventClass.isAssignableFrom(event.getClass())) { return; }
						method.invoke(listener, event);
					} catch(IllegalAccessException e) {
						e.printStackTrace();
					} catch(IllegalArgumentException e) {
						e.printStackTrace();
					} catch(InvocationTargetException e) {
						e.printStackTrace();
					}
					
				}
			};
			eventSet.add(new RegisteredListener(listener, executor, ep, plugin, false));
			
		}
		return ret;
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
		} catch(IOException e) {
			e.printStackTrace();
		}
		if(success) {
			System.out.println(RedstoneLamp.server);
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
		options = Arrays.asList(new String[]{
				"-d", folder
		});
		JAVA_SDK = sdk;
		try {
			pluginURL = new File(PLUGIN_CLASS_FOLDER).toURL();
		} catch(MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * gets fully qualified name of a class file
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
	
	@SuppressWarnings("unchecked")
	class PluginDiagnosticListener implements DiagnosticListener {
		
		@Override
		public void report(Diagnostic diagnostic) {
			RedstoneLamp.server.getLogger().info("Code->" + diagnostic.getCode());
			RedstoneLamp.server.getLogger().info("Column Number->" + diagnostic.getColumnNumber());
			RedstoneLamp.server.getLogger().info("End Position->" + diagnostic.getEndPosition());
			RedstoneLamp.server.getLogger().info("Kind->" + diagnostic.getKind());
			RedstoneLamp.server.getLogger().info("Line Number->" + diagnostic.getLineNumber());
			RedstoneLamp.server.getLogger().info("Message->" + diagnostic.getMessage(Locale.ENGLISH));
			RedstoneLamp.server.getLogger().info("Position->" + diagnostic.getPosition());
			RedstoneLamp.server.getLogger().info("Source" + diagnostic.getSource());
			RedstoneLamp.server.getLogger().info("Start Position->" + diagnostic.getStartPosition());
			RedstoneLamp.server.getLogger().info("\n");
		}
	}
}
