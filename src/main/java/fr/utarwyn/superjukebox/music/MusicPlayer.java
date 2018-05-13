package fr.utarwyn.superjukebox.music;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.jukebox.Jukebox;
import fr.utarwyn.superjukebox.music.model.Layer;
import fr.utarwyn.superjukebox.music.model.Note;
import org.bukkit.Bukkit;
import org.inventivetalent.particle.ParticleEffect;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MusicPlayer implements Runnable {

	private Jukebox jukebox;

	private boolean playing;

	private boolean destroyed;

	private int tick;

	private boolean taskRunned;

	private final Lock lock = new ReentrantLock();

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

	public synchronized void start() {
		this.playing = true;
	}

	public synchronized void stop() {
		this.playing = false;
	}

	public synchronized void restart() {
		this.tick = -1;
		this.start();
	}

	public synchronized void destroy() {
		lock.lock();

		try {
			this.destroyed = true;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void run() {
		while (!this.destroyed) {
			long start = System.currentTimeMillis();

			lock.lock();

			synchronized (this) {
				if (this.playing) {
					this.tick++;

					if (this.tick > this.jukebox.getCurrentMusic().getLength()) {
						this.playing = false;
						this.tick = -1;

						// Check for autoplay!
						if (this.jukebox.isAutoplay()) {
							this.jukebox.nextMusic();
							this.playing = true;
						}

						return;
					}

					if (Bukkit.getOnlinePlayers().size() > 0)
						Bukkit.getScheduler().runTask(SuperJukebox.getInstance(), this::playTick);
				}
			}

			lock.unlock();
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
					(1/16f) * this.jukebox.getDistance(),
					note.getPitch().getPitch()
			);

			nbNote++;
		}

		// Play particles at the same time!
		if (nbNote > 0)
			ParticleEffect.NOTE.send(
					Bukkit.getOnlinePlayers(),
					this.jukebox.getBlock().getLocation().clone().add(.5, 1.2, .5),
					.3f, .3f, .3f, 1f, nbNote
			);
	}

}
