package fr.utarwyn.superjukebox.util;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.configuration.Files;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to log messages in the console
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class Log {

    /**
     * The plugin logger
     */
    private static Logger logger;

    static {
        logger = SuperJukebox.getInstance().getLogger();
    }

    /**
     * Utility class. Cannot be instanciated.
     */
    private Log() {

    }

    /**
     * Send an error message to the console
     *
     * @param message Message to send
     */
    public static void error(String message) {
        logger.log(Level.SEVERE, message);
    }

    /**
     * Send a warning message to the console
     *
     * @param message Message to send
     */
    public static void warn(String message) {
        logger.log(Level.WARNING, message);
    }

    /**
     * Send an info message to the console only in debug mode
     *
     * @param message Message to send
     */
    public static void log(String message) {
        log(message, false);
    }

    /**
     * Send an error message to the console, and precise if you want
     * to send to force even if the plugin is not in debug mode.
     *
     * @param message Message to send
     * @param force   Force to send even if the plugin is in debug mode?
     */
    public static void log(String message, boolean force) {
        if (!force && !Files.getConfiguration().isDebug()) return;
        logger.log(Level.INFO, message);
    }

    /**
     * Send a log message to the console and precise the level of the message.
     *
     * @param level     Level of the log message!
     * @param message   Message to send
     * @param throwable A throwable to run
     */
    public static void log(Level level, String message, Throwable throwable) {
        logger.log(level, message, throwable);
    }

}
