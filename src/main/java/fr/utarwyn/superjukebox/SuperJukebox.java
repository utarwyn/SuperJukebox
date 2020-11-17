package fr.utarwyn.superjukebox;

import fr.utarwyn.superjukebox.commands.CommandManager;
import fr.utarwyn.superjukebox.configuration.Files;
import fr.utarwyn.superjukebox.jukebox.JukeboxesManager;
import fr.utarwyn.superjukebox.menu.MenuManager;
import fr.utarwyn.superjukebox.music.MusicManager;
import fr.utarwyn.superjukebox.util.Updater;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * Main class of the plugin. Everything starts here.
 * The plugin is under license. Please see LICENSE file to have more info.
 *
 * @author Utarwyn <maxime.malgorn@laposte.net>
 * @since 0.1.0
 */
public class SuperJukebox extends JavaPlugin {

    /**
     * bStats plugin identifier for metrics
     */
    private static final int PLUGIN_ID = 9435;

    /**
     * The SuperJukebox instance
     */
    private static SuperJukebox instance;

    /**
     * Allows to get the main instance of the plugin from all classes of the plugin.
     *
     * @return The SuperJukebox plugin instance.
     */
    public static SuperJukebox getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        SuperJukebox.instance = this;

        // Load configuration file
        if (!Files.initConfiguration(this)) {
            this.getLogger().log(Level.SEVERE, "Cannot load the configuration. " +
                    "Please check the above log. Plugin loading failed.");
            return;
        }

        // Register all managers
        CommandManager commandManager = new CommandManager();
        new MenuManager();
        new MusicManager();
        new JukeboxesManager();
        new Updater();

        commandManager.registerCommands();

        // Enable metrics
        new Metrics(this, PLUGIN_ID);
    }

    @Override
    public void onDisable() {
        Managers.unloadAll();
    }

    /**
     * Get the instance of a registered manager by its class.
     * This method causes a warning if the manager cannot be found because
     * it's not a normal phenomenon.
     *
     * @param clazz Class searched into the manager collection
     * @param <T>   Generic type which represents the manager object
     * @return Registered manager if found otherwise null
     */
    public final <T> T getInstance(Class<T> clazz) {
        T inst = Managers.getInstance(clazz);
        if (inst == null) {
            throw new NullPointerException(clazz.getName() + " instance is null!");
        }

        return inst;
    }

}
