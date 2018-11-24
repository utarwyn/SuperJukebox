package fr.utarwyn.superjukebox.bukkit.music.model;

import fr.utarwyn.superjukebox.bukkit.util.MinecraftVersion;
import org.bukkit.Sound;

/**
 * A music instrument
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public enum Instrument {

	HARP(0, "NOTE_PIANO", "BLOCK_NOTE_HARP"),
	BASS(1, "NOTE_BASS_GUITAR", "BLOCK_NOTE_BASS"),
	BASEDRUM(2, "NOTE_BASS_DRUM", "BLOCK_NOTE_BASEDRUM"),
	SNARE(3, "NOTE_SNARE_DRUM", "BLOCK_NOTE_SNARE"),
	HAT(4, "NOTE_STICKS", "BLOCK_NOTE_HAT"),
	GUITAR(5, "BLOCK_NOTE_GUITAR"),
	FLUTE(6, "BLOCK_NOTE_FLUTE"),
	BELL(7, "BLOCK_NOTE_BELL"),
	CHIME(8, "BLOCK_NOTE_CHIME"),
	XYLOPHONE(9, "BLOCK_NOTE_XYLOPHONE");

	private int key;

	private String pre1_9;

	private String post1_9;

	private String post1_12;

	private Sound mySound;

	Instrument(int key, String pre1_9, String post1_9) {
		this.key = key;
		this.pre1_9 = pre1_9;
		this.post1_9 = post1_9;
	}

	Instrument(int key, String post1_12) {
		this.key = key;
		this.post1_12 = post1_12;
	}

	public Sound getSound() {
		// Sound is cached to gain performance
		if (this.mySound != null)
			return this.mySound;

		String sound;

		if (MinecraftVersion.current() == MinecraftVersion.POST1_12) {
			sound = (this.post1_12 != null) ? this.post1_12 : this.post1_9;
		} else if (MinecraftVersion.current() == MinecraftVersion.PRE1_9) {
			sound = this.pre1_9;
		} else {
			sound = this.post1_9;
		}

		return this.mySound = Sound.valueOf(sound);
	}

	public static Instrument get(byte key) {
		for (Instrument ins : Instrument.values())
			if (ins.key == key)
				return ins;

		return Instrument.HARP;
	}

}
