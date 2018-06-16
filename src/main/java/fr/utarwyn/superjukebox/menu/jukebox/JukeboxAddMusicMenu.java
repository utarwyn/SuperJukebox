package fr.utarwyn.superjukebox.menu.jukebox;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.jukebox.Jukebox;
import fr.utarwyn.superjukebox.menu.AbstractMenu;
import fr.utarwyn.superjukebox.menu.MusicDiscsMenu;
import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.MusicManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

/**
 * The music add menu of a SuperJukebox!
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class JukeboxAddMusicMenu extends MusicDiscsMenu {

	private MusicManager manager;

	private Jukebox jukebox;

	JukeboxAddMusicMenu(Jukebox jukebox, Player player, AbstractMenu parentMenu) {
		super("SuperJukebox musics menu", player, new ArrayList<>());

		this.manager = SuperJukebox.getInstance().getInstance(MusicManager.class);
		this.jukebox = jukebox;
		this.setMusicList(this.manager.getMusics());
		this.setParentMenu(parentMenu);
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		super.onClick(event);

	}

	@Override
	public void onDiscClick(InventoryClickEvent event, Music music) {

	}

	@Override
	public void onClose(Player player) {

	}

}
