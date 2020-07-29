package fr.utarwyn.superjukebox.music.converters;

import fr.utarwyn.superjukebox.music.model.Sound;
import fr.utarwyn.superjukebox.util.ServerVersion;
import org.bukkit.Instrument;

public class InstrumentConverter {

    private InstrumentConverter() {
        // Utility class
    }

    public static org.bukkit.Sound getInstrument(byte instrument) {
        return Sound.getFromBukkitName(getInstrumentName(instrument));
    }

    public static String getInstrumentName(byte instrument) {
        switch (instrument) {
            case 1:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_BASS").name();
            case 2:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_BASEDRUM").name();
            case 3:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_SNARE").name();
            case 4:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_HAT").name();
            case 5:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_GUITAR").name();
            case 6:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_FLUTE").name();
            case 7:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_BELL").name();
            case 8:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_CHIME").name();
            case 9:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_XYLOPHONE").name();
            case 10:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_IRON_XYLOPHONE").name();
            case 11:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_COW_BELL").name();
            case 12:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_DIDGERIDOO").name();
            case 13:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_BIT").name();
            case 14:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_BANJO").name();
            case 15:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_PLING").name();
            default:
                return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_HARP").name();
        }
    }

    public static Instrument getBukkitInstrument(byte instrument) {
        switch (instrument) {
            case 0:
                return Instrument.PIANO;
            case 1:
                return Instrument.BASS_GUITAR;
            case 2:
                return Instrument.BASS_DRUM;
            case 3:
                return Instrument.SNARE_DRUM;
            case 4:
                return Instrument.STICKS;
            default:
                if (ServerVersion.isNewerThan(ServerVersion.V1_11)) {
                    switch (instrument) {
                        case 5:
                            return Instrument.valueOf("GUITAR");
                        case 6:
                            return Instrument.valueOf("FLUTE");
                        case 7:
                            return Instrument.valueOf("BELL");
                        case 8:
                            return Instrument.valueOf("CHIME");
                        case 9:
                            return Instrument.valueOf("XYLOPHONE");
                        default:
                            if (ServerVersion.isNewerThan(ServerVersion.V1_13)) {
                                switch (instrument) {
                                    case 10:
                                        return Instrument.valueOf("IRON_XYLOPHONE");
                                    case 11:
                                        return Instrument.valueOf("COW_BELL");
                                    case 12:
                                        return Instrument.valueOf("DIDGERIDOO");
                                    case 13:
                                        return Instrument.valueOf("BIT");
                                    case 14:
                                        return Instrument.valueOf("BANJO");
                                    case 15:
                                        return Instrument.valueOf("PLING");
                                    default:
                                        return Instrument.PIANO;
                                }
                            }
                            return Instrument.PIANO;
                    }
                }
                return Instrument.PIANO;
        }
    }

}
