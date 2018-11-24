package fr.utarwyn.superjukebox.bukkit.commands;

import fr.utarwyn.superjukebox.bukkit.Config;
import fr.utarwyn.superjukebox.bukkit.Managers;
import fr.utarwyn.superjukebox.bukkit.SuperJukebox;
import fr.utarwyn.superjukebox.bukkit.util.JUtil;
import fr.utarwyn.superjukebox.bukkit.util.Messages;
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

	ReloadCommand() {
		super("reload", "rl");
	}

	@Override
	public void perform(CommandSender sender) {
		if (!JUtil.senderHasPerm(sender, "reload")) {
			sender.sendMessage(Messages.PREFIX + ChatColor.RED + "You don't have permission to do that!");
			return;
		}

		if (!Config.get().reload()) {
			sender.sendMessage(Messages.PREFIX + "§cError when reloading config! See the console for more info!");
			sender.sendMessage(Messages.PREFIX + "§8Plugin now disabled.");

			Bukkit.getPluginManager().disablePlugin(SuperJukebox.getInstance());
			return;
		}

		Managers.reloadAll();

		sender.sendMessage(Messages.PREFIX + "§aConfiguration reloaded!");
	}

	@Override
	public void performPlayer(Player player) {

	}

	@Override
	public void performConsole(CommandSender sender) {

	}

}
