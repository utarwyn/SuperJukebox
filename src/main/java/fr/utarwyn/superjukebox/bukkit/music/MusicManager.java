package fr.utarwyn.superjukebox.bukkit.music;

import fr.utarwyn.superjukebox.bukkit.AbstractManager;
import fr.utarwyn.superjukebox.bukkit.Config;
import fr.utarwyn.superjukebox.bukkit.SuperJukebox;
import fr.utarwyn.superjukebox.bukkit.jukebox.Jukebox;
import fr.utarwyn.superjukebox.bukkit.jukebox.JukeboxesManager;
import fr.utarwyn.superjukebox.bukkit.util.*;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Manages all musics registered for the plugin.
 *
 * @author Utarwyn
 * @since 1.0.0
 */
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

	public MusicImportResult importMusic(String endpoint) {
		File musicsFolder = new File(SuperJukebox.getInstance().getDataFolder().getAbsolutePath() + File.separator + "musics");
		File targetFile = null;

		try {
			URL urlObject = new URL(endpoint);
			String fileName = endpoint.substring(endpoint.lastIndexOf('/') + 1, endpoint.length());
			if (!fileName.toLowerCase().endsWith(".nbs")) return MusicImportResult.MALFORMATED_URL;

			fileName = fileName.replace("%20", " ");
			targetFile = new File(musicsFolder, fileName);

			// Copy the file into the disk
			Files.copy(urlObject.openStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			File[] musicsFiles = musicsFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".nbs"));
			if (musicsFiles == null) {
				return MusicImportResult.UNKNOWN_FILE;
			}

			for (File musicFile : musicsFiles) {
				String simpleName = musicFile.getName().substring(0, musicFile.getName().lastIndexOf("."));

				if (simpleName.replace(" ", "").equalsIgnoreCase(endpoint)) {
					targetFile = musicFile;
					break;
				}
			}
		}

		if (targetFile == null || !targetFile.exists() || !targetFile.isFile()) {
			return MusicImportResult.UNKNOWN_FILE;
		}

		// Check if the target is already imported into the configuration
		for (Music music : this.getMusics()) {
			if (music.getFilename().equalsIgnoreCase(targetFile.getName())) {
				return MusicImportResult.ALREADY_IMPORTED;
			}
		}

		// Add it to the configuration (and the memory!)
		if (this.createMusicConfigurationSection(targetFile)) {
			return MusicImportResult.GOOD;
		} else {
			return MusicImportResult.DECODING_ERROR;
		}
	}

	public synchronized boolean removeMusic(Music music) {
		boolean deleted = false;
		int musicId = -1;

		// Delete the configuration section of the music
		for (Map.Entry<Integer, Music> entry : this.musics.entrySet()) {
			if (entry.getValue() == music) {
				musicId = entry.getKey();
				deleted = this.deleteMusicConfigurationSection(entry.getKey());
				break;
			}
		}

		if (deleted) {
			// Delete music from memory
			this.musics.remove(musicId);

			// Jump to next music for jukeboxes
			for (Jukebox jukebox : SuperJukebox.getInstance().getInstance(JukeboxesManager.class).getJukeboxes()) {
				if (jukebox.getCurrentMusic() == music) {
					int musicIndex = jukebox.getCurrentMusicIndex();
					List<Music> musics = jukebox.getMusics();
					Music newMusic = null;

					if (musicIndex < musics.size()) {
						newMusic = musics.get(musicIndex);
					}

					jukebox.play(newMusic);
				}
			}
		}

		return deleted;
	}

	public List<Music> getMusics() {
		return new ArrayList<>(this.musics.values());
	}

	public Music getMusic(int id) {
		return this.musics.getOrDefault(id, null);
	}

	public Integer getMusicId(Music music) {
		for (Map.Entry<Integer, Music> entry : this.musics.entrySet())
			if (entry.getValue() == music)
				return entry.getKey();

		return null;
	}

	private synchronized void reloadDatabase() {
		ConfigurationSection section;
		YamlConfiguration conf = this.database.getConfiguration();

		this.musics.clear();

		for (String confKey : conf.getKeys(false)) {
			section = conf.getConfigurationSection(confKey);
			this.loadMusicFile(section);
		}

		Log.log(this.musics.size() + " musics loaded from config!");

		// Save edited configuration! (when music doesn't exist)
		this.database.save();
	}

	private boolean loadMusicFile(ConfigurationSection section) {
		int id = section.getInt("id");
		String filename = section.getString("file").replace("..", "");

		File file = new File(this.getPlugin().getDataFolder(), Config.MUSICS_FOLDER + File.separator + filename);

		if (!file.exists()) {
			Log.warn("Music #" + id + " (" + filename + ") doesn't exist anymore! Deleting from configuration.");
			section.getRoot().set(section.getName(), null);
			return false;
		}

		try {
			Music music = NBSDecoder.decode(file);

			// Update the icon of the music
			music.setIconWithMaterialId(section.getString("icon"));

			this.musics.put(id, music);
			return true;
		} catch (NBSDecodeException ex) {
			Log.log(Level.WARNING, "Music #" + id + " (" + filename + ") cannot be loaded! Details below.", ex);
			return false;
		}
	}

	private synchronized boolean createMusicConfigurationSection(File file) {
		// Generate an unique number for the section name!
		String ts = String.valueOf(System.currentTimeMillis());
		String uniqueKey = ts + JUtil.RND.nextInt(1000);

		// Create the configuration
		ConfigurationSection section = this.database.getConfiguration().createSection("music" + uniqueKey);

		section.set("id", this.getNewMusicId());
		section.set("file", file.getName());
		section.set("icon", this.getRandomMusicMaterial().name());

		this.database.save();

		// And load the new configuration section into memory!
		if (this.loadMusicFile(section)) {
			return true;
		} else {
			section.getRoot().set(section.getName(), null);
			this.database.save();
			return false;
		}
	}

	private synchronized boolean deleteMusicConfigurationSection(int musicId) {
		for (String sectionName : this.database.getConfiguration().getKeys(false)) {
			ConfigurationSection section = this.database.getConfiguration().getConfigurationSection(sectionName);

			if (section.getInt("id") == musicId) {
				section.getRoot().set(sectionName, null);
				this.database.save();
				return true;
			}
		}

		return false;
	}

	private int getNewMusicId() {
		int max = 0;

		for (Integer musicId : this.musics.keySet())
			if (musicId > max)
				max = musicId;

		return max + 1;
	}

	private Material getRandomMusicMaterial() {
		return Material.valueOf("RECORD_" + (JUtil.RND.nextInt(10) + 3));
	}

}
