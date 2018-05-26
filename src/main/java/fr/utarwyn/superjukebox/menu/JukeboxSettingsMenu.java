package fr.utarwyn.superjukebox.menu;

import fr.utarwyn.superjukebox.jukebox.Jukebox;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class JukeboxSettingsMenu extends AbstractMenu {

	private Jukebox jukebox;

	private Player player;

	private ItemStack permissionItem;

	private AbstractMenu permissionsMenu;

	JukeboxSettingsMenu(AbstractMenu parentMenu, Jukebox jukebox, Player player) {
		super(3, "SuperJukebox settings menu");

		this.jukebox = jukebox;
		this.player = player;

		this.permissionsMenu = new JukeboxPermissionsMenu(this, this.jukebox, this.player);

		this.setParentMenu(parentMenu);
		this.prepare();
	}

	@Override
	public void prepare() {
		// Setings items
		// TODO

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

		// Permissions item
		if (item.equals(this.permissionItem)) {
			this.permissionsMenu.open(this.player);
		}

		return true;
	}

	@Override
	public void onClose(Player player) {

	}

}
