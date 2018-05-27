package fr.utarwyn.superjukebox.jukebox;

import fr.utarwyn.superjukebox.Config;
import fr.utarwyn.superjukebox.jukebox.perm.Permission;
import fr.utarwyn.superjukebox.jukebox.perm.PermissionType;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Stores all settings for one jukebox.
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class JukeboxSettings {

	/**
	 * Under this number of blocks, Minecraft seems to not respect the sound distance.
	 * So we have to define a minimum distance to keep a good Jukebox behavior.
	 */
	private static final int MIN_DISTANCE = 15;

	/**
	 * Max distance where a player can hear the sound of the jukebox
	 */
	private int distance;

	/**
	 * Volume of a jukebox, in percent.
	 */
	private int volume;

	/**
	 * True if the jukebox have to run automatically new music
	 */
	private boolean autoplay;

	/**
	 * True if the jukebox uses the global musics stored by the server
	 */
	private boolean useGlobalMusics;

	/**
	 * Permission to interact with the jukebox and to show its main menu.
	 */
	private Permission interactPerm;

	/**
	 * Permission to edit musics of the jukebox.
	 */
	private Permission editMusicsPerm;

	/**
	 * Permission to change settings of the jukebox.
	 */
	private Permission editSettingsPerm;

	/**
	 * Constructs the jukebox settings object and apply default configuration!
	 */
	JukeboxSettings() {
		// Default settings
		this.autoplay = Config.jukeboxDefaultUseAutoplay;
		this.useGlobalMusics = Config.jukeboxDefaultUseGlobalMusics;
		this.setDistance(Config.jukeboxDefaultDistance);
		this.setVolume(Config.jukeboxDefaultVolume);

		// Default permissions
		this.interactPerm = this.createPermissionNode(Config.jukeboxDefaultInteractPerm, "interact");
		this.editMusicsPerm = this.createPermissionNode(Config.jukeboxDefaultEditMusicsPerm, "editmusics");
		this.editSettingsPerm = this.createPermissionNode(Config.jukeboxDefaultEditSettingsPerm, "editsettings");
	}

	/**
	 * Returns the max distance of the jukebox
	 * @return Max distance of the jukebox
	 */
	public int getDistance() {
		return this.distance;
	}

	/**
	 * Returns the volume of the jukebox, in percent.
	 * @return Volume in percent.
	 */
	public int getVolume() {
		return this.volume;
	}

	/**
	 * Returns the interaction permission of the linked jukebox
	 * @return The permission node
	 */
	public Permission getInteractPerm() {
		return this.interactPerm;
	}

	/**
	 * Returns the musics edition permission of the linked jukebox
	 * @return The permission node
	 */
	public Permission getEditMusicsPerm() {
		return this.editMusicsPerm;
	}

	/**
	 * Returns the settings edition permission of the linked jukebox
	 * @return The permission node
	 */
	public Permission getEditSettingsPerm() {
		return this.editSettingsPerm;
	}

	/**
	 * Returns true if the jukebox is in an auto-playing mode
	 * @return True if autoplay option is activated for the linked jukebox
	 */
	public boolean isAutoplay() {
		return this.autoplay;
	}

	/**
	 * Returns true is the jukebox uses the global musics system.
	 * @return True for global musics.
	 */
	public boolean usesGlobalMusics() {
		return this.useGlobalMusics;
	}

	/**
	 * Sets the max distance
	 * @param distance Max distance
	 */
	public void setDistance(int distance) {
		this.distance = Math.max(distance, MIN_DISTANCE);
	}

	/**
	 * Sets the volume
	 * @param volume The volume in percent
	 */
	public void setVolume(int volume) {
		this.volume = Math.max(volume, 0);
	}

	/**
	 * Activate the autoplay behavior or no.
	 * @param autoplay True to turn on the autoplay system
	 */
	public void setAutoplay(boolean autoplay) {
		this.autoplay = autoplay;
	}

	/**
	 * Use global musics for this jukebox or no.
	 * @param useGlobalMusics True if the jukebox have to use global musics.
	 */
	public void setUseGlobalMusics(boolean useGlobalMusics) {
		this.useGlobalMusics = useGlobalMusics;
	}

	/**
	 * Loads all the jukebox settings from a Bukkit configuration section
	 * @param settingsSection Configuration section where all jukebox settings are stored
	 * @param permsSection Configuration section where all jukebox permissions are stored
	 */
	void loadFromConfiguration(ConfigurationSection settingsSection, ConfigurationSection permsSection) {
		// Load settings
		if (settingsSection != null) {
			this.autoplay = settingsSection.getBoolean("autoplay");
			this.useGlobalMusics = settingsSection.getBoolean("globalmusics");

			this.setDistance(settingsSection.getInt("distance"));
			this.setVolume(settingsSection.getInt("volume"));
		}

		// Load permissions
		if (permsSection != null) {
			this.interactPerm = this.createPermissionNode(permsSection, "interact");
			this.editMusicsPerm = this.createPermissionNode(permsSection, "editmusics");
			this.editSettingsPerm = this.createPermissionNode(permsSection, "editsettings");
		}
	}

	/**
	 * Creates a permission node from a name and the configuration section
	 * @param configSection Configuration section used to get the type of the permission
	 * @param permissionName Permission name used to create the Bukkit permission
	 * @return The generated permission node
	 */
	private Permission createPermissionNode(ConfigurationSection configSection, String permissionName) {
		return new Permission(PermissionType.getByName(configSection.getString(permissionName)), permissionName);
	}

	/**
	 * Creates a permission node from a permission type and a name
	 * @param permissionType Permission type
	 * @param permissionName Permission name used to create the Bukkit permission
	 * @return The generated permission node
	 */
	private Permission createPermissionNode(String permissionType, String permissionName) {
		return new Permission(PermissionType.getByName(permissionType), permissionName);
	}

}
