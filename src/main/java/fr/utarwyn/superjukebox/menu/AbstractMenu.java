package fr.utarwyn.superjukebox.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a menu generated by the plugin.
 * It has many improvements instead of a simple {@link org.bukkit.inventory.InventoryHolder}.
 * Events onClick and onClose are supported.
 *
 * @since 1.0.0
 * @author Utarwyn
 */
public abstract class AbstractMenu implements InventoryHolder {

	/**
	 * The separator item used for many menus
	 */
	static final ItemStack SEPARATOR = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1, (byte) 15);

	/**
	 * The number of rows of the menu
	 */
	private int rows;

	/**
	 * The title of the menu
	 */
	private String title;

	/**
	 * All items in the menu
	 */
	Map<Integer, ItemStack> items;

	/**
	 * The generated inventory
	 */
	private Inventory inventory;

	/**
	 * True if the size has to be dynamically calculated before the creation of the menu
	 */
	private boolean dynamicSize;

	/**
	 * Construct a menu with a number of rows and a title
	 * @param rows Number of rows of the container
	 * @param title Title of the container
	 */
	AbstractMenu(int rows, String title) {
		this.rows = rows;
		this.title = title;
		this.items = new HashMap<>();

		if (this.title.length() > 32)
			this.title = this.title.substring(0, 32);

		Menus.registerMenu(this);
	}

	/**
	 * Construct a menu with a title
	 * @param title Title of the container
	 */
	AbstractMenu(String title) {
		this(-1, title);
		this.dynamicSize = true;
	}

	static {
		ItemMeta separatorMeta = SEPARATOR.getItemMeta();
		separatorMeta.setDisplayName(ChatColor.BLACK + "");
		SEPARATOR.setItemMeta(separatorMeta);
	}

	/**
	 * Returns the number of filled slots in the container
	 * @return Number of fileld slots
	 */
	public int getFilledSlotsNb() {
		Map<Integer, ItemStack> copy = new HashMap<>(this.items);

		copy.entrySet().removeIf((entry) -> entry.getValue() == null);
		return copy.size();
	}

	/**
	 * Open the container to a specific player
	 * @param player Player that will receive the container
	 */
	public void open(Player player) {
		player.openInventory(this.getInventory());
	}

	/**
	 * Set an item in a specific position in the container
	 * @param position The position where the item will be setted
	 * @param item The item to set
	 */
	public void setItem(int position, ItemStack item) {
		if (!this.items.containsKey(position))
			this.items.put(position, item);
	}

	/**
	 * Set the number of rows of the container
	 * @param rows Number of rows
	 */
	public void setRows(int rows) {
		this.rows = rows;

		if (this.inventory != null && this.inventory.getViewers().size() == 0)
			this.inventory = null;
	}

	/**
	 * Returns an item at a given position in the container
	 * @param position Position where to search for an item
	 * @return The item found at the position
	 */
	public ItemStack getItemAt(int position) {
		return this.items.get(position);
	}

	/**
	 * Remove an item at a given position
	 * @param position The position where to remove the item
	 */
	public void removeItemAt(int position) {
		this.items.remove(position);
	}

	/**
	 * Clear all items of the menu
	 */
	public void clear() {
		this.items.clear();
	}

	/**
	 * Returns all the viewers of the container. Uses the method {@link Inventory#getViewers()}.
	 * @return The list of viewers of the container
	 */
	public List<HumanEntity> getViewers() {
		return this.inventory == null ? new ArrayList<>() : this.inventory.getViewers();
	}

	/**
	 * Update all items in the container with items stored in the menu.
	 * (Not the same object, so the container has to be updated sometimes and vice versa)
	 */
	void updateItems() {
		// Update item list with items in the inventory
		for (int i = 0; i < this.inventory.getSize(); i++) {
			ItemStack itemStack = this.inventory.getItem(i);

			if (itemStack != null)
				this.items.put(i, itemStack);
			else
				this.items.remove(i);
		}
	}

	/**
	 * Returns the generated inventory with items stored before
	 * @return The generated inventory
	 */
	@Override
	public Inventory getInventory() {
		if (this.inventory == null) {
			int size = this.getSize();

			if (this.dynamicSize) {
				int maxPos = 0;

				for (Integer n : this.items.keySet())
					if (n > maxPos)
						maxPos = n;

				size = ((int) Math.ceil((maxPos + 1) / 9.0D)) * 9;

				if (size <  1) size =  1;
				if (size > 54) size = 54;
			}

			this.inventory = Bukkit.createInventory(this, size, this.title);
		}

		if (this.inventory.getViewers().size() == 0)
			for (Integer index : items.keySet()) {
				if (index < 0 || index >= this.inventory.getSize())
					continue;

				ItemStack i = items.get(index);
				this.inventory.setItem(index, i);
			}

		return this.inventory;
	}

	/**
	 * Returns the size of the container (mesured in slots)
	 * @return Size of the chest's container
	 */
	private int getSize() {
		return this.rows * 9;
	}

	/**
	 * Called after the initialization of the menu
	 * and before the creation of the inventory.
	 * (This method is mainly used to set all items in the container)
	 */
	public abstract void prepare();

	/**
	 * Called when a player click on an item in the menu
	 * @param player The player who interacts with the menu
	 * @param slot The slot where the player has clicked
	 * @return True if the event has to be cancelled
	 */
	public abstract boolean onClick(Player player, int slot);

	/**
	 * Called when a player closes the menu
	 * @param player The player who closes the menu
	 */
	public abstract void onClose(Player player);

}
