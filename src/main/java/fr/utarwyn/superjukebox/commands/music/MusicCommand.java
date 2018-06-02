package fr.utarwyn.superjukebox.commands.music;

import fr.utarwyn.superjukebox.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MusicCommand extends AbstractCommand {

	public MusicCommand() {
		super("music");

		this.addSubCommand(new MusicImportCommand());
		this.addSubCommand(new MusicListCommand());
	}

	@Override
	public void perform(CommandSender sender) {

	}

	@Override
	public void performPlayer(Player player) {

	}

	@Override
	public void performConsole(CommandSender sender) {

	}

}
