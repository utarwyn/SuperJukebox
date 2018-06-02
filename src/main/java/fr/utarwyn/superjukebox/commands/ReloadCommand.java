package fr.utarwyn.superjukebox.commands;

import fr.utarwyn.superjukebox.Config;
import fr.utarwyn.superjukebox.Managers;
import fr.utarwyn.superjukebox.SuperJukebox;
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

	ReloadCommand() {
		super("reload", "rl");
	}

	@Override
	public void perform(CommandSender sender) {
		if (!JUtil.senderHasPerm(sender, "reload")) {
			sender.sendMessage(Config.PREFIX + ChatColor.RED + "You don't have permission to do that!");
			return;
		}

		if (!Config.get().reload()) {
			sender.sendMessage(Config.PREFIX + "§cError when reloading config! See the console for more info!");
			sender.sendMessage(Config.PREFIX + "§8Plugin now disabled.");

			Bukkit.getPluginManager().disablePlugin(SuperJukebox.getInstance());
			return;
		}

		Managers.reloadAll();

		sender.sendMessage(Config.PREFIX + "§aConfiguration reloaded!");
	}

	@Override
	public void performPlayer(Player player) {

	}

	@Override
	public void performConsole(CommandSender sender) {

	}

}
