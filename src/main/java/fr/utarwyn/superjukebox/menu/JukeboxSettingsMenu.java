package fr.utarwyn.superjukebox.menu;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.jukebox.Jukebox;
import fr.utarwyn.superjukebox.jukebox.JukeboxesManager;
import fr.utarwyn.superjukebox.jukebox.settings.Setting;
import fr.utarwyn.superjukebox.util.JUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class JukeboxSettingsMenu extends AbstractMenu {

	private Jukebox jukebox;

	private Map<Setting, ItemStack> settingItems;

	private ItemStack permissionItem;

	private AbstractMenu permissionsMenu;

	JukeboxSettingsMenu(AbstractMenu parentMenu, Jukebox jukebox) {
		super(4, "SuperJukebox settings menu");

		this.jukebox = jukebox;
		this.settingItems = new HashMap<>();

		this.setParentMenu(parentMenu);
	}

	@Override
	public void prepare() {

		// Clear all previous data
		this.clear();
		this.settingItems.clear();

		// Setings items
		this.createSettingItem(
				10, Material.ENDER_PEARL,
				"Distance",
				this.jukebox.getSettings().getDistance()
		);
		this.createSettingItem(
				11, Material.BEACON,
				"Volume",
				this.jukebox.getSettings().getVolume()
		);
		this.createSettingItem(
				12, Material.DIAMOND,
				"Global musics",
				this.jukebox.getSettings().getUseGlobalMusics()
		);
		this.createSettingItem(
				13, Material.REDSTONE_COMPARATOR,
				"Autoplay",
				this.jukebox.getSettings().getAutoplay()
		);
		this.createSettingItem(
				14, Material.NOTE_BLOCK,
				"Particles",
				this.jukebox.getSettings().getParticles()
		);
		this.createSettingItem(
                19, Material.REDSTONE,
                "Play with redstone",
                this.jukebox.getSettings().getPlayWithRedstone()
        );
        this.createSettingItem(
                20, Material.SIGN,
                "Announce music",
                this.jukebox.getSettings().getAnnouncements()
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
		for (int i = 17; i < 19; i++) {
		    this.setItem(i, SEPARATOR);
        }
		for (int i = 26; i < 36; i++) {
			if (i == 27) continue;
			this.setItem(i, SEPARATOR);
		}

		// Back item
		this.setItem(27, BACK_ITEM);
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		ItemStack itemStack = this.getItemAt(event.getSlot());

		// Setting item
		for (Map.Entry<Setting, ItemStack> entry : this.settingItems.entrySet()) {
			if (entry.getValue().equals(itemStack)) {
				Setting setting = entry.getKey();

				// Update the setting in memory in terms of the player's action
				switch (setting.getJavaType()) {
					case "Boolean":
						setting.setValue(!(Boolean) setting.getValue());
						break;

					case "Integer":
						int difference, newValue;

						// Left click = minus — Right click = plus
						if (event.getClick().isLeftClick()) {
							difference = -1;
						} else {
							difference = 1;
						}

						// Shift = 5 — Default = 1
						if (event.getClick().isShiftClick()) {
							difference *= 5;
						}

						// Apply the new value if possible
						newValue = (Integer) setting.getValue() + difference;
						if (setting.isValid(newValue)) {
							setting.setValue(newValue);
						}
						break;
				}

				// Update metadatas of the current itemstack and update the inventory!
				itemStack.setItemMeta(this.updateSettingItemLore(setting, itemStack.getItemMeta()));
				this.updateInventory();

				break;
			}
		}

		// Permissions item
		if (itemStack.equals(this.permissionItem)) {
			// Open the permission menu in another Thread
			JUtil.runSync(() -> {
				if (this.permissionsMenu == null) {
					this.permissionsMenu = new JukeboxPermissionsMenu(this, this.jukebox);
				}

				this.permissionsMenu.prepare();
				this.permissionsMenu.open((Player) event.getWhoClicked());
			});
		}

		event.setCancelled(true);
	}

	@Override
	public void onClose(Player player) {
		// Save all settings on the disk
		SuperJukebox.getInstance().getInstance(JukeboxesManager.class).saveJukeboxSettingsOnDisk(this.jukebox);
	}

	private void createSettingItem(int slot, Material material, String settingName, Setting setting) {
		ItemStack itemStack = new ItemStack(material);
		ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + settingName);
		itemMeta = this.updateSettingItemLore(setting, itemMeta);

		itemStack.setItemMeta(itemMeta);

		this.setItem(slot, itemStack);
		this.settingItems.put(setting, itemStack);
	}

	private ItemMeta updateSettingItemLore(Setting setting, ItemMeta itemMeta) {
		List<String> lore = new ArrayList<>(Arrays.asList(
				"",
				ChatColor.GRAY + "Current value: " + this.formatSettingValue(setting),
				""
		));

		switch (setting.getJavaType()) {
			case "Boolean":
				lore.add(ChatColor.LIGHT_PURPLE + "Click to switch the value!");
				break;

			case "Integer":
				lore.add(ChatColor.RED + ChatColor.BOLD.toString() + "-5: " + ChatColor.LIGHT_PURPLE + "shift-left click");
				lore.add(ChatColor.RED + ChatColor.BOLD.toString() + "-1: " + ChatColor.LIGHT_PURPLE + "left click");
				lore.add(ChatColor.GREEN + ChatColor.BOLD.toString() + "+1: " + ChatColor.LIGHT_PURPLE + "right click");
				lore.add(ChatColor.GREEN + ChatColor.BOLD.toString() + "+5: " + ChatColor.LIGHT_PURPLE + "shift-right click");
				break;
		}

		itemMeta.setLore(lore);
		return itemMeta;
	}

	private String formatSettingValue(Setting setting) {
		switch (setting.getJavaType()) {
			case "Boolean":
				if ((Boolean) setting.getValue()) {
					return ChatColor.GREEN + "Yes";
				} else {
					return ChatColor.RED + "No";
				}

			case "Integer":
				return ChatColor.AQUA.toString() + setting.getValue();

			default:
				return ChatColor.DARK_GRAY + "- error -";
		}
	}

}
