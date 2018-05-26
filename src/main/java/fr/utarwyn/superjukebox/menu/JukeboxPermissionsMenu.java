package fr.utarwyn.superjukebox.menu;

import fr.utarwyn.superjukebox.jukebox.Jukebox;
import fr.utarwyn.superjukebox.jukebox.perm.Permission;
import fr.utarwyn.superjukebox.jukebox.perm.PermissionType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JukeboxPermissionsMenu extends AbstractMenu {

	private Jukebox jukebox;

	private Player player;

	private Map<Permission, ItemStack> permissionItems;

	JukeboxPermissionsMenu(AbstractMenu parentMenu, Jukebox jukebox, Player player) {
		super(3, "Superjukebox permissions menu");

		this.jukebox = jukebox;
		this.player = player;
		this.permissionItems = new HashMap<>();

		this.setParentMenu(parentMenu);
		this.prepare();
	}

	@Override
	public void prepare() {
		// Clear any previous items
		this.clear();

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
	public boolean onClick(Player player, int slot) {
		ItemStack item = this.getItemAt(slot);

		for (Map.Entry<Permission, ItemStack> entry : this.permissionItems.entrySet()) {
			if (entry.getValue().equals(item)) {
				Permission permission = entry.getKey();

				// Redefine the type of the permission ...
				permission.setType(permission.getType().next());

				// ... and update the entire menu!
				this.prepare();
				this.updateInventory();
				break;
			}
		}

		return true;
	}

	@Override
	public void onClose(Player player) {

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
		ItemMeta itemMeta = item.getItemMeta();

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
		item.setItemMeta(itemMeta);

		// Add the item to the inventory and to memory
		this.setItem(slot, item);
		this.permissionItems.put(permission, item);
	}

}
