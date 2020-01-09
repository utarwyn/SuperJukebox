package fr.utarwyn.superjukebox.menu.jukebox;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.jukebox.Jukebox;
import fr.utarwyn.superjukebox.jukebox.JukeboxesManager;
import fr.utarwyn.superjukebox.menu.AbstractMenu;
import fr.utarwyn.superjukebox.menu.MusicDiscsMenu;
import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.MusicManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * The music add menu of a SuperJukebox!
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class JukeboxAddMusicMenu extends MusicDiscsMenu {

    private MusicManager musicManager;

    private JukeboxesManager jukeboxesManager;

    private Jukebox jukebox;

    JukeboxAddMusicMenu(Jukebox jukebox, Player player, AbstractMenu parentMenu) {
        super("SuperJukebox musics menu", player);

        this.musicManager = SuperJukebox.getInstance().getInstance(MusicManager.class);
        this.jukeboxesManager = SuperJukebox.getInstance().getInstance(JukeboxesManager.class);

        this.jukebox = jukebox;
        this.setParentMenu(parentMenu);
    }

    @Override
    public void onDiscClick(InventoryClickEvent event, Music music) {
        // Add the selected music to the jukebox ...
        this.jukebox.addCustomMusic(music);
        this.jukeboxesManager.saveJukeboxMusicsOnDisk(this.jukebox);

        // ... and go back to the main menu ;-)
        this.goToParentMenu(this.player);
    }

    @Override
    public List<Music> getMusicList() {
        List<Music> musicList = new ArrayList<>(this.musicManager.getMusics());
        musicList.removeAll(this.jukebox.getMusics());

        return musicList;
    }

    @Override
    public void onClose(Player player) {
        // Not implemented
    }

}
