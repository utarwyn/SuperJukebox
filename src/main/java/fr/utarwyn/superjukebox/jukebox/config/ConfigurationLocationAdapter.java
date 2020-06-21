package fr.utarwyn.superjukebox.jukebox.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

/**
 * Adapt a location from the Bukkit object
 * to a configuration section and vice-versa.
 *
 * @author Utarwyn
 * @since 0.3.1
 */
public class ConfigurationLocationAdapter {

    /**
     * Configuration key used to store the world name
     */
    private static final String WORLD_KEY = "world";

    /**
     * Configuration key used to store the position X
     */
    private static final String X_KEY = "x";

    /**
     * Configuration key used to store the position Y
     */
    private static final String Y_KEY = "y";

    /**
     * Configuration key used to store the position Z
     */
    private static final String Z_KEY = "z";

    /**
     * Configuration section object, can be null.
     */
    private ConfigurationSection configSection;

    /**
     * Location object, can be null.
     */
    private Location location;

    /**
     * Constructs the location adapter from a Bukkit location.
     *
     * @param location Bukkit location instance
     */
    public ConfigurationLocationAdapter(Location location) {
        this.location = location;
    }

    /**
     * Constructs the location adapter from a configuration section.
     *
     * @param configSection Bukkit configuration section instance
     */
    public ConfigurationLocationAdapter(ConfigurationSection configSection) {
        this.configSection = configSection;
    }

    /**
     * Generates a location object from the stored configuration section.
     *
     * @return generated location, null if this one is null or unvalid
     */
    public Location toLocation() {
        if (this.location == null && this.isConfigurationValid()) {
            this.location = new Location(
                    Bukkit.getWorld(Objects.requireNonNull(this.configSection.getString(WORLD_KEY))),
                    this.configSection.getInt(X_KEY),
                    this.configSection.getInt(Y_KEY),
                    this.configSection.getInt(Z_KEY)
            );
        }

        return this.location;
    }

    /**
     * Updates a configuration section from the stored location object.
     *
     * @param from original configuration section to update
     */
    public void updateConfigurationSection(ConfigurationSection from) {
        if (from != null && this.location.getWorld() != null) {
            from.set(WORLD_KEY, this.location.getWorld().getName());
            from.set(X_KEY, this.location.getBlockX());
            from.set(Y_KEY, this.location.getBlockY());
            from.set(Z_KEY, this.location.getBlockZ());
        }
    }

    /**
     * Checks if the configuration is valid or not.
     *
     * @return true if the configurration section is valid, false otherwise
     */
    private boolean isConfigurationValid() {
        if (this.configSection == null) {
            return false;
        }

        // Verify world
        String world = this.configSection.getString(WORLD_KEY);
        return world != null && Bukkit.getWorld(world) != null;
    }

}
