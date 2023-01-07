package fr.utarwyn.superjukebox.commands;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.commands.main.CreateCommand;
import fr.utarwyn.superjukebox.commands.main.HelpCommand;
import fr.utarwyn.superjukebox.commands.main.MusicCommand;
import fr.utarwyn.superjukebox.commands.main.ReloadCommand;
import fr.utarwyn.superjukebox.util.PluginMsg;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.List;

/**
 * Main command of the plugin.
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class MainCommand extends AbstractCommand {

    private final String pluginVersion;

    private final String pluginAuthor;

    public MainCommand() {
        super("superjukebox", "sj");

        this.addSubCommand(new CreateCommand());
        this.addSubCommand(new MusicCommand());
        this.addSubCommand(new HelpCommand());
        this.addSubCommand(new ReloadCommand());

        // Save some plugin values in memory
        PluginDescriptionFile description = SuperJukebox.getInstance().getDescription();
        List<String> authors = description.getAuthors();

        this.pluginVersion = description.getVersion();
        this.pluginAuthor = authors.get(0);
    }

    @Override
    public void perform(CommandSender sender) {
        PluginMsg.pluginBar(sender);

        sender.sendMessage(" ");
        sender.sendMessage("  §7 Plugin created by §a" + this.pluginAuthor + "§7 and contributors.");
        sender.sendMessage("  §7 Version installed: §e" + this.pluginVersion);
        sender.sendMessage("  §7 Do you need help? Type §d/sj help§7!");
        sender.sendMessage(" ");
    }

}
