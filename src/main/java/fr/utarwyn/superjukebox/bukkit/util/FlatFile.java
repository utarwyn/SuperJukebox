package fr.utarwyn.superjukebox.bukkit.util;

import fr.utarwyn.superjukebox.bukkit.SuperJukebox;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Manages a flat file stored on the disk.
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class FlatFile {

	/**
	 * The file object
	 */
	private File file;

	/**
	 * The Bukkit configuration object
	 */
	private YamlConfiguration configuration;

	/**
	 * Constructs a new flat file object
	 *
	 * @param path Path where the config file is located (under the plugin's data folder)
	 */
	public FlatFile(String path) {
		this.load(path);
	}

	/**
	 * Returns the configuration object
	 *
	 * @return Bukkit configuration object
	 */
	public YamlConfiguration getConfiguration() {
		return this.configuration;
	}

	/**
	 * Save the configuration in memory into the flat file on the disk
	 */
	public void save() {
		if (this.configuration == null || this.file == null)
			throw new NullPointerException("File or configuration seems to be null!");

		try {
			this.configuration.save(this.file);
		} catch (IOException ex) {
			Log.log(Level.SEVERE, "Cannot save the configuration in " + this.file.getName() + "!", ex);
		}
	}

	/**
	 * Load a .yml file into memory and load the configuration object
	 *
	 * @param path Path where the config file is located (under the plugin's data folder)
	 */
	protected void load(String path) {
		this.file = new File(SuperJukebox.getInstance().getDataFolder(), path);

		// Create the flat configuration file if doesn't exists.
		if (!file.exists()) {
			if (!file.getParentFile().exists())
				if (!file.getParentFile().mkdirs())
					return;

			try {
				if (!file.createNewFile()) return;
			} catch (IOException ex) {
				Log.log(Level.SEVERE, "Cannot create the configuration file" + this.file.getName() + "!", ex);
			}
		}

		this.configuration = YamlConfiguration.loadConfiguration(this.file);
	}

}
