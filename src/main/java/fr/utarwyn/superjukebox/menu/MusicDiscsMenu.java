package fr.utarwyn.superjukebox.menu;

import fr.utarwyn.superjukebox.music.Music;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MusicDiscsMenu extends AbstractMenu {

	private static final int MUSICS_PER_PAGE = 27;

	protected Player player;

	private List<Music> musicList;

	private int currentPage;

	public MusicDiscsMenu(String title, Player player, List<Music> musicList) {
		this(title, player, musicList, null);
	}

	public MusicDiscsMenu(String title, Player player, List<Music> musicList, AbstractMenu parentMenu) {
		super(4, title);

		this.player = player;
		this.setMusicList(musicList);
		this.currentPage = 1;

		if (parentMenu != null) {
			this.setParentMenu(parentMenu);
		}
	}

	public void setMusicList(List<Music> musicList) {
		this.musicList = new ArrayList<>(musicList);
	}

	@Override
	public void prepare() {
		// Clear all items at the beginning
		this.clear();

		// Discs items
		int musicCount = this.musicList.size();

		if (musicCount > 0) {
			int begin = (this.currentPage - 1) * MUSICS_PER_PAGE;

			for (int i = begin; i < begin + MUSICS_PER_PAGE && i < musicCount; i++) {
				this.setItem(i % MUSICS_PER_PAGE, this.musicList.get(i).getIcon());
			}
		} else {
			ItemStack noMusicItem = new ItemStack(Material.BARRIER);
			ItemMeta noMusicMeta = noMusicItem.getItemMeta();

			noMusicMeta.setDisplayName(ChatColor.RED + "✖ No music here!");
			noMusicMeta.setLore(Arrays.asList("§7Add music by choosing musics", "§7from the server list with", "§7the appropriated item below!"));
			noMusicItem.setItemMeta(noMusicMeta);

			this.setItem(13, noMusicItem);
		}

		// Bottom bar
		int beginBottomBar = 27;

		if (this.getParentMenu() != null) {
			this.setItem(beginBottomBar++, BACK_ITEM);
		}

		for (int i = beginBottomBar; i < 36; i++) {
			this.setItem(i, SEPARATOR);
		}

		// Page items
		if (this.musicList.size() > MUSICS_PER_PAGE) {
			// TODO
		}
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if (event.getSlot() < MUSICS_PER_PAGE) {
			int musicId = (this.currentPage - 1) * MUSICS_PER_PAGE + event.getSlot();

			if (musicId < this.musicList.size()) {
				Music music = this.musicList.get(musicId);
				this.onDiscClick(event, music);
			}
		}

		event.setCancelled(true);
	}

	public abstract void onDiscClick(InventoryClickEvent event, Music music);

	protected boolean isDiscItemStack(ItemStack itemStack) {
		return itemStack.getType().name().startsWith("RECORD_");
	}

}
