package fr.utarwyn.superjukebox.music.model;

/**
 * A music note
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class Note {

    private Instrument instrument;

    private NotePitch pitch;

    public Note(byte instrumentKey, byte pitch) {
        this.instrument = Instrument.get(instrumentKey);
        this.pitch = NotePitch.get(pitch - 33);
    }

    public Instrument getInstrument() {
        return this.instrument;
    }

    public NotePitch getPitch() {
        return this.pitch;
    }

}
