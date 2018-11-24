package fr.utarwyn.superjukebox.bukkit.commands;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Waooowww, the help command! You need help?
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class HelpCommand extends AbstractCommand {

	HelpCommand() {
		super("help", "?");
	}

	@Override
	public void perform(CommandSender sender) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7&m--------------------&r&8[&3SuperJukebox&8]&7&m--------------------\n\n" +
				"&b/&2sj &6help&7/&6? &b- &aShow this page!\n" +
                "&b/&2sj &6music&7/&6m &b- &aGet help with importing music!\n" +
                "&b/&2sj &6create &b- &aCreate a &6SuperJukebox\n" +
                "&b/&2sj &6support &b- &aGet the link to the discord server!\n" +
                "&b/&2sj &6reload&7 &b- &aReload the config!\n" +
                "&7&m----------------------------------------------------"));
	}

	@Override
	public void performPlayer(Player player) {

	}

	@Override
	public void performConsole(CommandSender sender) {

	}

}
