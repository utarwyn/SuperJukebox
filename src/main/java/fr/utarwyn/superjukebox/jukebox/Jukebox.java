package fr.utarwyn.superjukebox.jukebox;

import fr.utarwyn.superjukebox.Config;
import fr.utarwyn.superjukebox.music.Music;
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

	public int getId() {
		return this.id;
	}

	public Block getBlock() {
		return this.block;
	}

	public JukeboxSettings getSettings() {
		return this.settings;
	}

	public List<Music> getMusics() {
		return new ArrayList<>(this.musics);
	}

	public Music getCurrentMusic() {
		return this.currentMusic;
	}

	public boolean isValid() {
		return this.block.getType() == Config.MAT_JUKEBOX;
	}

	public void nextMusic() {
		this.currentMusicIdx = (this.currentMusicIdx + 1) % this.musics.size();
		this.currentMusic = this.musics.get(this.currentMusicIdx);
	}

	public void play() {
		this.nextMusic();

		// Start the player!
		if (!this.player.isTaskRunned())
			this.player.runTask();

		this.player.restart();
	}

	public void unload() {
		this.musics.clear();

		if (this.player != null)
			this.player.destroy();
	}

	/**
	 * TEMP
	 * TODO: use a music manager with custom music id to load musics here
	 */
	public void addMusic(Music music) {
		this.musics.add(music);
	}

}
