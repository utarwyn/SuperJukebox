package fr.utarwyn.superjukebox.commands.music;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.commands.Parameter;
import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.MusicManager;
import fr.utarwyn.superjukebox.util.PluginMsg;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCommand extends AbstractCommand {

    private static final int MUSICS_PER_PAGE = 8;

    private final MusicManager manager;

    public ListCommand() {
        super("list", "l");

        this.manager = SuperJukebox.getInstance().getInstance(MusicManager.class);

        this.setPermission("music.list");
        this.addParameter(Parameter.integer().optional());
    }

    @Override
    public void perform(CommandSender sender) {
        List<Music> musics = this.manager.getMusics();

        if (musics.isEmpty()) {
            PluginMsg.infoMessage(sender, "There is no music yet.");
            return;
        }

        int page = this.readArgOrDefault(1);
        if (page <= 0 || page > Math.ceil(musics.size() / (double) MUSICS_PER_PAGE)) {
            PluginMsg.errorMessage(sender, "This page does not exist!");
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

}
