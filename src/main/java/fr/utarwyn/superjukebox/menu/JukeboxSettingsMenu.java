package fr.utarwyn.superjukebox.menu;

import fr.utarwyn.superjukebox.jukebox.Jukebox;
import org.bukkit.entity.Player;

public class JukeboxSettingsMenu extends AbstractMenu {

	private Jukebox jukebox;

	private Player player;

	JukeboxSettingsMenu(Jukebox jukebox, Player player) {
		super(4, "SuperJukebox settings menu");

		this.jukebox = jukebox;
		this.player = player;
	}

	@Override
	public void prepare() {
		// Perms item
		// TODO
	}

	@Override
	public boolean onClick(Player player, int slot) {
		return true;
	}

	@Override
	public void onClose(Player player) {

	}

}
