package fr.utarwyn.superjukebox.commands.music;

import fr.utarwyn.superjukebox.Config;
import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.commands.parameter.Parameter;
import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.MusicManager;
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
				sender.sendMessage(Config.PREFIX + ChatColor.RED + "This music does not exist!");
				return;
			}

			if (this.manager.removeMusic(music)) {
				sender.sendMessage(Config.PREFIX + ChatColor.GREEN + "Music removed!");
			} else {
				sender.sendMessage(Config.PREFIX + ChatColor.RED + "Music cannot be removed from the configuration.");
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
