package fr.utarwyn.superjukebox.music;

import fr.utarwyn.superjukebox.music.model.Layer;

import java.util.ArrayList;
import java.util.List;

public class Music {

	private short length;

	private short height;

	private String name;

	private String author;

	private String description;

	private float tempo;

	private float delay;

	private List<Layer> layers;

	public Music(short length, short height, String name, String author, String desc, float tempo) {
		this.length = length;
		this.height = height;
		this.name = name;
		this.author = author;
		this.description = desc;
		this.tempo = tempo;
		this.delay = 20 / this.tempo;

		this.layers = new ArrayList<>();
	}

	public List<Layer> getLayers() {
		return new ArrayList<>(this.layers);
	}

	public short getLength() {
		return this.length;
	}

	public short getHeight() {
		return this.height;
	}

	public String getName() {
		return this.name;
	}

	public String getAuthor() {
		return this.author;
	}

	public String getDescription() {
		return this.description;
	}

	public float getTempo() {
		return this.tempo;
	}

	public float getDelay() {
		return this.delay;
	}

	public Layer getLayerOrDefault(int key) {
		for (Layer l : this.layers)
			if (l.getKey() == key)
				return l;

		Layer layer = new Layer(key);
		this.layers.add(layer);
		return layer;
	}

}
