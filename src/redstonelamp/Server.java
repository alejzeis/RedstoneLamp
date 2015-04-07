package redstonelamp;

import java.net.InetAddress;
import java.net.UnknownHostException;

import redstonelamp.logger.ServerLogger;

public class Server {
	private InetAddress address;
	private String motd, generator_settings, level_name, seed, level_type, rcon_pass;
	private int port, spawn_protection, max_players, gamemode, difficulty;
	private boolean whitelist, announce_player_achievements, allow_cheats, spawn_animals, spawn_mobs, force_gamemode, hardcore, pvp, query, rcon, auto_save;
	
	public Server(String motd, int port, boolean whitelist, boolean announce_player_achievements, int spawn_protection, int max_players, boolean allow_cheats, boolean spawn_animals, boolean spawn_mobs, int gamemode, boolean force_gamemode, boolean hardcore, boolean pvp, int difficulty, String generator_settings, String level_name, String seed, String level_type, boolean query, boolean rcon, String rcon_pass, boolean auto_save) {
		this.getLogger().info("Starting Minecraft: PE server");
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
		try {
			this.address = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			this.getLogger().fatal("Unable to determine system IP!");
		}
		this.getLogger().info("Starting Minecraft: PE server on " + this.getAddress() + ":" + this.getPort());
	}
	
	public InetAddress getAddress() {
		return this.address;
	}
	
	public int getPort() {
		return this.port;
	}

	public ServerLogger getLogger() {
		return new ServerLogger();
	}
}
