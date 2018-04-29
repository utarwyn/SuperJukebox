package fr.utarwyn.superjukebox;

import fr.utarwyn.superjukebox.util.Configurable;
import fr.utarwyn.superjukebox.util.YamlLinker;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Configuration class. Reflets the config.yml
 * @since 1.0.0
 * @author Utarwyn
 */
public class Config extends YamlLinker {

	/**
	 * The config static instance
	 */
	private static Config instance;

	/**
	 * No constructor, its an utility class
	 */
	private Config() {}

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

	@Configurable
	public static boolean debug;

	@Configurable
	public static String locale;

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
	 * @return The config instance.
	 */
	public static Config get() {
		if (instance != null) return instance;
		return instance = new Config();
	}

}
