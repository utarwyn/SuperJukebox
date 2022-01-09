package fr.utarwyn.superjukebox.commands;

import fr.utarwyn.superjukebox.AbstractManager;
import fr.utarwyn.superjukebox.SuperJukebox;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

/**
 * A manager to handle commands of the plugin.
 *
 * @author Utarwyn <maxime.malgorn@laposte.net>
 * @since 2.2.0
 */
public class CommandManager extends AbstractManager {

    /**
     * The Bukkit command map retrieved with reflection
     */
    private CommandMap cachedCommandMap;

    /**
     * Constructs the manager
     */
    public CommandManager() {
        super(SuperJukebox.getInstance());
    }

    @Override
    public void initialize() {
        // Not implemented
    }

    @Override
    protected void unload() {
        // Not implemented
    }

    /**
     * Register and initialize needed commands for the plugin.
     */
    public void registerCommands() {
        try {
            this.register(MainCommand.class);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                InvocationTargetException | NoSuchFieldException e) {
            this.logger.log(Level.SEVERE, "Cannot instanciate a command class", e);
        }
    }

    /**
     * Register an abstract command directly inside the server's command map.
     * This method is called by the AsbtractCommand class.
     *
     * @param commandClass Class of the command to register inside the Bukkit server
     */
    private void register(Class<? extends AbstractCommand> commandClass) throws IllegalAccessException,
            InstantiationException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        CommandMap commandMap = getCommandMap();

        if (commandMap != null) {
            commandMap.register("superjukebox", commandClass.getDeclaredConstructor().newInstance());
        }
    }

    /**
     * This method returns the command map of the server!
     *
     * @return The Bukkit internal Command map
     * @throws NoSuchFieldException   "commandMap" field cannot be found
     * @throws IllegalAccessException cannot access to the field "commandMap"
     */
    private CommandMap getCommandMap() throws NoSuchFieldException, IllegalAccessException {
        // Get the command map of the server first!
        if (cachedCommandMap == null) {
            Server server = this.plugin.getServer();
            Field fMap = server.getClass().getDeclaredField("commandMap");

            fMap.setAccessible(true);
            cachedCommandMap = (SimpleCommandMap) fMap.get(server);
            fMap.setAccessible(false);
        }

        return cachedCommandMap;
    }

}
