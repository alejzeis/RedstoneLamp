package redstonelamp.monitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import redstonelamp.Server;

public class FileMonitor {

	private String path;
	private HashMap<String, Long> map = new HashMap<String, Long>();
	private boolean monitor = false;
	private Server server;

	public FileMonitor(Server server) {
		this.server = server;
	}

	public void start() {
		File f = new File("./plugins");
		path = f.getAbsolutePath();
		loadPluginFiles(path, monitor);
		monitor = true;
		System.out.println(map);
		PluginMonitor monitor = new PluginMonitor();
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		service.scheduleAtFixedRate(monitor, 1, 1, TimeUnit.SECONDS);
		// service.submit(monitor);
	}

	private void loadPluginFiles(String path, boolean monitor) {
		File root   = new File(path);
		File[] list = root.listFiles();
		if (list == null)
			return;
		
		for (File f : list) {
			if (f.isFile()) {
				long time = f.lastModified();
				if (monitor) {
					if(!map.containsKey(f.getName().trim())) {
						map.put(f.getName(), f.lastModified());
						server.getPluginManager().loadPlugin(f, getPackageName(f));
					}
					else {
						long ftime = map.get(f.getName());
						if (f.lastModified() > ftime) {
							map.put(f.getName(), f.lastModified());
							server.getPluginManager().loadPlugin(f, getPackageName(f));
						}
					}
				} else {
					if (f.getName().endsWith(".java"))
						map.put(f.getName().trim(), time);
					getPackageName(f);
				}
			}
		}
	}

	class PluginMonitor implements Runnable {
		@Override
		public void run() {
			loadPluginFiles(path, monitor);
		}
	}
	
	private String getPackageName(File file) {
		BufferedReader br;
		try {
			 br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		String line="";
		String pkg = null ;
		try {
			while((line = br.readLine()) != null) {
				if(line.contains("import")) break;
				if(line.contains("package")) {
					pkg = line;
					break;
				}
			}
			if(pkg != null) {
				pkg = pkg.trim();
				pkg = pkg.substring(pkg.indexOf("package")+7, pkg.length()-1);
				return pkg;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}
}
