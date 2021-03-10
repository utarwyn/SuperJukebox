package fr.utarwyn.superjukebox.commands.music;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.commands.Parameter;
import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.MusicManager;
import fr.utarwyn.superjukebox.util.JUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCommand extends AbstractCommand {

    private final MusicManager manager;

    public RemoveCommand() {
        super("remove", "rm");

        this.manager = SuperJukebox.getInstance().getInstance(MusicManager.class);

        this.setPermission("music.remove");
        this.addParameter(Parameter.integer());
    }

    @Override
    public void perform(CommandSender sender) {
        int id = this.readArg();

        Bukkit.getScheduler().runTaskAsynchronously(SuperJukebox.getInstance(), () -> {
            Music music = this.manager.getMusic(id);

            if (music == null) {
                JUtil.sendMessage(sender, ChatColor.RED + "This music does not exist!");
                return;
            }

            if (this.manager.removeMusic(music)) {
                JUtil.sendMessage(sender, ChatColor.GREEN + "Music removed!");
            } else {
                JUtil.sendMessage(sender, ChatColor.RED + "Music cannot be removed from the configuration.");
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
