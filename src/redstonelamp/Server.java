package redstonelamp;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

import raknet.PacketHandler;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandRegistrationManager;
import redstonelamp.cmd.CommandSender;
import redstonelamp.cmd.ConsoleCommandSender;
import redstonelamp.cmd.PluginCommand;
import redstonelamp.cmd.PluginIdentifiableCommand;
import redstonelamp.cmd.SimpleCommandMap;
import redstonelamp.event.player.PlayerJoinEvent;
import redstonelamp.event.player.PlayerMoveEvent;
import redstonelamp.logger.Logger;
import redstonelamp.plugin.PluginBase;
import redstonelamp.plugin.PluginLoader;
import redstonelamp.plugin.PluginManager;
import redstonelamp.utils.RedstoneLampProperties;
import redstonelamp.utils.StringCast;

public class Server extends Thread {
	private String address, name, motd, generator_settings, level_name, seed, level_type, rcon_pass;
	private int port, spawn_protection, max_players, gamemode, difficulty;
	private boolean whitelist, announce_player_achievements, allow_cheats, spawn_animals, spawn_mobs, force_gamemode, hardcore, pvp, query, rcon, auto_save, enable_plugins;
	
	private CommandRegistrationManager commandManager;
	private PluginManager pluginManager;
	private SimpleCommandMap simpleCommandMap;
	
	private boolean isListening;
	private RedstoneLamp redstone;
	public DatagramSocket socket;
	private DatagramPacket packet;
	public long serverID;
	private Random rnd = new Random();
	public long start;
	public Player[] players;
	
	public Server(RedstoneLamp redstonelamp, String name, String motd, String port, String whitelist, String announce_player_achievements, String spawn_protection, String max_players, String allow_cheats, String spawn_animals, String spawn_mobs, String gamemode, String force_gamemode, String hardcore, String pvp, String difficulty, String generator_settings, String level_name, String seed, String level_type, String query, String rcon, String rcon_pass, String auto_save, String enable_plugins) throws SocketException {
		isListening = false;
		Thread.currentThread().setName("RedstoneLamp");
		this.name = name;
		this.motd = motd;
		this.port = StringCast.toInt(port);
		this.whitelist = StringCast.toBoolean(whitelist);
		this.announce_player_achievements = StringCast.toBoolean(announce_player_achievements);
		this.spawn_protection = StringCast.toInt(spawn_protection);
		this.max_players = StringCast.toInt(max_players);
		this.allow_cheats = StringCast.toBoolean(allow_cheats);
		this.spawn_animals = StringCast.toBoolean(spawn_animals);
		this.spawn_mobs = StringCast.toBoolean(spawn_mobs);
		this.gamemode = StringCast.toInt(gamemode);
		this.force_gamemode = StringCast.toBoolean(force_gamemode);
		this.hardcore = StringCast.toBoolean(hardcore);
		this.pvp = StringCast.toBoolean(pvp);
		this.difficulty = StringCast.toInt(difficulty);
		this.generator_settings = generator_settings;
		this.level_name = level_name;
		this.seed = seed;
		this.level_type = level_type;
		this.query = StringCast.toBoolean(query);
		this.rcon = StringCast.toBoolean(rcon);
		this.rcon_pass = rcon_pass;
		this.auto_save = StringCast.toBoolean(auto_save);
		this.enable_plugins = StringCast.toBoolean(enable_plugins);
		this.getLogger().info("Starting Minecraft: PE Server v" + this.getMCVersion());
		try {
			InetAddress ip = InetAddress.getLocalHost();
			this.address = ip.getHostAddress();
		} catch(UnknownHostException e) {
			this.getLogger().fatal("Unable to determine system IP!");
		}
		this.getLogger().info("Opening server on " + this.address + ":" + this.port);
		this.getLogger().info("This server is running " + RedstoneLamp.SOFTWARE + " version " + RedstoneLamp.VERSION + " \"" + RedstoneLamp.CODENAME + "\" (API " + RedstoneLamp.API_VERSION + ")");
		this.getLogger().info(RedstoneLamp.SOFTWARE + " is distributed under the " + RedstoneLamp.LICENSE);
		
		File folder = new File("./plugins");
		File inuse = new File("./plugins/cache".trim());
		
		if(this.enable_plugins) {
			if(!folder.exists())
				folder.mkdirs();
			
			if(!inuse.exists())
				inuse.mkdirs();
			inuse.deleteOnExit();
			
			simpleCommandMap = new SimpleCommandMap(this);
			commandManager = new CommandRegistrationManager(simpleCommandMap);
			pluginManager = new PluginManager(this, simpleCommandMap);
			PluginLoader pluginLoader = new PluginLoader(this);
			
			pluginLoader.setPluginOption("./plugins/".trim(), "./plugins/cache/".trim());
			pluginManager.registerPluginLoader(pluginLoader);
			pluginManager.loadPlugins(folder);
			
			CommandSender sender = new ConsoleCommandSender();
			
			PlayerJoinEvent pje = new PlayerJoinEvent(null);
			PlayerMoveEvent pme = new PlayerMoveEvent(null);
			pluginManager.callEvent(pje);
			pluginManager.callEvent(pme);
			
			String cmd = null;
			ArrayList<Command> cmdList = this.getCommandRegistrationManager().getPluginCommands(cmd);
			for(Command command : cmdList) {
				PluginCommand pcmd = (PluginCommand) command;
				PluginBase base = (PluginBase) pcmd.getPlugin();
				if(base != null)
					base.onCommand(sender, command, cmd, null);
			}
		} else
			this.getLogger().info("Plugins are not enabled so RedstoneLamp has ignored any exsisting Plugin files!");
		
		socket = new DatagramSocket(StringCast.toInt(port));
		socket.getBroadcast();
		serverID = rnd.nextLong();
		isListening = true;
	}
	
