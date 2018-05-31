package fr.utarwyn.superjukebox.commands;

import fr.utarwyn.superjukebox.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Waooowww, the help command! You need help?
 * @author Utarwyn
 * @since 1.0.0
 */
public class HelpCommand extends AbstractCommand {

	HelpCommand() {
		super("help", "?");
	}

	@Override
	public void perform(CommandSender sender) {
		sender.sendMessage(Config.PREFIX + "§cNo help for the moment.");
	}

	@Override
	public void performPlayer(Player player) {

	}

	@Override
	public void performConsole(CommandSender sender) {

	}

}
