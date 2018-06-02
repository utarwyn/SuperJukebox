package fr.utarwyn.superjukebox.jukebox;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.jukebox.settings.JukeboxSettings;
import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.MusicManager;
import fr.utarwyn.superjukebox.music.MusicPlayer;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Jukebox {

	private int id;

	private Block block;

	private JukeboxSettings settings;

	private MusicPlayer player;

	private List<Music> musics;

	private Music currentMusic;

	private int currentMusicIdx;

	Jukebox(int id, Block block) {
		this.id = id;
		this.block = block;
		this.currentMusicIdx = -1;
		this.musics = new ArrayList<>();

		// Register all settings of the jukebox!
		this.settings = new JukeboxSettings();

		// Instanciate the music player!
		this.player = new MusicPlayer(this);
	}

	/**
	 * Returns the unique identifier of the jukebox
	 *
	 * @return A decimal identifier
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Returns the Bukkit block used by this jukebox class.
	 *
	 * @return This jukebox Bukkit block.
	 */
	public Block getBlock() {
		return this.block;
	}

	/**
	 * Returns the settings associated with this jukebox.
	 *
	 * @return All settings of the jukebox.
	 */
	public JukeboxSettings getSettings() {
		return this.settings;
	}

	/**
	 * Get all musics used by this jukebox.
	 * We have to check in the settings to return globals musics or custom musics.
	 *
	 * @return All musics that can be played by this jukebox.
	 */
	public List<Music> getMusics() {
		if (this.getSettings().getUseGlobalMusics().getValue()) {
			return SuperJukebox.getInstance().getInstance(MusicManager.class).getMusics();
		} else {
			return new ArrayList<>(this.musics);
		}
	}

	/**
	 * Returns the current music played by this jukebox.
	 *
	 * @return The current musics for this jukebox.
	 */
	public Music getCurrentMusic() {
		return this.currentMusic;
	}

	/**
	 * Change to the next music!
	 */
	public void nextMusic() {
		this.currentMusicIdx = (this.currentMusicIdx + 1) % this.getMusics().size();
		this.currentMusic = this.getMusics().get(this.currentMusicIdx);
	}

	/**
	 * Play the next music!
	 */
	public void playNext() {
		this.nextMusic();

		// Start the player!
		if (!this.player.isTaskRunned())
			this.player.runTask();

		this.player.start();
	}

	/**
	 * Play a specific music!
	 *
	 * @param music Music to be played.
	 */
	public void play(Music music) {
		// Does the music exist in the list?
		if (!this.getMusics().contains(music)) return;

		// Ssetup the current music
		this.currentMusicIdx = this.getMusics().indexOf(music);
		this.currentMusic = music;

		// Start the player!
		if (!this.player.isTaskRunned())
			this.player.runTask();

		// Restart the player at the beginning of the music!
		this.player.start();
	}

	/**
	 * Unload this jukebox.
	 * This means clear all musics and destroy the player object if needed.
	 */
	void unload() {
		this.musics.clear();

		if (this.player != null)
			this.player.destroy();
	}

	/**
	 * Loads musics from the jukebox configuration (with a list which contains all music ids)
	 *
	 * @param musicIdsList Music ids list extracted from the configuration
	 */
	void loadMusicsFromConfiguration(List<Integer> musicIdsList) {
		MusicManager musicManager = SuperJukebox.getInstance().getInstance(MusicManager.class);
		Music music;

		for (int musicId : musicIdsList) {
			music = musicManager.getMusic(musicId);

			if (music != null) {
				this.musics.add(music);
			}
		}
	}

}
