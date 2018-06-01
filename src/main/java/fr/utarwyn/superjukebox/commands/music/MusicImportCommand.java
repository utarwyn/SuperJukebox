package fr.utarwyn.superjukebox.commands.music;

import fr.utarwyn.superjukebox.Config;
import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.commands.parameter.Parameter;
import fr.utarwyn.superjukebox.music.MusicManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MusicImportCommand extends AbstractCommand {

	private MusicManager manager;

	MusicImportCommand() {
		super("import", "i");

		this.manager = SuperJukebox.getInstance().getInstance(MusicManager.class);

		this.setParameters(Parameter.STRING);
	}

	@Override
	public void perform(CommandSender sender) {

	}

	@Override
	public void performPlayer(Player player) {
		String url = this.readArg();

		Bukkit.getScheduler().runTaskAsynchronously(SuperJukebox.getInstance(), () -> {
			if (this.manager.importMusic(url)) {
				player.sendMessage(Config.PREFIX + "§aMusic imported and ready to be played!");
			} else {
				player.sendMessage(Config.PREFIX + "§cWe cannot import your music. Check your url.");
			}
		});
	}

	@Override
	public void performConsole(CommandSender sender) {

	}

}
