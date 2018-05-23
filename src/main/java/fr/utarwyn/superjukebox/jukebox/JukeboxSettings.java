package fr.utarwyn.superjukebox.jukebox;

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

	JukeboxSettings() {
		this.distance = 20;
		this.volume = 100;
		this.autoplay = false;
		this.useGlobalMusics = true;
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
	 * Loads all the jukebox settings from a Bukkit configuration section
	 * @param section Configuration section where all jukebox settings are stored
	 */
	void loadFromConfigurationSection(ConfigurationSection section) {
		this.autoplay = section.getBoolean("autoplay");
		this.useGlobalMusics = section.getBoolean("globalmusics");

		this.setDistance(section.getInt("distance"));
		this.setVolume(section.getInt("volume"));
	}

}
