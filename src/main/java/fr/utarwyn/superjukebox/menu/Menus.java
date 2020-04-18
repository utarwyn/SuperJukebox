package fr.utarwyn.superjukebox.menu;

import fr.utarwyn.superjukebox.SuperJukebox;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages all menus of the plugin.
 * Used as a listener too, this class makes the creation
 * of menus a very simple action.
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class Menus implements Listener {

    /**
     * Collection of all menus
     */
    private static Set<AbstractMenu> menuSet;

    /**
     * No constructor, its an utility class
     */
    private Menus() {
    }

    /**
     * Register an AbstractMenu
     *
     * @param menu Menu to register
     */
    static void registerMenu(AbstractMenu menu) {
        if (menuSet == null) {
            menuSet = new HashSet<>();
            Bukkit.getPluginManager().registerEvents(new Menus(), SuperJukebox.getInstance());
        }

        menuSet.add(menu);
    }

    /**
     * Close all registered menus for everyone connected on the server
     */
    public static void closeAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Inventory openInv = player.getOpenInventory().getTopInventory();
            if (openInv.getType() == InventoryType.ENDER_CHEST || menuSet == null) {
                player.closeInventory();
                continue;
            }

            for (AbstractMenu menu : menuSet) {
                if (openInv.getHolder() == menu) {
                    menu.updateItems();
                    menu.onClose(player);

                    player.closeInventory();
                    break;
                }
            }
        }
    }

    /**
     * Called when a player click in an inventory.
     * Used to detect an interaction with one of registered menus.
     *
     * @param event The inventory click event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        AbstractMenu menu = this.getMenuFromInventory(event.getInventory());
        Player player = (Player) event.getWhoClicked();

        if (menu == null || event.getSlot() < 0)
            return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
            return;

        // Click on a back item!
        if (menu.getParentMenu() != null && event.getCurrentItem().equals(AbstractMenu.BACK_ITEM)) {
            event.setCancelled(true);
            menu.goToParentMenu(player);
            return;
        }

        menu.onClick(event);
    }

    /**
     * Called when a player close an inventory.
     * Used to detect a closure of one of registered menus.
     *
     * @param event The inventory close event
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        AbstractMenu menu = this.getMenuFromInventory(event.getInventory());
        Player player = (Player) event.getPlayer();
        if (menu == null) return;

        menu.updateItems();
        menu.onClose(player);
    }

    /**
     * Gets a registered menu linked to a given inventory
     *
     * @param inventory Inventory to check
     * @return Menu if found, otherwise null
     */
    private AbstractMenu getMenuFromInventory(Inventory inventory) {
        for (AbstractMenu menu : menuSet)
            if (menu == inventory.getHolder())
                return menu;

        return null;
    }

}
