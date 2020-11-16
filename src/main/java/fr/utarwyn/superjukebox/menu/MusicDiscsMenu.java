package fr.utarwyn.superjukebox.menu;

import fr.utarwyn.superjukebox.music.Music;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public abstract class MusicDiscsMenu extends AbstractMenu {

    private static final int MUSICS_PER_PAGE = 27;

    private static final int PAGINATION_SLOT = 33;

    protected Player player;

    private int currentPage;

    protected MusicDiscsMenu(String title, Player player) {
        this(title, player, null);
    }

    protected MusicDiscsMenu(String title, Player player, AbstractMenu parentMenu) {
        super(4, title);

        this.player = player;
        this.currentPage = 1;

        if (parentMenu != null) {
            this.setParentMenu(parentMenu);
        }
    }

    @Override
    public void prepare() {
        // Clear all items at the beginning
        this.clear();

        // Discs items
        List<Music> musicList = this.getMusicList();
        int musicCount = musicList.size();
        int begin = (this.currentPage - 1) * MUSICS_PER_PAGE;

        if (musicCount > 0) {
            for (int i = begin; i < begin + MUSICS_PER_PAGE && i < musicCount; i++) {
                this.setItem(i % MUSICS_PER_PAGE, musicList.get(i).getIcon());
            }
        } else {
            ItemStack noMusicItem = new ItemStack(Material.BARRIER);
            ItemMeta noMusicMeta = noMusicItem.getItemMeta();

            noMusicMeta.setDisplayName(ChatColor.RED + "✖ No music here!");
            noMusicMeta.setLore(Arrays.asList("§7Add music by choosing musics", "§7from the server list with", "§7the appropriated item below!"));
            noMusicItem.setItemMeta(noMusicMeta);

            this.setItem(13, noMusicItem);
        }

        // Prepare the bottom bar
        int beginBottomBar = 27;

        if (this.getParentMenu() != null) {
            this.setItem(beginBottomBar++, BACK_ITEM);
        }

        for (int i = beginBottomBar; i < 36; i++) {
            this.setItem(i, SEPARATOR);
        }

        // Prepare pagination items
        if (this.currentPage > 1) {
            this.setItem(PAGINATION_SLOT, AbstractMenu.PREV_PAGE_ITEM);
        }
        if (musicCount > begin + MUSICS_PER_PAGE) {
            this.setItem(PAGINATION_SLOT + 1, AbstractMenu.NEXT_PAGE_ITEM);
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.getSlot() < MUSICS_PER_PAGE) {
            List<Music> musicList = this.getMusicList();
            int musicId = (this.currentPage - 1) * MUSICS_PER_PAGE + event.getSlot();

            if (musicId < musicList.size()) {
                Music music = musicList.get(musicId);
                this.onDiscClick(event, music);
            }
        }

        // Handle pagination items
        Material currentMaterial = event.getCurrentItem() != null ? event.getCurrentItem().getType() : null;
        boolean isPaginationitem = currentMaterial == AbstractMenu.PREV_PAGE_ITEM.getType();

        if (isPaginationitem && event.getSlot() == PAGINATION_SLOT) {
            this.currentPage--;
            this.prepare();
            this.updateInventory();
        } else if (isPaginationitem && event.getSlot() == PAGINATION_SLOT + 1) {
            this.currentPage++;
            this.prepare();
            this.updateInventory();
        }

        event.setCancelled(true);
    }

    /**
     * Method called when a player click on a disc in the menu!
     *
     * @param event Event fired by Bukkit
     * @param music Music object represented by the clicked disc
     */
    public abstract void onDiscClick(InventoryClickEvent event, Music music);

    /**
     * Method called to get the list of music discs to be displayed!
     *
     * @return Music discs needed
     */
    public abstract List<Music> getMusicList();

}
