package fr.utarwyn.superjukebox.music.model;

import org.bukkit.Sound;

/**
 * A music instrument
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public enum Instrument {

	HARP(0, "BLOCK_NOTE_BLOCK_HARP", "BLOCK_NOTE_HARP", "NOTE_PIANO"),
	BASS(1, "BLOCK_NOTE_BLOCK_BASS", "BLOCK_NOTE_BASS", "NOTE_BASS"),
	BASEDRUM(2, "BLOCK_NOTE_BLOCK_BASEDRUM", "BLOCK_NOTE_BASEDRUM", "NOTE_BASS_DRUM"),
	SNARE(3, "BLOCK_NOTE_BLOCK_SNARE", "BLOCK_NOTE_SNARE", "NOTE_SNARE_DRUM"),
	HAT(4, "BLOCK_NOTE_BLOCK_HAT", "BLOCK_NOTE_HAT", "NOTE_STICKS"),
	GUITAR(5, "BLOCK_NOTE_BLOCK_GUITAR", "BLOCK_NOTE_GUITAR", "NOTE_BASS_GUITAR"),
	FLUTE(6, "BLOCK_NOTE_BLOCK_FLUTE", "BLOCK_NOTE_FLUTE"),
	BELL(7, "BLOCK_NOTE_BLOCK_BELL", "BLOCK_NOTE_BELL"),
	CHIME(8, "BLOCK_NOTE_BLOCK_CHIME", "BLOCK_NOTE_CHIME"),
	XYLOPHONE(9, "BLOCK_NOTE_BLOCK_XYLOPHONE", "BLOCK_NOTE_XYLOPHONE"),
	// 1.14+ instruments
	IRON_XYLOPHONE(10, "BLOCK_NOTE_BLOCK_IRON_XYLOPHONE"),
	COW_BELL(11, "BLOCK_NOTE_BLOCK_COW_BELL"),
	DIDGERIDOO(12, "BLOCK_NOTE_BLOCK_DIDGERIDOO"),
	BIT(13, "BLOCK_NOTE_BLOCK_BIT"),
	BANJO(14, "BLOCK_NOTE_BLOCK_BANJO"),
	PLING(15, "BLOCK_NOTE_BLOCK_PLING");

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
					this.bukkitSound = Sound.valueOf(name);
					return this.bukkitSound;
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
