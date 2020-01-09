package fr.utarwyn.superjukebox.configuration;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * A file handler which create and contains a single instance of all config files.
 *
 * @author Utarwyn <maxime.malgorn@laposte.net>
 * @since 1.2.0
 */
public class Files {

    /**
     * The singleton instance for the main configuration
     */
    private static Configuration configuration;

    /**
     * This class cannot be instancied, it just contains config file instances.
     */
    private Files() {
        // Not implemented
    }

    /**
     * Gets the global plugin configuration. Its a singleton through all the plugin.
     *
     * @return The plugin's configuration
     */
    public static Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Construct a new configuration instance. If the instance exists, it does not create a new one.
     *
     * @param plugin The Bukkit plugin
     * @return true if the configuration has been initialized and loaded.
     */
    public static boolean initConfiguration(JavaPlugin plugin) {
        if (configuration == null) {
            configuration = new Configuration(plugin);
            return configuration.load();
        }

        return false;
    }

}
