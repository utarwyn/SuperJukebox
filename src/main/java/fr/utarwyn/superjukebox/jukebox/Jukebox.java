package fr.utarwyn.superjukebox.jukebox;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.jukebox.settings.JukeboxSettings;
import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.MusicManager;
import fr.utarwyn.superjukebox.music.MusicPlayer;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * Respresents a SuperJukebox!
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class Jukebox {

    /**
     * The music player which played music of this jukebox!
     */
    private final MusicPlayer player;

    /**
     * Id of the jukebox
     */
    private final int id;

    /**
     * Block linked with the jukebox
     */
    private final Block block;

    /**
     * Contains all settings of this jukebox
     */
    private final JukeboxSettings settings;

    /**
     * List of all custom musics for this jukebox
     */
    private final List<Music> musics;

    /**
     * Construct a super jukebox!
     *
     * @param id    Id of the jukebox
     * @param block Block to link with
     */
    Jukebox(int id, Block block) {
        this.id = id;
        this.block = block;
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
     * Returns the music player for this jukebox.
     *
     * @return music player of this jukebox
     */
    public MusicPlayer getPlayer() {
        return this.player;
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
        boolean globalMusics = this.getSettings().getUseGlobalMusics().getValue();
        if (globalMusics) {
            return SuperJukebox.getInstance().getInstance(MusicManager.class).getMusics();
        } else {
            return new ArrayList<>(this.musics);
        }
    }

    /**
     * Add a custom music to this jukebox
     *
     * @param music Music to add!
     */
    public void addCustomMusic(Music music) {
        if (!this.musics.contains(music)) {
            this.musics.add(music);
        }
    }

    /**
     * Play a specific music!
     *
     * @param music Music to be played.
     */
    public void play(Music music) {
        if (!this.getMusics().contains(music)) {
            this.player.stop();
            return;
        }

        this.player.setCurrentMusic(music);
        this.player.start();
    }

    /**
     * Unload this jukebox.
     * This means clear all musics and destroy the player object if needed.
     */
    void unload() {
        this.musics.clear();

        if (this.player != null) {
            this.player.destroy();
        }
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
