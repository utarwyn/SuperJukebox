package fr.utarwyn.superjukebox.commands.music;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.commands.Parameter;
import fr.utarwyn.superjukebox.music.MusicManager;
import fr.utarwyn.superjukebox.util.PluginMsg;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

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
                    PluginMsg.successMessage(sender, "Music imported and ready to be played!");
                    break;
                case MALFORMATED_URL:
                    PluginMsg.errorMessage(sender, "Your importation url seems to be invalid, please retry.");
                    break;
                case UNKNOWN_FILE:
                    PluginMsg.errorMessage(sender, "The file you tried to load cannot be found.");
                    break;
                case DECODING_ERROR:
                    PluginMsg.errorMessage(sender, "An nbs decoding error was caught during the importation. Check your console to have more info.");
                    break;
                case ALREADY_IMPORTED:
                    PluginMsg.errorMessage(sender, "This music has been already imported.");
                    break;
            }
        });
    }

}
