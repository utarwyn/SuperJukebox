package fr.utarwyn.superjukebox;

import com.google.common.base.Preconditions;
import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.commands.MainCommand;
import fr.utarwyn.superjukebox.jukebox.JukeboxesManager;
import fr.utarwyn.superjukebox.music.MusicManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SuperJukebox extends JavaPlugin {
	private static SuperJukebox instance;

    @Override
    public void onEnable() {
        checkVersion();
        SuperJukebox.instance = this;

		// Load main configuration ...
		if (!Config.get().initialize(this))
			return;


		// Register all managers
		new MusicManager();
		new JukeboxesManager();

		// Register the main command!
		AbstractCommand.register(new MainCommand());
	}

	@Override
	public void onDisable() {
		Managers.unloadAll();
	}

	private void checkVersion() {
		if(Bukkit.getVersion().contains("1.8")) {
		    Bukkit.getLogger().log(Level.SEVERE, "---------------------\n" +
                    "Sorry, but this plugin doesn't work in 1.8 yet!\n" +
                    "We're trying to get a fix for this!\n" +
                    "---------------------");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
			return;
		}

	}

	public static SuperJukebox getInstance() {
		return instance;
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
		Preconditions.checkState(inst != null, "%s instance is null!", clazz);

		return inst;
	}
}
