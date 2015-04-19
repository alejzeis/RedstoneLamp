package redstonelamp;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Random;

import redstonelamp.cmd.CommandRegistrationManager;
import redstonelamp.cmd.PluginIdentifiableCommand;
import redstonelamp.cmd.SimpleCommandMap;
import redstonelamp.logger.Logger;
import redstonelamp.plugin.PluginManager;

public class Server extends Thread {
	private String address, name, motd, generator_settings, level_name, seed, level_type, rcon_pass;
	private int port, spawn_protection, max_players, gamemode, difficulty;
	private boolean whitelist, announce_player_achievements, allow_cheats, spawn_animals, spawn_mobs, force_gamemode, hardcore, pvp, query, rcon, auto_save;
	
	private boolean running = false;
	
	private CommandRegistrationManager commandManager;
	private PluginManager pluginManager;
	private SimpleCommandMap simpleCommandMap;
	
    public DatagramSocket socket;
    private DatagramPacket packet;
    private long serverID;
    	
	public Server(String name, String motd, int port, boolean whitelist, boolean announce_player_achievements, int spawn_protection, int max_players, boolean allow_cheats, boolean spawn_animals, boolean spawn_mobs, int gamemode, boolean force_gamemode, boolean hardcore, boolean pvp, int difficulty, String generator_settings, String level_name, String seed, String level_type, boolean query, boolean rcon, String rcon_pass, boolean auto_save) throws SocketException {
		if(!this.running) {
			Thread.currentThread().setName("RedstoneLamp");
			this.name = name;
			this.motd = motd;
			this.port = port;
			this.whitelist = whitelist;
			this.announce_player_achievements = announce_player_achievements;
			this.spawn_protection = spawn_protection;
			this.max_players = max_players;
			this.allow_cheats = allow_cheats;
			this.spawn_animals = spawn_animals;
			this.spawn_mobs = spawn_mobs;
			this.gamemode = gamemode;
			this.force_gamemode = force_gamemode;
			this.hardcore = hardcore;
			this.pvp = pvp;
			this.difficulty = difficulty;
			this.generator_settings = generator_settings;
			this.level_name = level_name;
			this.seed = seed;
			this.level_type = level_type;
			this.query = query;
			this.rcon = rcon;
			this.rcon_pass = rcon_pass;
			this.auto_save = auto_save;
			
			simpleCommandMap = new SimpleCommandMap(this);
			commandManager = new CommandRegistrationManager(simpleCommandMap);
			pluginManager = new PluginManager(this, simpleCommandMap);

			
			try {
				InetAddress ip = InetAddress.getLocalHost();
				this.address = ip.getHostAddress();
			} catch (UnknownHostException e) {
				this.getLogger().fatal("Unable to determine system IP!");
			}
			this.getLogger().info("Starting Minecraft: PE server on " + this.getAddress() + ":" + this.getPort());
			this.running = true;
			socket = new DatagramSocket(this.getPort());
			socket.getBroadcast();
			serverID = new Random().nextLong();
			this.start();
			this.getLogger().info("This server is running " + RedstoneLamp.SOFTWARE + " version " + RedstoneLamp.VERSION + " \"" + RedstoneLamp.CODENAME + "\" (API " + RedstoneLamp.API_VERSION + ")");
		}
	}
	
	public void run() {
		while(this.running) {
			
		}
	}
	
	/*
	 * @return String ServerIP
	 */
	public String getAddress() {
		return this.address;
	}
	
	/*
	 * @return String ServerPort
	 */
	public int getPort() {
		return this.port;
	}
	
//	/*
//	 * @return String ServerName
//	 */
//	public String getName() {
//		return this.name;
//	}
	
	/*
	 * @return String MOTD
	 */
	public String getMOTD() {
		return this.motd;
	}
	
	/*
	 * @return boolean Whitelisted
	 */
	public boolean isWhitelisted() {
		return this.whitelist;
	}
	
	/*
	 * @return int MaxPlayers
	 */
	public int getMaxPlayers() {
		return this.max_players;
	}
	
	/*
	 * @return boolean Cheats
	 */
	public boolean cheatsEnabled() {
		return this.allow_cheats;
	}
	
	/*
	 * @return boolean Animals
	 */
	public boolean spawnAnimals() {
		return this.spawn_animals;
	}
	
	/*
	 * @return boolean Mobs
	 */
	public boolean spawnMobs() {
		return this.spawn_mobs;
	}
	
	/*
	 * @return int Gamemode
	 */
	public int getGamemode() {
		return this.gamemode;
	}
	
	/*
	 * @return boolean Hardcore
	 */
	public boolean isHardcore() {
		return this.hardcore;
	}
	
	/*
	 * @return boolean PvP
	 */
	public boolean isPvPEnabled() {
		return this.pvp;
	}
	
	/*
	 * @return int Difficulty
	 */
	public int getDifficulty() {
		return this.difficulty;
	}
	
	/*
	 * @return String LevelName
	 */
	public String getLevelName() {
		return this.level_name;
	}
	
	/*
	 * @return String seed
	 */
	public String getSeed() {
		return this.seed;
	}
	
	/*
	 * @return boolean AutoSave
	 */
	public boolean isAutoSaveEnabled() {
		return this.auto_save;
	}
	
//	/*
//	 * Stops the server
//	 */
//	public void stop() {
//		if(this.running)
//			new File("./plugins/cache/").delete();
//		System.exit(1);
//	}
//	
//	/*
//	 * @param boolean force
//	 */
//	public void stop(boolean force) {
//		if(!force)
//			this.stop();
//		System.exit(1);
//	}

	/*
	 * @return Logger
	 */
	public Logger getLogger() {
		return new Logger();
	}
	
	/*
	 * @return CommandRegistrationManager
	 */
	public CommandRegistrationManager getCommandRegistrationManager() {
		return commandManager;
	}
	
	/*
	 * @return PluginManager
	 */
	public PluginManager getPluginManager() {
		return pluginManager;
	}
	
	/*
	 * return Plug-in commands
	 */
	public PluginIdentifiableCommand getPluginCommand(final String cmd) {
		return commandManager.getPluginCommand(cmd);
	}

	public SimpleCommandMap getCommandMap() {
		return simpleCommandMap;
	}

}
