package fr.utarwyn.superjukebox.menu;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.jukebox.Jukebox;
import fr.utarwyn.superjukebox.jukebox.JukeboxesManager;
import fr.utarwyn.superjukebox.jukebox.perm.Permission;
import fr.utarwyn.superjukebox.jukebox.perm.PermissionType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JukeboxPermissionsMenu extends AbstractMenu {

	private Jukebox jukebox;

	private Map<Permission, ItemStack> permissionItems;

	JukeboxPermissionsMenu(AbstractMenu parentMenu, Jukebox jukebox) {
		super(3, "Superjukebox permissions menu");

		this.jukebox = jukebox;
		this.permissionItems = new HashMap<>();

		this.setParentMenu(parentMenu);
	}

	@Override
	public void prepare() {
		// Clear any previous data
		this.clear();
		this.permissionItems.clear();

		// Create permission items
		this.createPermissionItem(10, Material.JUKEBOX, this.jukebox.getSettings().getInteractPerm());
		this.createPermissionItem(11, Material.RECORD_3, this.jukebox.getSettings().getEditMusicsPerm());
		this.createPermissionItem(12, Material.COMMAND, this.jukebox.getSettings().getEditSettingsPerm());

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
	public void onClick(InventoryClickEvent event) {
		for (Map.Entry<Permission, ItemStack> entry : this.permissionItems.entrySet()) {
			if (entry.getValue().equals(event.getCurrentItem())) {
				Permission permission = entry.getKey();

				// Redefine the type of the permission ...
				permission.setType(permission.getType().next());

				// ... and only the display name/lore of the current item!
				this.updateItemMeta(this.getItemAt(event.getSlot()), permission);
				this.updateInventory();
				break;
			}
		}

		event.setCancelled(true);
	}

	@Override
	public void onClose(Player player) {
		// Save all permissions on the disk
		SuperJukebox.getInstance().getInstance(JukeboxesManager.class).saveJukeboxPermissionsOnDisk(this.jukebox);
	}

	/**
	 * Creates an itemstack for a particular SuperJukebox permission.
	 * This stores also the itemstack in a hasmap for the onclick Bukkit event.
	 *
	 * @param slot Slot where the item have to be added
	 * @param material Material used for the itemstack
	 * @param permission Permission on which the item is based
	 */
	private void createPermissionItem(int slot, Material material, Permission permission) {
		ItemStack item = new ItemStack(material);

		// Update the item just created with all needed information
		this.updateItemMeta(item, permission);

		// Add the item to the inventory and to memory
		this.setItem(slot, item);
		this.permissionItems.put(permission, item);
	}

	/**
	 * Update an itemstack with informations about a permission
	 * @param itemStack The itemstack which have to be updated
	 * @param permission The permission used to do the update
	 */
	private void updateItemMeta(ItemStack itemStack, Permission permission) {
		ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Permission " + ChatColor.GOLD + ChatColor.UNDERLINE + permission.getBukkitPermission().toUpperCase());

		List<String> lore = new ArrayList<>();
		lore.add("");

		lore.add(ChatColor.GRAY + "Current state:");
		for (PermissionType permissionType : PermissionType.values()) {
			if (permissionType.equals(permission.getType())) {
				lore.add(ChatColor.GREEN + " - " + permissionType.name() + " ✔");
			} else {
				lore.add(ChatColor.DARK_GRAY + " - " + permissionType.name());
			}
		}

		lore.add("");
		lore.add(ChatColor.LIGHT_PURPLE + "Click to switch the state!");

		itemMeta.setLore(lore);
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
		itemStack.setItemMeta(itemMeta);
	}

}
