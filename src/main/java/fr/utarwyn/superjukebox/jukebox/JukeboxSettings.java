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
	}

	/**
	 * Returns the max distance of the jukebox
	 * @return Max distance of the jukebox
	 */
	public int getDistance() {
		return this.distance;
	}

	/**
	 * Returns true if the jukebox is in an auto-playing mode
	 * @return True if autoplay option is activated for the linked jukebox
	 */
	public boolean isAutoplay() {
		return this.autoplay;
	}

	/**
	 * Sets the max distance
	 * @param distance Max distance
	 */
	public void setDistance(int distance) {
		this.distance = distance;
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
		this.distance = section.getInt("settings.distance");
		this.autoplay = section.getBoolean("settings.autoplay");
	}

}
