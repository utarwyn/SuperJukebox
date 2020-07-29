package fr.utarwyn.superjukebox.music.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A music layer
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class Layer {

    private final Map<Integer, Note> notes;
    private byte volume;
    private int panning;

    public Layer() {
        this.notes = new HashMap<>();
    }

    public Note getNote(int tick) {
        return this.notes.get(tick);
    }

    public int getPanning() {
        return panning;
    }

    public void setPanning(int panning) {
        this.panning = panning;
    }

    public void addNote(int tick, Note note) {
        this.notes.put(tick, note);
    }

    public byte getVolume() {
        return volume;
    }

    public void setVolume(byte volume) {
        this.volume = volume;
    }

}
