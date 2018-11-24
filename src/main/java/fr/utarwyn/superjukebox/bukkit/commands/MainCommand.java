package fr.utarwyn.superjukebox.bukkit.commands;

import fr.utarwyn.superjukebox.bukkit.SuperJukebox;
import fr.utarwyn.superjukebox.bukkit.commands.music.MusicCommand;
import fr.utarwyn.superjukebox.bukkit.util.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Main command of the plugin.
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class MainCommand extends AbstractCommand {

	public MainCommand() {
		super("superjukebox", "sj");

		this.addSubCommand(new CreateCommand());
		this.addSubCommand(new MusicCommand());
		this.addSubCommand(new HelpCommand());
		this.addSubCommand(new ReloadCommand());
		this.addSubCommand(new SupportCommand());
	}

	@Override
	public void perform(CommandSender sender) {
		sender.sendMessage(Messages.PREFIX + "Created by §3Utarwyn§7 edited by §3TCOOfficiall.§7 Version §e" + SuperJukebox.getInstance().getDescription().getVersion() + "§7.");
		sender.sendMessage(Messages.PREFIX + "To show plugin's help: §d/sj help§7.");
	}

	@Override
	public void performPlayer(Player player) {

	}

	@Override
	public void performConsole(CommandSender sender) {

	}

}
