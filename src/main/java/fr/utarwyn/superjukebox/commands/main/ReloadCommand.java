package fr.utarwyn.superjukebox.commands.main;

import fr.utarwyn.superjukebox.Managers;
import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.configuration.Files;
import fr.utarwyn.superjukebox.util.PluginMsg;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * This command just add a possibility to reload the plugin
 * directly in game, that's not incredible.
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class ReloadCommand extends AbstractCommand {

    public ReloadCommand() {
        super("reload", "rl");
        this.setPermission("reload");
    }

    @Override
    public void perform(CommandSender sender) {
        if (!Files.getConfiguration().reload()) {
            PluginMsg.errorMessage(sender, "Error when reloading config! See the console for more info!");
            Bukkit.getPluginManager().disablePlugin(SuperJukebox.getInstance());
            PluginMsg.infoMessage(sender, "Plugin has been disabled.");
            return;
        }

        Managers.reloadAll();
        PluginMsg.successMessage(sender, "Configuration reloaded!");
    }

}
