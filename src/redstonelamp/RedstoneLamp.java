package redstonelamp;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import redstonelamp.cmd.defaults.*;
import redstonelamp.utils.MainLogger;

public class RedstoneLamp implements Runnable{
	public static String SOFTWARE = "RedstoneLamp";
	public static String VERSION = "1.2.0";
	public static String CODENAME = "Snowball";
	public static String STAGE = "DEVELOPMENT";
	public static double API_VERSION = 1.4;
	public static String LICENSE = "GNU GENERAL PUBLIC LICENSE v3";
	
	public static Properties properties;
	private static Server SERVER_INSTANCE;
	private static ExecutorService async;
	private static MainLogger logger;
	
	public static void main(String[] args) {
		logger = new MainLogger();
		new RedstoneLamp().run();
	}

	public void run(){
		try {
			properties = loadProperties();
			int workers = Integer.parseInt(properties.getProperty("async-workers", "4"));
			async = Executors.newFixedThreadPool(workers);
			logger.debug("Created " + workers + " Async threads!");
			new Server(properties, logger);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Properties loadProperties() throws IOException {
		logger.info("Loading server properties...");
		Properties properties = new Properties();
		File propFile = new File("server.properties");
		if(!propFile.exists()){
			propFile.createNewFile();
			properties.put("motd", "A Minecraft Server");
			properties.put("server-ip", "0.0.0.0");
			properties.put("max-players", "20");
			properties.put("white-list", "false");
			properties.put("level-name", "world");
			properties.put("level-type", "DEFAULT");
			properties.put("level-seed", "");
			properties.put("spawn-npcs", "true");
			properties.put("spawn-animals", "true");
			properties.put("spawn-monsters", "true");
			properties.put("hardcore", "false");
			properties.put("enable-rcon", "false");
			properties.put("force-gamemode", "false");
			properties.put("enable-query", "false");
			properties.put("allow-flight", "false");
			properties.put("announce-player-achievements", "true");
			properties.put("pvp", "true");
			properties.put("difficulty", "1");
			properties.put("gamemode", "0");
			properties.put("view-distance", "10");
			properties.put("generate-structures", "true");
			properties.put("mcpe-port", "19132");
			properties.put("mcpc-port", "25565");
			properties.put("debug", "false");
			properties.put("async-workers", "4");
			properties.store(new FileWriter(propFile), "RedstoneLamp properties");
		}
		properties.load(new FileReader(propFile));
		return properties;
	}

	protected static void setServerInstance(Server server){
		RedstoneLamp.SERVER_INSTANCE = server;
	}

	public static Server getServerInstance(){
		return RedstoneLamp.SERVER_INSTANCE;
	}
	
	public static ExecutorService getAsync() {
		return async;
	}

	public static void registerDefaultCommands() {
		getServerInstance().getCommandManager().registerCommand("ban", "Blocks a player name from joining", new Ban(), "redstonelamp.command.ban");
		getServerInstance().getCommandManager().registerCommand("banip", "Blocks a user with an IP from joining", new BanIp(), "redstonelamp.command.ban.ip");
		getServerInstance().getCommandManager().registerCommand("banlist", "Shows a list of all banned players and IPs", new BanList(), "redstonelamp.command.");
		getServerInstance().getCommandManager().registerCommand("deop", "Removes a players operator permissions", new Deop(), "redstonelamp.command.deop");
		getServerInstance().getCommandManager().registerCommand("dumpmemory", "Dumps server data for developers", new DumpMemory(), "redstonelamp.command.dump");
		getServerInstance().getCommandManager().registerCommand("effect", "Adds an effect to a player", new Effect(), "redstonelamp.command.effect");
		getServerInstance().getCommandManager().registerCommand("gamemode", "Sets a players gamemode", new Gamemode(), "redstonelamp.command.gamemode");
		getServerInstance().getCommandManager().registerCommand("garbagecollector", "Garbage collection stuff for developers", new GarbageCollector(), "redstonelamp.command.garbage");
		getServerInstance().getCommandManager().registerCommand("give", "Gives an item to a player", new Give(), "redstonelamp.command.give");
		getServerInstance().getCommandManager().registerCommand("help", "Shows all available commands", new Help(), "redstonelamp.command.help");
		getServerInstance().getCommandManager().registerCommand("kick", "Kicks a player from the server", new Kick(), "redstonelamp.command.kick");
		getServerInstance().getCommandManager().registerCommand("kill", "Kills a player", new Kill(), "redstonelamp.command.kill");
		getServerInstance().getCommandManager().registerCommand("list", "Shows a list of all online players", new List(), "redstonelamp.command.list");
		getServerInstance().getCommandManager().registerCommand("me", "Show an action in chat", new Me(), "redstonelamp.command.me");
		getServerInstance().getCommandManager().registerCommand("op", "Gives a player operator permissions", new Op(), "redstonelamp.command.op");
		getServerInstance().getCommandManager().registerCommand("pardon", "Unblocks a player name from joining", new Pardon(), "redstonelamp.command.pardon");
		getServerInstance().getCommandManager().registerCommand("pardonip", "Unblocks an IP from joining", new PardonIp(), "redstonelamp.command.pardon.ip");
		getServerInstance().getCommandManager().registerCommand("particle", "Adds a particle at a position", new Particle(), "redstonelamp.command.particle");
		getServerInstance().getCommandManager().registerCommand("plugins", "Shows a list of available plugins", new Plugins(), "redstonelamp.command.plugins");
		getServerInstance().getCommandManager().registerCommand("reload", "Reloads the server", new Reload(), "redstonelamp.command.reload");
		getServerInstance().getCommandManager().registerCommand("save-all", "Saves server data", new Save(), "redstonelamp.command.save");
		getServerInstance().getCommandManager().registerCommand("save-off", "Disables automatic saving", new SaveOff(), "redstonelamp.command.save.disable");
		getServerInstance().getCommandManager().registerCommand("save-on", "Enables automatic saving", new SaveOn(), "redstonelamp.command.save.enable");
		getServerInstance().getCommandManager().registerCommand("say", "Sends a message to chat", new Say(), "redstonelamp.command.say");
		getServerInstance().getCommandManager().registerCommand("seed", "Displays the world seed", new Seed(), "redstonelamp.command.seed");
		getServerInstance().getCommandManager().registerCommand("setworldspawn", "Sets the spawn for the world", new SetWorldSpawn(), "redstonelamp.command.setspawn.world");
		getServerInstance().getCommandManager().registerCommand("spawnpoint", "Sets the spawn point for a player", new Spawnpoint(), "redstonelamp.command.setspawn.self");
		getServerInstance().getCommandManager().registerCommand("status", "Displays server information", new Status(), "redstonelamp.command.status");
		getServerInstance().getCommandManager().registerCommand("stop", "Stops the server", new Stop(), "redstonelamp.command.stop");
		getServerInstance().getCommandManager().registerCommand("sudo", "Run a command as a player", new Sudo(), "redstonelamp.command.sudo");
		getServerInstance().getCommandManager().registerCommand("teleport", "Teleports a player", new Teleport(), "redstonelamp.command.tp");
		getServerInstance().getCommandManager().registerCommand("tell", "Sends a player a message", new Tell(), "redstonelamp.command.tell");
		getServerInstance().getCommandManager().registerCommand("time", "Manages the server time", new Time(), "redstonelamp.command.time");
		getServerInstance().getCommandManager().registerCommand("version", "Shows information about RedstoneLamp", new Version(), "redstonelamp.command.version");
		getServerInstance().getCommandManager().registerCommand("whitelist", "Manages whitelisted players", new Whitelist(), "redstonelamp.command.whitelist");
	}
}
