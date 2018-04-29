package fr.utarwyn.superjukebox.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

/**
 * The Utility class for SuperJukebox!
 * @since 1.0.0
 * @author Utarwyn
 */
public class JUtil {

	/**
	 * No constructor, it's an utility class!
	 */
	private JUtil() {

	}

	/**
	 * Create a Bukkit Location object from a stored location in a Yaml configuration.
	 * (This method only supports block coordinates)
	 * @param section Root configuration section where the location is stored.
	 * @return A Bukkit Location object.
	 */
	public static Location getLocationFromConfig(ConfigurationSection section) {
		return new Location(
				Bukkit.getWorld(section.getString("world")),
				section.getInt("x"), section.getInt("y"), section.getInt("z")
		);
	}

	/**
	 * Save a Bukkit Location object into a Yaml configuration.
	 * (This method only supports block coordinates)
	 * @param section Section where the location will be stored.
	 * @param location Location to store into the configuration.
	 */
	public static void saveLocationIntoConfig(ConfigurationSection section, Location location) {
		section.set("world", location.getWorld().getName());
		section.set("x", location.getBlockX());
		section.set("y", location.getBlockY());
		section.set("z", location.getBlockZ());
	}

}
