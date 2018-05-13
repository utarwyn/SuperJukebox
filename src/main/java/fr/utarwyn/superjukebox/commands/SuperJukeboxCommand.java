package fr.utarwyn.superjukebox.commands;

import fr.utarwyn.superjukebox.Config;
import fr.utarwyn.superjukebox.Managers;
import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.jukebox.JukeboxesManager;
import fr.utarwyn.superjukebox.util.JUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SuperJukeboxCommand implements CommandExecutor {

	private JukeboxesManager manager;

	public SuperJukeboxCommand() {
		this.manager = SuperJukebox.getInstance().getInstance(JukeboxesManager.class);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(Config.PREFIX + "Created by §3Utarwyn§7. Version §e" + SuperJukebox.getInstance().getDescription().getVersion() + "§7.");
			sender.sendMessage(Config.PREFIX + "To show plugin's help: §d/sj help§7.");
			return true;
		}

		String arg = args[0];

		switch (arg) {
			case "help":
			case "h":
			case "?":
				// TODO: write the plugin's help page
				sender.sendMessage(Config.PREFIX + ChatColor.RED + "No help for the moment.");
				break;

			case "reload":
			case "rl":
				if (!JUtil.senderHasPerm(sender, "reload")) {
					sender.sendMessage(Config.PREFIX + "You don't have permission to do that!");
					return true;
				}

				if (!Config.get().reload()) {
					sender.sendMessage(Config.PREFIX + "§cError when reloading config! See the console for more info!");
					sender.sendMessage(Config.PREFIX + "§8Plugin now disabled.");

					Bukkit.getPluginManager().disablePlugin(SuperJukebox.getInstance());
					return true;
				}

				Managers.reloadAll();

				sender.sendMessage(Config.PREFIX + "§aConfiguration reloaded!");
				break;
		}

		return true;
	}

}
