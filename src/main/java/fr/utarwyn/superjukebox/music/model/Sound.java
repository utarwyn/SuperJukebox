package fr.utarwyn.superjukebox.music.model;

import java.util.HashMap;
import java.util.Map;

public enum Sound {

    NOTE_PIANO("NOTE_PIANO", "BLOCK_NOTE_HARP", "BLOCK_NOTE_BLOCK_HARP"),
    NOTE_BASS("NOTE_BASS", "BLOCK_NOTE_BASS", "BLOCK_NOTE_BLOCK_BASS"),
    NOTE_BASS_DRUM("NOTE_BASS_DRUM", "BLOCK_NOTE_BASEDRUM", "BLOCK_NOTE_BLOCK_BASEDRUM"),
    NOTE_SNARE_DRUM("NOTE_SNARE_DRUM", "BLOCK_NOTE_SNARE", "BLOCK_NOTE_BLOCK_SNARE"),
    NOTE_STICKS("NOTE_STICKS", "BLOCK_NOTE_HAT", "BLOCK_NOTE_BLOCK_HAT"),
    NOTE_BASS_GUITAR("NOTE_BASS_GUITAR", "BLOCK_NOTE_GUITAR", "BLOCK_NOTE_BLOCK_GUITAR"),
    NOTE_FLUTE("NOTE_FLUTE", "BLOCK_NOTE_FLUTE", "BLOCK_NOTE_BLOCK_FLUTE"),
    NOTE_BELL("NOTE_BELL", "BLOCK_NOTE_BELL", "BLOCK_NOTE_BLOCK_BELL"),
    NOTE_CHIME("NOTE_CHIME", "BLOCK_NOTE_CHIME", "BLOCK_NOTE_BLOCK_CHIME"),
    NOTE_XYLOPHONE("NOTE_XYLOPHONE", "BLOCK_NOTE_XYLOPHONE", "BLOCK_NOTE_BLOCK_XYLOPHONE"),
    NOTE_PLING("NOTE_PLING", "BLOCK_NOTE_PLING", "BLOCK_NOTE_BLOCK_PLING"),
    NOTE_IRON_XYLOPHONE("BLOCK_NOTE_BLOCK_IRON_XYLOPHONE"),
    NOTE_COW_BELL("BLOCK_NOTE_BLOCK_COW_BELL"),
    NOTE_DIDGERIDOO("BLOCK_NOTE_BLOCK_DIDGERIDOO"),
    NOTE_BIT("BLOCK_NOTE_BLOCK_BIT"),
    NOTE_BANJO("BLOCK_NOTE_BLOCK_BANJO");

    private static final Map<String, org.bukkit.Sound> CACHED_SOUND_MAP;

    static {
        CACHED_SOUND_MAP = new HashMap<>();

        for (Sound sound : values()) {
            for (String soundName : sound.names) {
                CACHED_SOUND_MAP.put(soundName.toUpperCase(), sound.getSound());
            }
        }
    }

    private final String[] names;
    private org.bukkit.Sound cached = null;

    Sound(String... names) {
        this.names = names;
    }

    public static org.bukkit.Sound getFromBukkitName(String bukkitSoundName) {
        org.bukkit.Sound sound = CACHED_SOUND_MAP.get(bukkitSoundName.toUpperCase());
        if (sound != null) {
            return sound;
        } else {
            return org.bukkit.Sound.valueOf(bukkitSoundName);
        }
    }

    public org.bukkit.Sound getSound() {
        if (this.cached != null) {
            return this.cached;
        }

        for (String name : names) {
            try {
                this.cached = org.bukkit.Sound.valueOf(name);
                return this.cached;
            } catch (IllegalArgumentException ignore) {
                // Exception ignored, just try next name
            }
        }
        return null;
    }

}
