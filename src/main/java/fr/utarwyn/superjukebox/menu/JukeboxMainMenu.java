package fr.utarwyn.superjukebox.menu;

import fr.utarwyn.superjukebox.jukebox.Jukebox;
import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.util.JUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class JukeboxMainMenu extends AbstractMenu {

	private static final int MUSICS_PER_PAGE = 27;

	private Jukebox jukebox;

	private Player player;

	private int currentPage;

	private ItemStack settingItem;

	private ItemStack musicsItem;

	private AbstractMenu settingsMenu;

	public JukeboxMainMenu(Jukebox jukebox, Player player) {
		super(4, "SuperJukebox main menu");

		this.jukebox = jukebox;
		this.player = player;
		this.currentPage = 1;

		this.prepare();
	}

	@Override
	public void prepare() {
		List<Music> musics = this.jukebox.getMusics();

		// Clear any previous item
		this.clear();

		// Settings items
		if (this.jukebox.getSettings().getEditSettingsPerm().has(this.player)) {
			this.settingItem = new ItemStack(Material.COMMAND);
			ItemMeta settingMeta = this.settingItem.getItemMeta();

			settingMeta.setDisplayName(ChatColor.YELLOW + "Jukebox's settings");
			settingMeta.setLore(Arrays.asList("§7Click to change the settings", "§7of this jukebox!"));
			this.settingItem.setItemMeta(settingMeta);

			this.setItem(28, this.settingItem);
		} else {
			this.setItem(28, SEPARATOR);
		}

		// Music management item
		if (this.jukebox.getSettings().getEditMusicsPerm().has(this.player)) {
			this.musicsItem = new ItemStack(Material.DIAMOND);
			ItemMeta musicsMeta = this.musicsItem.getItemMeta();

			musicsMeta.setDisplayName(ChatColor.GOLD + "Manage jukebox musics");
			if (this.jukebox.getSettings().getUseGlobalMusics().getValue()) {
				musicsMeta.setLore(Arrays.asList(
						"§cYou can't edit this jukebox's musics", "§cbecause §lthey are managed globally§c.",
						"§dYou can allow custom musics by changing the", "§doption in the settings menu of this jukbox."
				));
			} else {
				musicsMeta.setLore(Arrays.asList("§7Click to edit musics", "§7of this jukebox!"));
				musicsMeta.addEnchant(Enchantment.DURABILITY, 3, true);
				musicsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			this.musicsItem.setItemMeta(musicsMeta);

			this.setItem(29, this.musicsItem);
		} else {
			this.setItem(29, SEPARATOR);
		}

		// Page items
		if (musics.size() > MUSICS_PER_PAGE) {
			// TODO
		} else {
			this.setItem(33, SEPARATOR);
			this.setItem(34, SEPARATOR);
		}

		// Separators
		this.setItem(27, SEPARATOR);
		this.setItem(30, SEPARATOR);
		this.setItem(31, SEPARATOR);
		this.setItem(32, SEPARATOR);
		this.setItem(35, SEPARATOR);

		// Music items
		if (!musics.isEmpty()) {
			int begin = (this.currentPage - 1) * MUSICS_PER_PAGE;

			for (int i = begin; i < begin + MUSICS_PER_PAGE && i < musics.size(); i++)
				this.setItem(i % MUSICS_PER_PAGE, musics.get(i).getIcon());
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
	public void onClick(InventoryClickEvent event) {
		// Click on a music disc!
		if (event.getSlot() < MUSICS_PER_PAGE) {
			int musicId = (this.currentPage - 1) * MUSICS_PER_PAGE + event.getSlot();

			if (musicId < this.jukebox.getMusics().size()) {
				Music music = this.jukebox.getMusics().get(musicId);

				this.jukebox.play(music);
				this.player.sendMessage(ChatColor.GREEN + "Have a great moment with " + ChatColor.YELLOW + music.getName() + ChatColor.GREEN + " !");
			}
		}

		// Settings menu item
		if (this.settingItem != null && event.getCurrentItem().equals(this.settingItem)) {
			// Open the setting menu in another Thread
			JUtil.runSync(() -> {
				if (this.settingsMenu == null) {
					this.settingsMenu = new JukeboxSettingsMenu(this, this.jukebox);
				}

				this.settingsMenu.prepare();
				this.settingsMenu.open(this.player);
			});
		}

		event.setCancelled(true);
	}

	@Override
	public void onClose(Player player) {

	}

}
