package fr.utarwyn.superjukebox.bukkit.menu.jukebox;

/**
 * Represents an edition mode for the jukebox main menu!
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public enum JukeboxMenuEditingMode {

	DISABLE("No edition"),
	SWAP("swap two musics"),
	REMOVE("remove music");

	private String title;

	JukeboxMenuEditingMode(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

}
