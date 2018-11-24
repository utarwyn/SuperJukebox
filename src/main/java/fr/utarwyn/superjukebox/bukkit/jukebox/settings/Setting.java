package fr.utarwyn.superjukebox.bukkit.jukebox.settings;

import java.util.function.Predicate;

/**
 * Represents a jukebox setting!
 * (a java type must be provided to deals with the setting)
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class Setting<T> {

	private String key;

	private T value;

	private Predicate<T> predicate;

	Setting(String key, T defaultValue, Predicate<T> predicate) {
		this.key = key;
		this.value = defaultValue;
		this.predicate = predicate;
	}

	Setting(String key, T defaultValue) {
		this(key, defaultValue, null);
	}

	public String getKey() {
		return this.key;
	}

	public String getJavaType() {
		return (this.value != null) ? this.value.getClass().getSimpleName() : "null";
	}

	public boolean isValid(T value) {
		return this.predicate == null || this.predicate.test(value);
	}

	public T getValue() {
		return this.value;
	}

	public void setValue(T value) {
		if (this.isValid(value)) {
			this.value = value;
		}
	}

}
