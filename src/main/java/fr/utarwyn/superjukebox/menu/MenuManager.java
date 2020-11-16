package fr.utarwyn.superjukebox.menu;

import fr.utarwyn.superjukebox.AbstractManager;
import fr.utarwyn.superjukebox.SuperJukebox;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

/**
 * Manages all menus of the plugin.
 * Used as a listener too, this class makes the creation
 * of menus a very simple action.
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class MenuManager extends AbstractManager {

    public MenuManager() {
        super(SuperJukebox.getInstance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        // not implemented
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void unload() {
        // not implemented
    }

    /**
     * Called when a player click in an inventory.
     * Used to detect an interaction with one of registered menus.
     *
     * @param event The inventory click event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getView().getTopInventory();
        Optional<AbstractMenu> menu = this.getMenuFromInventory(inventory);

        int slot = event.getRawSlot();
        boolean validSlot = slot >= 0 && slot < inventory.getSize();

        if (menu.isPresent()) {
            // A restricted move is when player clicks in the menu or uses shift click
            event.setCancelled(validSlot || event.isShiftClick());

            // Perform the action only when player clicks on a valid slot of the menu
            if (validSlot) {
                // Handle back item
                if (menu.get().getParentMenu() != null && AbstractMenu.BACK_ITEM.equals(event.getCurrentItem())) {
                    menu.get().goToParentMenu((Player) event.getWhoClicked());
                    return;
                }

                menu.get().onClick(event);
            }
        }
    }

    /**
     * Called when a player close an inventory.
     * Used to detect a closure of one of registered menus.
     *
     * @param event The inventory close event
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Optional<AbstractMenu> menu = this.getMenuFromInventory(event.getInventory());
        menu.ifPresent(m -> {
            m.updateItems();
            m.onClose((Player) event.getPlayer());
        });
    }

    /**
     * Close all menus for everyone connected on the server.
     */
    public void closeAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Inventory inventory = player.getOpenInventory().getTopInventory();
            Optional<AbstractMenu> menu = this.getMenuFromInventory(inventory);

            if (menu.isPresent()) {
                menu.get().updateItems();
                menu.get().onClose(player);
                player.closeInventory();
            }
        }
    }

    /**
     * Gets a registered menu linked to a given inventory
     *
     * @param inventory Inventory to check
     * @return Menu if found, otherwise null
     */
    private Optional<AbstractMenu> getMenuFromInventory(Inventory inventory) {
        boolean isCustom = inventory.getHolder() instanceof AbstractMenu;
        return isCustom ? Optional.of((AbstractMenu) inventory.getHolder()) : Optional.empty();
    }

}
