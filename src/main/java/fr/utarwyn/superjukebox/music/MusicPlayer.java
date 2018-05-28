package fr.utarwyn.superjukebox.music;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.jukebox.Jukebox;
import fr.utarwyn.superjukebox.music.model.Layer;
import fr.utarwyn.superjukebox.music.model.Note;
import org.bukkit.Bukkit;
import org.inventivetalent.particle.ParticleEffect;

public class MusicPlayer implements Runnable {

	private Jukebox jukebox;

	private boolean playing;

	private boolean destroyed;

	private int tick;

	private boolean taskRunned;

	public MusicPlayer(Jukebox jukebox) {
		this.jukebox = jukebox;
		this.tick = -1;

		this.destroyed = false;
		this.playing = false;
	}

	public boolean isTaskRunned() {
		return this.taskRunned;
	}

	public synchronized void runTask() {
		if (this.taskRunned) return;

		Bukkit.getScheduler().runTaskAsynchronously(SuperJukebox.getInstance(), this);
		this.taskRunned = true;
	}

	public synchronized void resume() {
		this.playing = true;
	}

	public synchronized void pause() {
		this.playing = false;
	}

	public synchronized void start() {
		this.tick = -1;
		this.resume();
	}

	public synchronized void stop() {
		this.pause();
		this.tick = -1;
	}

	public synchronized void destroy() {
		this.destroyed = true;
	}

	@Override
	public void run() {
		while (!this.destroyed) {
			long start = System.currentTimeMillis();

			synchronized (this) {
				if (this.playing && this.canPlay()) {
					this.tick++;

					if (this.tick > this.jukebox.getCurrentMusic().getLength()) {
						this.playing = false;
						this.tick = -1;

						// Check for autoplay!
						if (this.jukebox.getSettings().getAutoplay().getValue())
							this.jukebox.playNext();

						return;
					}

					if (Bukkit.getOnlinePlayers().size() > 0)
						Bukkit.getScheduler().runTask(SuperJukebox.getInstance(), this::playTick);
				}
			}

			if (this.destroyed) break;

			long diff = System.currentTimeMillis() - start;
			float delay = this.jukebox.getCurrentMusic().getDelay() * 50;

			if (diff < delay) {
				try {
					Thread.sleep((long) (delay - diff));
				} catch (InterruptedException ignored) {  }
			}
		}
	}

	private void playTick() {
		int nbNote = 0;

		for (Layer layer : this.jukebox.getCurrentMusic().getLayers()) {
			Note note = layer.getNote(this.tick);
			if (note == null) continue;

			this.jukebox.getBlock().getWorld().playSound(
					this.jukebox.getBlock().getLocation(),
					note.getInstrument().getSound(),
					(1/16f) * this.jukebox.getSettings().getDistance().getValue() *
							(this.jukebox.getSettings().getVolume().getValue() / 100f),
					note.getPitch().getPitch()
			);

			nbNote++;
		}

		// Play particles at the same time if needed!
		if (nbNote > 0 && this.jukebox.getSettings().getParticles().getValue()) {
			ParticleEffect.NOTE.send(
					Bukkit.getOnlinePlayers(),
					this.jukebox.getBlock().getLocation().clone().add(.5, 1.2, .5),
					.3f, .3f, .3f, 1f, nbNote
			);
		}
	}

	private boolean canPlay() {
		return !this.jukebox.getSettings().getPlayWithRedstone().getValue() || this.jukebox.getBlock().isBlockIndirectlyPowered();
	}

}
