package fr.utarwyn.superjukebox.menu;

import fr.utarwyn.superjukebox.jukebox.Jukebox;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class JukeboxSettingsMenu extends AbstractMenu {

	private Jukebox jukebox;

	private Player player;

	private Map<String, ItemStack> settingItems;

	private ItemStack permissionItem;

	private AbstractMenu permissionsMenu;

	JukeboxSettingsMenu(AbstractMenu parentMenu, Jukebox jukebox, Player player) {
		super(3, "SuperJukebox settings menu");

		this.jukebox = jukebox;
		this.player = player;
		this.settingItems = new HashMap<>();

		this.permissionsMenu = new JukeboxPermissionsMenu(this, this.jukebox, this.player);

		this.setParentMenu(parentMenu);
		this.prepare();
	}

	@Override
	public void prepare() {
		// Setings items
		this.createSettingItem(
				10, Material.ENDER_PEARL,
				"setDistance", "Distance",
				this.jukebox.getSettings().getDistance()
		);
		this.createSettingItem(
				11, Material.BEACON,
				"setVolume", "Volume",
				this.jukebox.getSettings().getVolume()
		);
		this.createSettingItem(
				12, Material.DIAMOND,
				"setUseGlobalMusics", "Global musics",
				this.jukebox.getSettings().usesGlobalMusics()
		);
		this.createSettingItem(
				13, Material.REDSTONE_COMPARATOR,
				"setAutoplay", "Autoplay",
				this.jukebox.getSettings().isAutoplay()
		);

		// Permissions item
		this.permissionItem = new ItemStack(Material.BLAZE_POWDER);
		ItemMeta permissionMeta = this.permissionItem.getItemMeta();

		permissionMeta.setDisplayName(ChatColor.GOLD + "Manage permissions");
		permissionMeta.setLore(Arrays.asList(
				ChatColor.GRAY + "Click here to manage",
				ChatColor.GRAY + "permission of this jukebox!"
		));

		this.permissionItem.setItemMeta(permissionMeta);
		this.setItem(16, this.permissionItem);

		// Separators
		for (int i = 0; i < 10; i++) {
			this.setItem(i, SEPARATOR);
		}
		for (int i = 17; i < 27; i++) {
			if (i == 18) continue;
			this.setItem(i, SEPARATOR);
		}

		// Back item
		this.setItem(18, BACK_ITEM);
	}

	@Override
	public boolean onClick(Player player, int slot) {
		ItemStack item = this.getItemAt(slot);

		// Permissions item
		if (item.equals(this.permissionItem)) {
			this.permissionsMenu.open(this.player);
		}

		return true;
	}

	@Override
	public void onClose(Player player) {
		// TODO Save settings on the disk...
	}

	private void createSettingItem(int slot, Material material, String setter, String settingName, Object value) {
		ItemStack itemStack = new ItemStack(material);
		ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + settingName);

		List<String> lore = new ArrayList<>(Arrays.asList(
				"",
				ChatColor.GRAY + "Current value: " + this.formatSettingValue(value),
				""
		));

		// Adding action lore

		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);

		this.setItem(slot, itemStack);
		this.settingItems.put(setter, itemStack);
	}

	private String formatSettingValue(Object value) {
		switch (value.getClass().getSimpleName()) {
			case "Boolean":
				if ((Boolean) value) {
					return ChatColor.GREEN + "Yes";
				} else {
					return ChatColor.RED + "No";
				}
			case "Integer":
				return ChatColor.AQUA.toString() + value;
			default:
				return ChatColor.DARK_GRAY + "- error -";
		}
	}

}
