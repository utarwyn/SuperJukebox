package fr.utarwyn.superjukebox.music;

import fr.utarwyn.superjukebox.AbstractManager;
import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.util.FlatFile;
import fr.utarwyn.superjukebox.util.Log;
import fr.utarwyn.superjukebox.util.NBSDecodeException;
import fr.utarwyn.superjukebox.util.NBSDecoder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicManager extends AbstractManager {

	private FlatFile database;

	private Map<Integer, Music> musics;

	public MusicManager() {
		super(SuperJukebox.getInstance());
	}

	@Override
	public void initialize() {
		this.musics = new HashMap<>();

		if (this.database == null)
			this.database = new FlatFile("musics.yml");

		this.reloadDatabase();
	}

	@Override
	protected void unload() {

	}

	public List<Music> getMusics() {
		return new ArrayList<>(this.musics.values());
	}

	public Music getMusic(int id) {
		return this.musics.getOrDefault(id, null);
	}

	private void reloadDatabase() {
		ConfigurationSection section;
		YamlConfiguration conf = this.database.getConfiguration();

		this.musics.clear();

		for (String confKey : conf.getKeys(false)) {
			section = conf.getConfigurationSection(confKey);

			int id = section.getInt("id");
			String filename = section.getString("file").replace("..", "");

			File file = new File(this.getPlugin().getDataFolder(), "musics/" + filename);

			if (!file.exists()) {
				Log.warn("Music #" + id + " (" + filename + ") doesn't exist anymore! Deleting from configuration.");
				conf.set(confKey, null);
				continue;
			}

			try {
				Music music = NBSDecoder.decode(file);

				// Update the icon of the music
				music.setIconWithMaterialId(section.getString("icon"));

				this.musics.put(id, music);
			} catch (NBSDecodeException e) {
				Log.warn("Music #" + id + " (" + filename + ") cannot be loaded! Details below.");
				e.printStackTrace();
			}
		}

		Log.log(this.musics.size() + " musics loaded from config!");

		// Save edited configuration! (when music doesn't exist)
		this.database.save();
	}

}
