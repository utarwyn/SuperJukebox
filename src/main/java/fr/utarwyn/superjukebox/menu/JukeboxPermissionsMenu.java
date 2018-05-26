package fr.utarwyn.superjukebox.menu;

import fr.utarwyn.superjukebox.jukebox.Jukebox;
import org.bukkit.entity.Player;

public class JukeboxPermissionsMenu extends AbstractMenu {

	private Jukebox jukebox;

	private Player player;

	JukeboxPermissionsMenu(AbstractMenu parentMenu, Jukebox jukebox, Player player) {
		super(3, "Superjukebox permissions menu");

		this.jukebox = jukebox;
		this.player = player;

		this.setParentMenu(parentMenu);
		this.prepare();
	}

	@Override
	public void prepare() {
		// TODO

		// Separators
		for (int i = 0; i < 10; i ++) {
			this.setItem(i, SEPARATOR);
		}
		for (int i = 17; i < 27; i ++) {
			if (i == 18) continue;
			this.setItem(i, SEPARATOR);
		}

		// Back item
		this.setItem(18, BACK_ITEM);
	}

	@Override
	public boolean onClick(Player player, int slot) {
		return true;
	}

	@Override
	public void onClose(Player player) {

	}

}
