package fr.utarwyn.superjukebox.commands.music;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.commands.Parameter;
import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.MusicManager;
import fr.utarwyn.superjukebox.util.PluginMsg;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

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
                PluginMsg.errorMessage(sender, "This music does not exist!");
                return;
            }

            if (this.manager.removeMusic(music)) {
                PluginMsg.successMessage(sender, "Music removed!");
            } else {
                PluginMsg.errorMessage(sender, "Music cannot be removed from the configuration.");
            }
        });
    }

}
