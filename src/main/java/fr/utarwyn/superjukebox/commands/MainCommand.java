package fr.utarwyn.superjukebox.commands;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.commands.main.*;
import fr.utarwyn.superjukebox.util.JUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

    private String pluginContributors;

    public MainCommand() {
        super("superjukebox", "sj");

        this.addSubCommand(new CreateCommand());
        this.addSubCommand(new MusicCommand());
        this.addSubCommand(new HelpCommand());
        this.addSubCommand(new ReloadCommand());
        this.addSubCommand(new SupportCommand());

        // Save some plugin values in memory
        PluginDescriptionFile description = SuperJukebox.getInstance().getDescription();
        List<String> authors = description.getAuthors();

        this.pluginVersion = description.getVersion();
        this.pluginAuthor = authors.get(0);
        if (authors.size() > 1) {
            this.pluginContributors = String.join(", ", authors.subList(1, authors.size()));
        }
    }

    @Override
    public void perform(CommandSender sender) {
        JUtil.sendMessage(sender, String.format("Created by §3%s.§7 Version §e%s§7.", this.pluginAuthor, this.pluginVersion));
        if (this.pluginContributors != null) {
            JUtil.sendMessage(sender, String.format("Contributors: §3%s§7.", this.pluginContributors));
        }

        JUtil.sendMessage(sender, "To show plugin's help: §d/sj help§7.");
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
