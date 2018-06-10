package fr.utarwyn.superjukebox.commands.music;

import fr.utarwyn.superjukebox.Config;
import fr.utarwyn.superjukebox.commands.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MusicCommand extends AbstractCommand {

	public MusicCommand() {
		super("music");

		this.addSubCommand(new MusicImportCommand());
		this.addSubCommand(new MusicListCommand());
		this.addSubCommand(new MusicRemoveCommand());
	}

	@Override
	public void perform(CommandSender sender) {
		sender.sendMessage(Config.PREFIX + ChatColor.RED + "Usage: /sj music <import|list|remove>");
	}

	@Override
	public void performPlayer(Player player) {

	}

	@Override
	public void performConsole(CommandSender sender) {

	}

}
