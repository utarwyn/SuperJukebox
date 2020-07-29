package fr.utarwyn.superjukebox.music.model;

/**
 * A music note.
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class Note {

    private final byte instrument;

    private final byte key;

    private final byte velocity;

    private final int panning;

    private final short pitch;

    public Note(byte instrument, byte key) {
        this(instrument, key, (byte) 100, (byte) 100, (short) 0);
    }

    public Note(byte instrument, byte key, byte velocity, int panning, short pitch) {
        this.instrument = instrument;
        this.key = key;
        this.velocity = velocity;
        this.panning = panning;
        this.pitch = pitch;
    }

    public byte getInstrument() {
        return instrument;
    }

    public byte getKey() {
        return key;
    }

    public short getPitch() {
        return pitch;
    }

    public byte getVelocity() {
        return velocity;
    }

    public int getPanning() {
        return panning;
    }

}
