package fr.utarwyn.superjukebox;

import fr.utarwyn.superjukebox.util.Configurable;
import fr.utarwyn.superjukebox.util.YamlLinker;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Configuration class. Reflets the config.yml
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class Config extends YamlLinker {

	/**
	 * The config static instance
	 */
	private static Config instance;

	/**
	 * No constructor, its an utility class
	 */
	private Config() {
	}

	/**
	 * Download link of the plugin
	 */
	public static final String DOWNLOAD_LINK = "§c§l✖";

	/**
	 * The plugin prefix
	 */
	public static final String PREFIX = "§8[§3SuperJukebox§8] §7";

	/**
	 * Bukkit material used to distinguish super jukeboxes!
	 */
	public static final Material MAT_JUKEBOX = Material.JUKEBOX;

	/**
	 * Folder inside the plugin's folder used to store all music files
	 */
	public static final String MUSICS_FOLDER = "musics";

	@Configurable
	public static boolean debug;

	@Configurable
	public static String locale;

	@Configurable(key = "alljukeboxesaresuper")
	public static boolean allJukeboxAreSuper;

	@Configurable(key = "jukeboxconfiguration.settings.distance")
	public static int jukeboxDefaultDistance;

	@Configurable(key = "jukeboxconfiguration.settings.volume")
	public static int jukeboxDefaultVolume;

	@Configurable(key = "jukeboxconfiguration.settings.globalmusics")
	public static boolean jukeboxDefaultUseGlobalMusics;

	@Configurable(key = "jukeboxconfiguration.settings.autoplay")
	public static boolean jukeboxDefaultUseAutoplay;

	@Configurable(key = "jukeboxconfiguration.settings.particles")
	public static boolean jukeboxDefaultParticles;

	@Configurable(key = "jukeboxconfiguration.settings.playwithredstone")
	public static boolean jukeboxDefaultPlayWithRedstone;

	@Configurable(key = "jukeboxconfiguration.settings.announcements")
	public static boolean jukeboxDefaultAnnouncements;

	@Configurable(key = "jukeboxconfiguration.permissions.interact")
	public static String jukeboxDefaultInteractPerm;

	@Configurable(key = "jukeboxconfiguration.permissions.editmusics")
	public static String jukeboxDefaultEditMusicsPerm;

	@Configurable(key = "jukeboxconfiguration.permissions.editsettings")
	public static String jukeboxDefaultEditSettingsPerm;

	@Override
	public boolean initialize(JavaPlugin plugin) {
		// Create config.yml file if not exists
		if (!new File(plugin.getDataFolder(), "config.yml").exists())
			plugin.saveDefaultConfig();

		// And load all config values ...
		return this.load(plugin.getConfig());
	}

	/**
	 * Gets the Config instance from anywhere!
	 * (Create it if it don't exists)
	 *
	 * @return The config instance.
	 */
	public static Config get() {
		if (instance != null) return instance;
		instance = new Config();
		return instance;
	}

}
