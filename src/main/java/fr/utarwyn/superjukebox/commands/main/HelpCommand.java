package fr.utarwyn.superjukebox.commands.main;

import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.util.PluginMsg;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Waooowww, the help command! You need help?
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super("help", "h", "?");
    }

    /**
     * Format a command usage for a given player and command.
     *
     * @param sender     The entity which receive the line
     * @param command    Command to perform to run the action
     * @param permission Permission which player have to had to perform the see the help line
     * @return formated command
     */
    static String formatCommandFor(CommandSender sender, String command, String permission) {
        if (!sender.hasPermission("superjukebox." + permission)) {
            command = ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + ChatColor.stripColor(command);
        }

        return command;
    }

    @Override
    public void perform(CommandSender sender) {
        PluginMsg.pluginBar(sender);

        sender.sendMessage(" ");
        sender.sendMessage("  §6/sj music §bimport,list,remove");
        sender.sendMessage("  §7   ⏩ Manage the music available in jukeboxes");
        sender.sendMessage("  " + formatCommandFor(sender, "§6/sj create", "create"));
        sender.sendMessage("  §7   ⏩ Create a super jukebox");
        sender.sendMessage("  " + formatCommandFor(sender, "§3/sj reload", "reload"));
        sender.sendMessage("  §7   ⏩ Reload the plugin");
        sender.sendMessage(" ");
    }

}
