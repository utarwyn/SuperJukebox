package fr.utarwyn.superjukebox.bukkit.util;

import fr.utarwyn.superjukebox.bukkit.SuperJukebox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * The Utility class for SuperJukebox!
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class JUtil {

	public static final Random RND = new Random();

	/**
	 * No constructor, it's an utility class!
	 */
	private JUtil() {

	}

	/**
	 * Create a Bukkit Location object from a stored location in a Yaml configuration.
	 * (This method only supports block coordinates)
	 *
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
	 *
	 * @param section  Section where the location will be stored.
	 * @param location Location to store into the configuration.
	 */
	public static void saveLocationIntoConfig(ConfigurationSection section, Location location) {
		section.set("world", location.getWorld().getName());
		section.set("x", location.getBlockX());
		section.set("y", location.getBlockY());
		section.set("z", location.getBlockZ());
	}

	/**
	 * Shortcut used to create an synchronous Java thread
	 *
	 * @param runnable Runnable to run synchronously
	 */
	public static void runSync(Runnable runnable) {
		Bukkit.getScheduler().runTask(SuperJukebox.getInstance(), runnable);
	}

	/**
	 * Checks if a player has a specific SuperJukebox permission.
	 * Permissions are automatically prefixed by the name of the plugin.
	 *
	 * @param player Player to check
	 * @param perm   Permission used for the test
	 * @return True if the player has the given permission
	 */
	public static boolean playerHasPerm(Player player, String perm) {
		return player.isOp() || player.hasPermission("superjukebox." + perm);
	}

	/**
	 * Checks if a command sender has a specific SuperJukebox permission.
	 * Permissions are automatically prefixed by the name of the plugin.
	 *
	 * @param sender Command sender to check
	 * @param perm   Permission used for the test
	 * @return True if the command sender has the given permission
	 */
	public static boolean senderHasPerm(CommandSender sender, String perm) {
		return !(sender instanceof Player && !playerHasPerm((Player) sender, perm)) || sender instanceof ConsoleCommandSender;
	}

	/**
	 * Plays a sound at a specific location with support of 1.8 sound and 1.9+ sound.
	 *
	 * @param location Location where to play the sound
	 * @param sound18  Sound string for 1.8 versions
	 * @param sound19  Sound string for 1.9 versions
	 */
	public static void playSound(Location location, String sound18, String sound19) {
		Sound sound = JUtil.generateSound(sound18, sound19);
		if (sound == null) return;

		location.getWorld().playSound(location, sound, 1f, 1f);
	}

	/**
	 * Plays a sound at a specific location with support of 1.8 sound and 1.9+ sound.
	 * Plays the sound only for a specific player.
	 *
	 * @param player  Player which will receive the sound
	 * @param sound18 Sound string for 1.8 versions
	 * @param sound19 Sound string for 1.9 versions
	 */
	public static void playSound(Player player, String sound18, String sound19) {
		Sound sound = JUtil.generateSound(sound18, sound19);
		if (sound == null) return;

		player.playSound(player.getLocation(), sound, 1f, 1f);
	}

	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean isFloat(String str) {
		try {
			Float.parseFloat(str);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Returns the existance of a sound by its name
	 *
	 * @param soundName Sound name to check
	 * @return True if the sound with the given name exists
	 */
	private static boolean soundExists(String soundName) {
		for (Sound sound : Sound.values())
			if (sound.name().equals(soundName))
				return true;

		return false;
	}

	/**
	 * Generates a sound from two string (one for 1.8 and one for 1.9+).
	 * The method tries to generate the 1.8 sound, and if its not working it tries to
	 * generate the 1.9+ sound, and if its not working too it generate nothing, without error.
	 *
	 * @param sound18 Sound key for MC 1.8 version.
	 * @param sound19 Sound key for MC 1.9+ versions.
	 * @return The generated sound, null otherwise.
	 */
	private static Sound generateSound(String sound18, String sound19) {
		Sound sound;

		if (JUtil.soundExists(sound18))
			sound = Sound.valueOf(sound18);   // 1.8
		else if (JUtil.soundExists(sound19))
			sound = Sound.valueOf(sound19);   // 1.9+
		else
			return null;                      // Else? Not supported.

		return sound;
	}

}
