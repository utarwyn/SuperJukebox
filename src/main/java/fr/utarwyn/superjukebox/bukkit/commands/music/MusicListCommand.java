package fr.utarwyn.superjukebox.bukkit.commands.music;

import fr.utarwyn.superjukebox.bukkit.SuperJukebox;
import fr.utarwyn.superjukebox.bukkit.commands.AbstractCommand;
import fr.utarwyn.superjukebox.bukkit.commands.parameter.Parameter;
import fr.utarwyn.superjukebox.bukkit.music.Music;
import fr.utarwyn.superjukebox.bukkit.music.MusicManager;
import fr.utarwyn.superjukebox.bukkit.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MusicListCommand extends AbstractCommand {

	private static final int MUSICS_PER_PAGE = 2;

	private MusicManager manager;

	MusicListCommand() {
		super("list", "l");

		this.manager = SuperJukebox.getInstance().getInstance(MusicManager.class);

		this.addParameter(Parameter.INT.optional());
	}

	@Override
	public void perform(CommandSender sender) {
		List<Music> musics = this.manager.getMusics();

		if (musics.isEmpty()) {
			sender.sendMessage(Messages.PREFIX + ChatColor.RED + "There is no music yet.");
			return;
		}

		int page = this.readArgOrDefault(1);
		if (page <= 0 || page > Math.ceil(musics.size() / (double) MUSICS_PER_PAGE)) {
			sender.sendMessage(Messages.PREFIX + ChatColor.RED + "This page does not exist!");
			return;
		}


        sender.sendMessage("§a§lMusic list §2§l(§a§lPage " + page + "§2§l)§a§l:");

		int begin = MUSICS_PER_PAGE * (page - 1);
		for (int i = begin; i < begin + MUSICS_PER_PAGE && i < musics.size(); i++) {
			Music music = musics.get(i);
			sender.sendMessage(
					"§7#" + String.format("%02d", i + 1) + ": §f" + music.getName() + "§b (" + music.getOriginalAuthor() + "§b)"
			);
		}
	}

	@Override
	public void performPlayer(Player player) {

	}

	@Override
	public void performConsole(CommandSender sender) {

	}

}
