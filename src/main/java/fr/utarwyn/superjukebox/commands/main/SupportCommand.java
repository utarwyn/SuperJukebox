package fr.utarwyn.superjukebox.commands.main;

import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.util.JUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SupportCommand extends AbstractCommand {

    public SupportCommand() {
        super("support", "discord");
    }

    @Override
    public void perform(CommandSender sender) {
        JUtil.sendMessage(sender, "If you ever need help with the plugin," +
                " or you want to report a bug. Join the discord here: "
                + ChatColor.AQUA + "https://discord.gg/ZrZvV2s");
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
