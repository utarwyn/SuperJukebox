package fr.utarwyn.superjukebox.commands.main;

import fr.utarwyn.superjukebox.Managers;
import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.configuration.Files;
import fr.utarwyn.superjukebox.util.JUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This command just add a possibility to reload the plugin
 * directly in game, that's not incredible.
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class ReloadCommand extends AbstractCommand {

    public ReloadCommand() {
        super("reload", "rl");
    }

    @Override
    public void perform(CommandSender sender) {
        if (!JUtil.senderHasPerm(sender, "reload")) {
            JUtil.sendMessage(sender, ChatColor.RED + "You don't have permission to do that!");
            return;
        }

        if (!Files.getConfiguration().reload()) {
            JUtil.sendMessage(sender, ChatColor.RED + "Error when reloading config! See the console for more info!");
            JUtil.sendMessage(sender, ChatColor.DARK_GRAY + "Plugin now disabled.");

            Bukkit.getPluginManager().disablePlugin(SuperJukebox.getInstance());
            return;
        }

        Managers.reloadAll();

        JUtil.sendMessage(sender, ChatColor.GREEN + "Configuration reloaded!");
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
