package fr.utarwyn.superjukebox.menu.jukebox;

import fr.utarwyn.superjukebox.jukebox.Jukebox;
import fr.utarwyn.superjukebox.menu.AbstractMenu;
import fr.utarwyn.superjukebox.menu.MusicDiscsMenu;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The main menu of a SuperJukebox!
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class JukeboxMainMenu extends MusicDiscsMenu {

	private Jukebox jukebox;

	private ItemStack settingItem;

	private ItemStack musicAddItem;

	private ItemStack musicEditingModeItem;

	private AbstractMenu settingsMenu;

	private AbstractMenu musicAddMenu;

	private JukeboxMenuEditingMode editingMode;

	public JukeboxMainMenu(Jukebox jukebox, Player player) {
		super("SuperJukebox main menu", player);

		this.jukebox = jukebox;
		this.editingMode = JukeboxMenuEditingMode.DISABLE;

		this.prepare();
	}

	@Override
	public void prepare() {
		// Reprepare all discs items
		super.prepare();

		// Settings items
		if (this.jukebox.getSettings().getEditSettingsPerm().has(this.player)) {
			this.settingItem = new ItemStack(Material.COMMAND);
			ItemMeta settingMeta = this.settingItem.getItemMeta();

			settingMeta.setDisplayName(ChatColor.YELLOW + "Jukebox's settings");
			settingMeta.setLore(Arrays.asList("§7Click to change the settings", "§7of this jukebox!"));
			this.settingItem.setItemMeta(settingMeta);

			this.removeItemAt(28);
			this.setItem(28, this.settingItem);
		}

		// Music adding/editing items
		if (this.jukebox.getSettings().getEditMusicsPerm().has(this.player)) {
			// Music adding item
			this.musicAddItem = new ItemStack(Material.DIAMOND);
			ItemMeta musicsMeta = this.musicAddItem.getItemMeta();

			musicsMeta.setDisplayName(ChatColor.GOLD + "Adding musics");
			if (this.jukebox.getSettings().getUseGlobalMusics().getValue()) {
				musicsMeta.setLore(Arrays.asList(
						"§cYou can't add music to this jukebox", "§cbecause §lthey are managed globally§c.",
						"§dYou can allow custom musics by changing the", "§doption in the settings menu of this jukbox."
				));
			} else {
				musicsMeta.setLore(Arrays.asList("§7Click to add music", "§7in this jukebox!"));
				musicsMeta.addEnchant(Enchantment.DURABILITY, 3, true);
				musicsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			this.musicAddItem.setItemMeta(musicsMeta);

			this.removeItemAt(29);
			this.setItem(29, this.musicAddItem);

			// Music editing item
			this.musicEditingModeItem = new ItemStack(Material.BEACON);
			ItemMeta musicEditingMeta = this.musicEditingModeItem.getItemMeta();

			musicEditingMeta.setDisplayName(ChatColor.GOLD + "Menu editing mode");
			if (this.jukebox.getSettings().getUseGlobalMusics().getValue()) {
				musicEditingMeta.setLore(Arrays.asList(
						"§cYou can't edit musics of this jukebox", "§cbecause §lthey are managed globally§c.",
						"§dYou can allow custom musics by changing the", "§doption in the settings menu of this jukbox."
				));
			} else {
				JukeboxMainMenu.updateEditingModeItem(musicEditingMeta, this.editingMode);
				musicEditingMeta.addEnchant(Enchantment.DURABILITY, 3, true);
				musicEditingMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			this.musicEditingModeItem.setItemMeta(musicEditingMeta);

			this.removeItemAt(30);
			this.setItem(30, this.musicEditingModeItem);
		}
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		super.onClick(event);

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

		// Music add menu item
		if (this.musicAddItem != null && event.getCurrentItem().equals(this.musicAddItem) && !this.jukebox.getSettings().getUseGlobalMusics().getValue()) {
			// Open the menu in another Thread
			JUtil.runSync(() -> {
				if (this.musicAddMenu == null) {
					this.musicAddMenu = new JukeboxAddMusicMenu(this.jukebox, this.player, this);
				}

				this.musicAddMenu.prepare();
				this.musicAddMenu.open(this.player);
			});
		}
	}

	@Override
	public void onDiscClick(InventoryClickEvent event, Music music) {
		this.jukebox.play(music);
		this.player.sendMessage(ChatColor.GREEN + "Have a great moment with " + ChatColor.YELLOW + music.getName() + ChatColor.GREEN + " !");
	}

	@Override
	public List<Music> getMusicList() {
		return this.jukebox.getMusics();
	}

	@Override
	public void onClose(Player player) {

	}

	private static void updateEditingModeItem(ItemMeta meta, JukeboxMenuEditingMode currentEditingMode) {
		List<String> lore = new ArrayList<>(Arrays.asList(
				"",
				ChatColor.GRAY + "Click to choose the editing mode",
				ChatColor.GRAY + "you want for this menu!",
				""
		));

		for (JukeboxMenuEditingMode editingMode : JukeboxMenuEditingMode.values()) {
			if (editingMode == currentEditingMode) {
				lore.add(ChatColor.GREEN + " - " + editingMode.getTitle() + " ✔");
			} else {
				lore.add(ChatColor.DARK_RED + " - " + editingMode.getTitle());
			}
		}

		meta.setLore(lore);
	}

}
