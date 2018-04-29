package fr.utarwyn.superjukebox.commands;

import fr.utarwyn.superjukebox.Config;
import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.jukebox.JukeboxesManager;
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
		}

		return true;
	}

}
