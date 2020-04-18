package fr.utarwyn.superjukebox;

import fr.utarwyn.superjukebox.util.Log;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abtsract base class for creating a SuperJukeboox manager
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public abstract class AbstractManager implements Listener {

    /**
     * Stores the plugin main class
     */
    protected SuperJukebox plugin;

    /**
     * Stores the plugin logger
     */
    protected Logger logger;

    /**
     * Constructs the manager
     *
     * @param plugin    Main class of the plugin
     * @param listeners List of listeners to automatically load during the initialization of the manager
     */
    public AbstractManager(SuperJukebox plugin, Listener... listeners) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();

        // Now we register all listeners (and the manager too)
        List<Listener> listenerList = new ArrayList<>();

        Collections.addAll(listenerList, listeners);
        listenerList.add(this);

        for (Listener listener : listenerList)
            this.registerListener(listener);


        // We start the initialization of the manager
        this.initialize();

        // Register the manager for the plugin
        try {
            Managers.registerManager(this.getClass(), this);
        } catch (Exception ex) {
            Log.log(Level.WARNING, "Cannot register the manager " + this.getClass().getSimpleName() + "!", ex);
        }
    }

    /**
     * Register a specific listener to the server
     *
     * @param listener Listener to register
     */
    protected void registerListener(Listener listener) {
        this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
    }

    /**
     * Returns the main class of the plugin. Only a shortcut.
     *
     * @return Main class
     */
    public SuperJukebox getPlugin() {
        return this.plugin;
    }

    /**
     * Called when the manager is initializing.
     * Called in the constructor so before the constructor of sub-managers.
     */
    public abstract void initialize();

    /**
     * Called when the manager is unloading.
     */
    protected abstract void unload();

}
