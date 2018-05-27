package fr.utarwyn.superjukebox.jukebox.settings;

public class Setting<T> {

	private String key;

	private T value;

	Setting(String key, T defaultValue) {
		this.key = key;
		this.value = defaultValue;
	}

	public String getKey() {
		return this.key;
	}

	public String getJavaType() {
		return (this.value != null) ? this.value.getClass().getSimpleName() : "null";
	}

	public T getValue() {
		return this.value;
	}

	public void setValue(T value) {
		if (this.checkValue(value)) {
			this.value = value;
		}
	}

	public boolean checkValue(T value) {
		return true;
	}

}
