package redstonelamp.security;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class BanSecurity {
	private File players = new File("banned-players.txt");
	private File ips = new File("banned-ips.txt");
	
	public void addPlayer(String name) {
		try {
			if(players.isFile())
				FileUtils.writeStringToFile(players, FileUtils.readFileToString(players) + name + "\n");
			else
				FileUtils.writeStringToFile(players, name + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addIP(String ip) {
		try {
			if(ips.isFile())
				FileUtils.writeStringToFile(ips, FileUtils.readFileToString(ips) + ip + "\n");
			else
				FileUtils.writeStringToFile(ips, ip + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean nameBanned(String name) {
		name = name.toLowerCase();
		try {
			if(players.isFile()) {
				String all = FileUtils.readFileToString(players);
				if(all.contains(name))
					return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean ipBanned(String ip) {
		try {
			if(ips.isFile()) {
				String all = FileUtils.readFileToString(ips);
				if(all.contains(ip))
					return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
