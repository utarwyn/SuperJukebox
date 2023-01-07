package fr.utarwyn.superjukebox.util;

import com.google.common.base.Strings;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Utility class used which sends various messages.
 *
 * @author Utarwyn
 * @since 0.4.1
 */
public class PluginMsg {

    private static final String PREFIX_ERROR = "✖ ";
    private static final String PREFIX_SUCCESS = "✔ ";
    private static final String PREFIX_INFO = "→ ";

    private PluginMsg() {
        // Not implemented
    }

    /**
     * Send a simple error message to a specific entity.
     *
     * @param receiver receiver of the message
     * @param message  message to send with in an error state
     */
    public static void errorMessage(CommandSender receiver, String message) {
        receiver.sendMessage(ChatColor.RED + PREFIX_ERROR + message);
    }

    /**
     * Send a simple success message to a specific entity.
     *
     * @param receiver receiver of the message
     * @param message  message to send with in a success state
     */
    public static void successMessage(CommandSender receiver, String message) {
        receiver.sendMessage(ChatColor.GREEN + PREFIX_SUCCESS + message);
    }

    /**
     * Send a simple information message to a specific entity.
     *
     * @param receiver receiver of the message
     * @param message  message to send with in an info state
     */
    public static void infoMessage(CommandSender receiver, String message) {
        receiver.sendMessage(ChatColor.GRAY + PREFIX_INFO + message);
    }

    /**
     * Send the plugin header bar to a specific entity.
     *
     * @param receiver receiver of the message
     */
    public static void pluginBar(CommandSender receiver) {
        String pBar = "§5§m" + Strings.repeat("-", 5);
        String sBar = "§d§m" + Strings.repeat("-", 11);

        receiver.sendMessage("§8++" + pBar + sBar + "§r§d( §3SuperJukebox §d)" + sBar + pBar + "§8++");
    }

}
