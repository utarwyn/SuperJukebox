package fr.utarwyn.superjukebox.commands.music;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.commands.Parameter;
import fr.utarwyn.superjukebox.music.MusicManager;
import fr.utarwyn.superjukebox.util.JUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ImportCommand extends AbstractCommand {

    private final MusicManager manager;

    public ImportCommand() {
        super("import", "i");

        this.manager = SuperJukebox.getInstance().getInstance(MusicManager.class);

        this.setPermission("music.import");
        this.addParameter(Parameter.string());
    }

    @Override
    public void perform(CommandSender sender) {
        String endpoint = this.readArg();

        Bukkit.getScheduler().runTaskAsynchronously(SuperJukebox.getInstance(), () -> {
            switch (this.manager.importMusic(endpoint)) {
                case GOOD:
                    JUtil.sendMessage(sender, ChatColor.GREEN + "Music imported and ready to be played!");
                    break;
                case MALFORMATED_URL:
                    JUtil.sendMessage(sender, ChatColor.RED + "Your importation url seems to be invalid, please retry.");
                    break;
                case UNKNOWN_FILE:
                    JUtil.sendMessage(sender, ChatColor.RED + "The file you tried to load cannot be found.");
                    break;
                case DECODING_ERROR:
                    JUtil.sendMessage(sender, ChatColor.RED + "An nbs decoding error was caught during the importation. Check your console to have more info.");
                    break;
                case ALREADY_IMPORTED:
                    JUtil.sendMessage(sender, ChatColor.RED + "This music has been already imported.");
                    break;
            }
        });
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
