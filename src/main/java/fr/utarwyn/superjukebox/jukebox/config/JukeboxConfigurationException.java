package fr.utarwyn.superjukebox.jukebox.config;

/**
 * Thrown when an error occurs during a
 * jukebox initialization from the configuration file.
 *
 * @author Utarwyn
 * @since 0.3.1
 */
public class JukeboxConfigurationException extends Exception {

    public JukeboxConfigurationException(String message) {
        super(message);
    }

}
