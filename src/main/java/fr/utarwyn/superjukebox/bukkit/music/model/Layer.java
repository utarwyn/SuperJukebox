package fr.utarwyn.superjukebox.bukkit.music.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A music layer
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class Layer {

	private int key;

	private Map<Integer, Note> notes;

	public Layer(int key) {
		this.key = key;
		this.notes = new HashMap<>();
	}

	public int getKey() {
		return this.key;
	}

	public Note getNote(int tick) {
		return this.notes.get(tick);
	}

	public void addNote(int tick, Note note) {
		this.notes.put(tick, note);
	}

}
