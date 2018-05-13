package fr.utarwyn.superjukebox.menu;

import fr.utarwyn.superjukebox.jukebox.Jukebox;
import fr.utarwyn.superjukebox.music.Music;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class JukeboxMainMenu extends AbstractMenu {

	private static final int MUSICS_PER_PAGE = 27;

	private Jukebox jukebox;

	private Player player;

	public JukeboxMainMenu(Jukebox jukebox, Player player) {
		super(4, "SuperJukebox main menu");

		this.jukebox = jukebox;
		this.player = player;

		this.prepare();
	}

	@Override
	public void prepare() {
		List<Music> musics = this.jukebox.getMusics();

		// Settings items
		ItemStack settingItem = new ItemStack(Material.COMMAND);
		ItemMeta settingMeta = settingItem.getItemMeta();

		settingMeta.setDisplayName(ChatColor.YELLOW + "Jukebox's settings");
		settingMeta.setLore(Arrays.asList("§7Click to change the settings", "§7of this jukebox!"));
		settingItem.setItemMeta(settingMeta);

		this.setItem(28, settingItem);

		// Page items
		if (musics.size() > MUSICS_PER_PAGE) {
			// TODO
		} else {
			this.setItem(33, AbstractMenu.SEPARATOR);
			this.setItem(34, AbstractMenu.SEPARATOR);
		}

		// Separators
		this.setItem(27, AbstractMenu.SEPARATOR);
		this.setItem(32, AbstractMenu.SEPARATOR);
		this.setItem(35, AbstractMenu.SEPARATOR);

		// Music items
		if (!musics.isEmpty()) {
			// TODO
		} else {
			ItemStack noMusicItem = new ItemStack(Material.BARRIER);
			ItemMeta noMusicMeta = noMusicItem.getItemMeta();

			noMusicMeta.setDisplayName(ChatColor.RED + "✖ No music here!");
			noMusicMeta.setLore(Arrays.asList("§7Add music by choosing musics", "§7from the server list with", "§7the appropriated item below!"));
			noMusicItem.setItemMeta(noMusicMeta);

			this.setItem(13, noMusicItem);
		}
	}

	@Override
	public boolean onClick(Player player, int slot) {
		return true;
	}

	@Override
	public void onClose(Player player) {

	}

}
