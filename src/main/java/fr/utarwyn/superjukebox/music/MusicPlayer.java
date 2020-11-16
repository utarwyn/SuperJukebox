package fr.utarwyn.superjukebox.music;

import fr.mrmicky.fastparticle.FastParticle;
import fr.mrmicky.fastparticle.ParticleType;
import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.jukebox.Jukebox;
import fr.utarwyn.superjukebox.music.converters.InstrumentConverter;
import fr.utarwyn.superjukebox.music.converters.NotePitchConverter;
import fr.utarwyn.superjukebox.music.model.Layer;
import fr.utarwyn.superjukebox.music.model.Note;
import fr.utarwyn.superjukebox.util.ActionBarUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

/**
 * Capable of playing musics from SuperJukeboxes!
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class MusicPlayer implements Runnable {

    /**
     * Jukebox object which using this player
     */
    private final Jukebox jukebox;

    /**
     * Stores the current music played by the jukebox.
     */
    private Music currentMusic;

    private boolean playing;

    private boolean destroyed;

    private int tick;

    private BukkitTask task;

    public MusicPlayer(Jukebox jukebox) {
        this.jukebox = jukebox;
        this.tick = -1;

        this.destroyed = false;
        this.playing = false;
    }

    public boolean isTaskRunning() {
        return this.task != null;
    }

    public synchronized Music getCurrentMusic() {
        return this.currentMusic;
    }

    public synchronized void setCurrentMusic(Music music) {
        this.currentMusic = music;
    }

    public synchronized void playNextMusic() {
        List<Music> musics = this.jukebox.getMusics();
        if (!musics.isEmpty()) {
            int current = musics.indexOf(this.currentMusic);
            int next = (current + 1) % musics.size();

            this.setCurrentMusic(musics.get(next));
            this.start();
        }
    }

    public synchronized void runTask() {
        this.task = Bukkit.getScheduler().runTaskAsynchronously(SuperJukebox.getInstance(), this);
    }

    public synchronized void resume() {
        this.playing = true;
    }

    public synchronized void pause() {
        this.playing = false;
    }

    public synchronized void start() {
        // Before we have to run this task if needed
        if (!this.isTaskRunning()) {
            this.runTask();
        }

        this.tick = -1;
        this.resume();

        // Send announcements to all players
        boolean announcements = this.jukebox.getSettings().getAnnouncements().getValue();
        if (announcements) {
            // TODO: only send to players which can hear the music!
            ActionBarUtil.sendActionTitleToAllPlayers(String.format("§a♫ %s §e(%s)",
                    this.currentMusic.getName(), this.currentMusic.getOriginalAuthor()));
        }
    }

    public synchronized void stop() {
        this.pause();
        this.tick = -1;
    }

    public synchronized void destroy() {
        this.destroyed = true;
        this.currentMusic = null;
        this.task = null;
    }

    @Override
    public void run() {
        while (!this.destroyed) {
            long startTime = System.currentTimeMillis();

            synchronized (this) {
                if (this.playing && this.canPlay()) {
                    this.tick++;

                    if (this.tick > this.currentMusic.getLength()) {
                        this.stop();

                        // Check for autoplay!
                        boolean autoplay = this.jukebox.getSettings().getAutoplay().getValue();
                        if (autoplay) {
                            this.playNextMusic();
                        }

                        continue;
                    }

                    if (!Bukkit.getOnlinePlayers().isEmpty()) {
                        Bukkit.getScheduler().runTask(SuperJukebox.getInstance(), this::playTick);
                    }
                }
            }

            if (this.destroyed) {
                break;
            }

            long duration = System.currentTimeMillis() - startTime;
            float delay = this.currentMusic.getDelay() * 50;

            if (duration < delay) {
                try {
                    Thread.sleep((long) (delay - duration));
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
        }
    }

    private void playTick() {
        int nbNote = 0;
        Block block = this.jukebox.getBlock();
        Location loc = block.getLocation().clone().add(0.5, -0.5, 0.5);

        for (Layer layer : this.currentMusic.getLayers().values()) {
            Note note = layer.getNote(this.tick);
            if (note == null) continue;

            Bukkit.getOnlinePlayers().forEach(player -> player.playNote(
                    loc, InstrumentConverter.getBukkitInstrument(note.getInstrument()),
                    new org.bukkit.Note(note.getKey() - 33)
            ));

            float volume = ((layer.getVolume() * (this.jukebox.getSettings().getVolume().getValue() / 100f)
                    * (int) note.getVelocity()) / 100_000_000f)
                    * ((1f / 16f) * this.jukebox.getSettings().getDistance().getValue());
            float pitch = NotePitchConverter.getPitch(note);

            block.getWorld().playSound(
                    loc, InstrumentConverter.getInstrument(note.getInstrument()),
                    volume, pitch
            );

            nbNote++;
        }

        // Play particles at the same time if needed!
        boolean particlesEnabled = this.jukebox.getSettings().getParticles().getValue();
        if (nbNote > 0 && particlesEnabled) {
            FastParticle.spawnParticle(
                    block.getWorld(), ParticleType.NOTE,
                    block.getLocation().clone().add(.5, 1.2, .5),
                    nbNote, .3f, .3f, .3f
            );
        }
    }

    private boolean canPlay() {
        return !this.jukebox.getSettings().getPlayWithRedstone().getValue()
                || this.jukebox.getBlock().isBlockIndirectlyPowered();
    }

}
