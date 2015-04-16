package redstonelamp;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import redstonelamp.command.CommandRegistrationManager;
import redstonelamp.logger.ServerLogger;
import redstonelamp.plugin.PluginManager;

@SuppressWarnings("unused")
public class Server {
	private String address, name, motd, generator_settings, level_name, seed, level_type, rcon_pass;
	private int port, spawn_protection, max_players, gamemode, difficulty;
	private boolean whitelist, announce_player_achievements, allow_cheats, spawn_animals, spawn_mobs, force_gamemode, hardcore, pvp, query, rcon, auto_save;
	
	private boolean running = false;
	
	private CommandRegistrationManager commandManager;
	
	private PluginManager pluginManager;
	
	
	/*
	 * Initialize the server if it is not running
	 */
	public Server(String name, String motd, int port, boolean whitelist, boolean announce_player_achievements, int spawn_protection, int max_players, boolean allow_cheats, boolean spawn_animals, boolean spawn_mobs, int gamemode, boolean force_gamemode, boolean hardcore, boolean pvp, int difficulty, String generator_settings, String level_name, String seed, String level_type, boolean query, boolean rcon, String rcon_pass, boolean auto_save) {
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
			
			//// register command registration manager
			commandManager = new CommandRegistrationManager();
			
			try {
				InetAddress ip = InetAddress.getLocalHost();
				this.address = ip.getHostAddress();
			} catch (UnknownHostException e) {
				this.getLogger().fatal("Unable to determine system IP!");
			}
			this.getLogger().info("Starting Minecraft: PE server on " + this.getAddress() + ":" + this.getPort());
			this.running = true;
			this.start();
			this.getLogger().info("This server is running " + RedstoneLamp.SOFTWARE + " version " + RedstoneLamp.VERSION + " \"" + RedstoneLamp.CODENAME + "\" (API " + RedstoneLamp.API_VERSION + ")");
		}
	}
	
	/*
	 * Used to start the server (Maybe open the socket?)
	 */
	private void start() {
		
	}
	
	/*
	 * Returns the servers IP
	 */
	public String getAddress() {
		return this.address;
	}
	
	/*
	 * Returns the servers Port
	 */
	public int getPort() {
		return this.port;
	}
	
	/*
	 * Returns the servers name
	 */
	public String getName() {
		return this.name;
	}
	
	/*
	 * Returns the servers Message of The Day
	 */
	public String getMOTD() {
		return this.motd;
	}
	
	/*
	 * Returns true if server is whitelisted
	 */
	public boolean isWhitelisted() {
		return this.whitelist;
	}
	
	/*
	 * Returns number of players that can join total (not open slots)
	 */
	public int getMaxPlayers() {
		return this.max_players;
	}
	
	/*
	 * Returns true if players can fly without creative mode
	 */
	public boolean cheatsEnabled() {
		return this.allow_cheats;
	}
	
	/*
	 * Returns true if spawn_animals is enabled
	 */
	public boolean spawnAnimals() {
		return this.spawn_animals;
	}
	
	/*
	 * Returns true is spawn_mobs is enabled
	 */
	public boolean spawnMobs() {
		return this.spawn_mobs;
	}
	
	/*
	 * Returns the gamemode integer
	 */
	public int getGamemode() {
		return this.gamemode;
	}
	
	/*
	 * Returns true if Hardcore is enabled (Ban on death)
	 */
	public boolean isHardcore() {
		return this.hardcore;
	}
	
	/*
	 * Returns true if pvp is enabled
	 */
	public boolean isPvPEnabled() {
		return this.pvp;
	}
	
	/*
	 * Returns difficulty integer
	 */
	public int getDifficulty() {
		return this.difficulty;
	}
	
	/*
	 * Returns level_name
	 */
	public String getLevelName() {
		return this.level_name;
	}
	
	/*
	 * Returns the seed used
	 */
	public String getSeed() {
		return this.seed;
	}
	
	/*
	 * Returns true if auto_save is enabled
	 */
	public boolean isAutoSaveEnabled() {
		return this.auto_save;
	}
	
	/*
	 * Stops the server
	 */
	public void stop() {
		if(this.running) {
			new File("./plugins/in_use/").delete();
		}
		System.exit(1);
	}
	
	/*
	 * Forces the software to close (ie if crashed) if force equals true
	 */
	public void stop(boolean force) {
		if(!force)
			this.stop();
		System.exit(1);
	}

	/*
	 * Returns the ServerLogger class
	 */
	public ServerLogger getLogger() {
		return new ServerLogger();
	}
	
	/*
	 * returns command registration manager
	 */
	public CommandRegistrationManager getCommandRegistrationManager() {
		return commandManager;
	}
	
	/*
	 * returns plug in manager
	 */
	public PluginManager getPluginManager() {
		return pluginManager;
	}
	
}
