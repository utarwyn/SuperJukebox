package fr.utarwyn.superjukebox.util;

import org.bukkit.Bukkit;

public enum MinecraftVersion {

	PRE1_9,
	PRE1_11,
	PRE1_12,
	POST1_12;

	private static MinecraftVersion current;

	public static MinecraftVersion current() {
		if (current != null)
			return current;

		String version = Bukkit.getVersion();

		if (version.contains("1.8"))
			current = PRE1_9;
		else if (version.contains("1.9") || version.contains("1.10"))
			current = PRE1_11;
		else if (version.contains("1.11"))
			current = PRE1_12;
		else
			current = POST1_12;

		return current;
	}

}
