package fr.utarwyn.superjukebox.music.model;

import org.bukkit.Sound;

/**
 * A music instrument
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public enum Instrument {

	HARP(0, "NOTE_PIANO", "BLOCK_NOTE_HARP", "BLOCK_NOTE_BLOCK_HARP"),
	BASS(1, "NOTE_BASS", "BLOCK_NOTE_BASS", "BLOCK_NOTE_BLOCK_BASS"),
	BASEDRUM(2, "NOTE_BASS_DRUM", "BLOCK_NOTE_BASEDRUM", "BLOCK_NOTE_BLOCK_BASEDRUM"),
	SNARE(3, "NOTE_SNARE_DRUM", "BLOCK_NOTE_SNARE", "BLOCK_NOTE_BLOCK_SNARE"),
	HAT(4, "NOTE_STICKS", "BLOCK_NOTE_HAT", "BLOCK_NOTE_BLOCK_HAT"),
	GUITAR(5, "NOTE_BASS_GUITAR", "BLOCK_NOTE_GUITAR", "BLOCK_NOTE_BLOCK_GUITAR", "BLOCK_NOTE_BASS"),
	FLUTE(6, "BLOCK_NOTE_FLUTE"),
	BELL(7, "BLOCK_NOTE_BELL"),
	CHIME(8, "BLOCK_NOTE_CHIME"),
	XYLOPHONE(9, "BLOCK_NOTE_XYLOPHONE");

	private int key;

	private String[] versionDependentNames;

	private Sound bukkitSound;

	Instrument(int key, String... versionDependentNames) {
		this.key = key;
		this.versionDependentNames = versionDependentNames;
	}

	public Sound getSound() {
		// If the sound is not stored in cache yet, we have to search for it
		if (this.bukkitSound == null) {
			for (String name : versionDependentNames) {
				try {
					return this.bukkitSound = Sound.valueOf(name);
				} catch (IllegalArgumentException ignore) {
					// Try the next one in the list
				}
			}

			throw new IllegalArgumentException("Sound of " + this.name() + " cannot be resolved.");
		}

		return this.bukkitSound;
	}

	public static Instrument get(byte key) {
		for (Instrument ins : Instrument.values())
			if (ins.key == key)
				return ins;

		return Instrument.HARP;
	}

}