	public void run() {
		start = System.currentTimeMillis();
		while(isListening) {
			byte[] buffer = new byte[1536];
			packet = new DatagramPacket(buffer, 1536);
			int packetSize = 0;
			try {
				socket.setSoTimeout(5000);
				socket.receive(packet);
				socket.setSoTimeout(0);
				packetSize = packet.getLength();
			} catch(Exception e) {}
			
			if(packetSize > 0) {
				ByteBuffer b = ByteBuffer.wrap(packet.getData());
				byte[] data = new byte[packet.getLength()];
				b.get(data);
				
				DatagramPacket pkt = new DatagramPacket(data, packetSize);
				pkt.setAddress(packet.getAddress());
				pkt.setPort(packet.getPort());
				new Thread(new PacketHandler(redstone, this, pkt)).start();
			}
		}
	}
	
	/**
	 * Returns the Server Address
	 * 
	 * @return String
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Returns the Server Port
	 * 
	 * @return int
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Returns the Server MOTD
	 * 
	 * @return String
	 */
	public String getMOTD() {
		return motd;
	}
	
	/**
	 * Returns the Server Name
	 * 
	 * @return String
	 */
	public String getServerName() {
		return name;
	}
	
	/**
	 * Returns true if the server is whitelisted
	 * 
	 * @return boolean
	 */
	public boolean isWhitelisted() {
		return whitelist;
	}
	
	/**
	 * Returns the number of player slots
	 * 
	 * @return int
	 */
	public int getMaxPlayers() {
		return max_players;
	}
	
	/**
	 * Returns true if cheats are enabled
	 * 
	 * @return boolean
	 */
	public boolean cheatsEnabled() {
		return allow_cheats;
	}
	
	/**
	 * Returns true if spawn animals is enabled
	 * 
	 * @return boolean
	 */
	public boolean spawnAnimals() {
		return spawn_animals;
	}
	
	/**
	 * Returns true if spawn mobs is enabled
	 * 
	 * @return boolean
	 */
	public boolean spawnMobs() {
		return spawn_mobs;
	}
	
	/**
	 * Returns the gamemode integer
	 * 
	 * @return int
	 */
	public int getGamemode() {
		return gamemode;
	}
	
	/**
	 * Returns true if hardcore is enabled
	 * 
	 * @return boolean
	 */
	public boolean isHardcore() {
		return hardcore;
	}
	
	/**
	 * Returns true if PvP is enabled
	 * 
	 * @return boolean
	 */
	public boolean isPvPEnabled() {
		return pvp;
	}
	
	/**
	 * Returns the difficulty integer
	 * 
	 * @return int
	 */
	public int getDifficulty() {
		return difficulty;
	}
	
	/**
	 * Returns the default levels name
	 * 
	 * @return String
	 */
	public String getLevelName() {
		return level_name;
	}
	
	/**
	 * Returns the default levels seed
	 * 
	 * @return String
	 */
	public String getSeed() {
		return seed;
	}
	
	/**
	 * Returns true if auto save is enabled
	 * 
	 * @return boolean
	 */
	public boolean isAutoSaveEnabled() {
		return auto_save;
	}
	
	/**
	 * Returns the Server ID
	 * 
	 * @return long
	 */
	public long getServerID() {
		return serverID;
	}
	
	/**
	 * Returns the version of MCPE the server is running on
	 * 
	 * @return String
	 */
	public String getMCVersion() {
		return RedstoneLamp.MC_VERSION;
	}
	
	/**
	 * Returns true if plugins are enabled
	 * 
	 * @return boolean
	 */
	public boolean pluginsEnabled() {
		return enable_plugins;
	}
	
	/**
	 * Returns the Server Logger class
	 * 
	 * @return Logger
	 */
	public Logger getLogger() {
		return RedstoneLamp.logger;
	}
	
	/**
	 * Returns the CommandRegistrationManager class
	 * 
	 * @return CommandRegistrationManager
	 */
	public CommandRegistrationManager getCommandRegistrationManager() {
		return commandManager;
	}
	
	/**
	 * Returns the PluginManager class
	 * 
	 * @return PluginManager
	 */
	public PluginManager getPluginManager() {
		return pluginManager;
	}
	
	/**
	 * @param cmd
	 * @return PluginIdentifiableCommand
	 */
	public PluginIdentifiableCommand getPluginCommand(final String cmd) {
		return commandManager.getPluginCommand(cmd);
	}
	
	/**
	 * Returns the SimpleCommandMap class
	 * 
	 * @return SimpleCommandMap
	 */
	public SimpleCommandMap getCommandMap() {
		return simpleCommandMap;
	}
	
	/**
	 * Returns the RedstoneLampProperties class
	 * 
	 * @return RedstoneLampProperties
	 */
	public RedstoneLampProperties getRedstoneLampProperties() {
		return new RedstoneLampProperties();
	}
	
	/**
	 * Returns the StringCast class
	 * 
	 * @return StringCast
	 */
	public StringCast getStringCast() {
		return new StringCast();
	}
}
