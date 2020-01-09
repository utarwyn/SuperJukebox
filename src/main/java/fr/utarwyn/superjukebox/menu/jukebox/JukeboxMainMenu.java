package fr.utarwyn.superjukebox.menu.jukebox;

import fr.utarwyn.superjukebox.jukebox.Jukebox;
import fr.utarwyn.superjukebox.menu.AbstractMenu;
import fr.utarwyn.superjukebox.menu.MusicDiscsMenu;
import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.util.JUtil;
import fr.utarwyn.superjukebox.util.MaterialHelper;
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

    private ItemStack musicStopItem;

    private AbstractMenu settingsMenu;

    private AbstractMenu musicAddMenu;

    private JukeboxMenuEditingMode editingMode;

    public JukeboxMainMenu(Jukebox jukebox, Player player) {
        super("§6SuperJukebox §7main menu", player);

        this.jukebox = jukebox;
        this.editingMode = JukeboxMenuEditingMode.DISABLE;

        this.prepare();
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

    @Override
    public void prepare() {
        // Reprepare all discs items
        super.prepare();

        // Settings items
        if (this.jukebox.getSettings().getEditSettingsPerm().has(this.player)) {
            this.settingItem = new ItemStack(MaterialHelper.findMaterial("COMMAND", "COMMAND_BLOCK"));
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
            boolean globalMusics = this.jukebox.getSettings().getUseGlobalMusics().getValue();

            musicsMeta.setDisplayName(ChatColor.GOLD + "Adding musics");
            if (globalMusics) {
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
            ItemStack musicEditingModeItem = new ItemStack(Material.BEACON);
            ItemMeta musicEditingMeta = musicEditingModeItem.getItemMeta();

            musicEditingMeta.setDisplayName(ChatColor.GOLD + "Menu editing mode");
            if (globalMusics) {
                musicEditingMeta.setLore(Arrays.asList(
                        "§cYou can't edit musics of this jukebox", "§cbecause §lthey are managed globally§c.",
                        "§dYou can allow custom musics by changing the", "§doption in the settings menu of this jukbox."
                ));
            } else {
                JukeboxMainMenu.updateEditingModeItem(musicEditingMeta, this.editingMode);
                musicEditingMeta.addEnchant(Enchantment.DURABILITY, 3, true);
                musicEditingMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            musicEditingModeItem.setItemMeta(musicEditingMeta);

            this.removeItemAt(30);
            this.setItem(30, musicEditingModeItem);

            // Music PauseItem
            this.musicStopItem = new ItemStack(Material.BARRIER);
            ItemMeta musicStopMeta = this.musicStopItem.getItemMeta();

            musicStopMeta.setDisplayName(ChatColor.GOLD + "Stop music");
            if (this.jukebox.getPlayer().isTaskRunned()) {
                musicStopMeta.setLore(Arrays.asList("§cPress this iten to", "§cstop the music you're playing§c."));
            } else {
                musicStopMeta.setLore(Arrays.asList("§7You can't stop music", "§7that's not playing!"));
                musicStopMeta.addEnchant(Enchantment.DURABILITY, 3, true);
                musicStopMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            this.musicStopItem.setItemMeta(musicStopMeta);

            this.removeItemAt(31);
            this.setItem(31, this.musicStopItem);
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
        boolean globalMusics = this.jukebox.getSettings().getUseGlobalMusics().getValue();
        if (this.musicAddItem != null && this.musicAddItem.equals(event.getCurrentItem()) && !globalMusics) {
            // Open the menu in another Thread
            JUtil.runSync(() -> {
                if (this.musicAddMenu == null) {
                    this.musicAddMenu = new JukeboxAddMusicMenu(this.jukebox, this.player, this);
                }

                this.musicAddMenu.prepare();
                this.musicAddMenu.open(this.player);
            });
        }
        if (this.musicStopItem != null && this.musicStopItem.equals(event.getCurrentItem())) {
            this.jukebox.getPlayer().pause();

            this.clear();
            this.prepare();
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
        // Not implemented
    }

}
