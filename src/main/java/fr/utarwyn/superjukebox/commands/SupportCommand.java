package fr.utarwyn.superjukebox.commands;

import fr.utarwyn.superjukebox.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SupportCommand extends AbstractCommand {

    SupportCommand() {
        super("support", "discord");
    }

    @Override
    public void perform(CommandSender sender) {
        sender.sendMessage(Messages.PREFIX + "If you ever need help with the plugin," +
                " or you want to report a bug. Join the discord here: "
                + ChatColor.AQUA + "https://discord.gg/ZrZvV2s");
    }

    @Override
    public void performPlayer(Player player) {

    }

    @Override
    public void performConsole(CommandSender sender) {

    }
}
