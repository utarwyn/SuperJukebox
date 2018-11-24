package fr.utarwyn.superjukebox.bukkit.commands.music;

import fr.utarwyn.superjukebox.bukkit.SuperJukebox;
import fr.utarwyn.superjukebox.bukkit.commands.AbstractCommand;
import fr.utarwyn.superjukebox.bukkit.commands.parameter.Parameter;
import fr.utarwyn.superjukebox.bukkit.music.Music;
import fr.utarwyn.superjukebox.bukkit.music.MusicManager;
import fr.utarwyn.superjukebox.bukkit.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MusicRemoveCommand extends AbstractCommand {

	private MusicManager manager;

	MusicRemoveCommand() {
		super("remove", "rm");

		this.manager = SuperJukebox.getInstance().getInstance(MusicManager.class);

		this.addParameter(Parameter.INT);
	}

	@Override
	public void perform(CommandSender sender) {
		int id = this.readArg();

		Bukkit.getScheduler().runTaskAsynchronously(SuperJukebox.getInstance(), () -> {
			Music music = this.manager.getMusic(id);

			if (music == null) {
				sender.sendMessage(Messages.PREFIX + ChatColor.RED + "This music does not exist!");
				return;
			}

			if (this.manager.removeMusic(music)) {
				sender.sendMessage(Messages.PREFIX + ChatColor.GREEN + "Music removed!");
			} else {
				sender.sendMessage(Messages.PREFIX + ChatColor.RED + "Music cannot be removed from the configuration.");
			}
		});
	}

	@Override
	public void performPlayer(Player player) {

	}

	@Override
	public void performConsole(CommandSender sender) {

	}

}
