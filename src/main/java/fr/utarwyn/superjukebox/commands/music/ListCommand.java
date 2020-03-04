package fr.utarwyn.superjukebox.commands.music;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.commands.Parameter;
import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.MusicManager;
import fr.utarwyn.superjukebox.util.JUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ListCommand extends AbstractCommand {

    private static final int MUSICS_PER_PAGE = 8;

    private MusicManager manager;

    public ListCommand() {
        super("list", "l");

        this.manager = SuperJukebox.getInstance().getInstance(MusicManager.class);

        this.addParameter(Parameter.integer().optional());
    }

    @Override
    public void perform(CommandSender sender) {
        List<Music> musics = this.manager.getMusics();

        if (musics.isEmpty()) {
            JUtil.sendMessage(sender, ChatColor.RED + "There is no music yet.");
            return;
        }

        int page = this.readArgOrDefault(1);
        if (page <= 0 || page > Math.ceil(musics.size() / (double) MUSICS_PER_PAGE)) {
            JUtil.sendMessage(sender, ChatColor.RED + "This page does not exist!");
            return;
        }


        sender.sendMessage("§a§lMusic list §2§l(§a§lPage " + page + "§2§l)§a§l:");

        int begin = MUSICS_PER_PAGE * (page - 1);
        for (int i = begin; i < begin + MUSICS_PER_PAGE && i < musics.size(); i++) {
            Music music = musics.get(i);
            sender.sendMessage(
                    "§7#" + String.format("%02d", i + 1) + ": §f" + music.getName() + "§b (" + music.getOriginalAuthor() + "§b)"
            );
        }
    }

    @Override
    public void performPlayer(Player player) {
        // Not implemented
    }

    @Override
    public void performConsole(CommandSender sender) {
        // Not implemented
    }

}
