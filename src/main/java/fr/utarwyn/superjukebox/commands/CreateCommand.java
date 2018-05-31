package fr.utarwyn.superjukebox.commands;

import fr.utarwyn.superjukebox.Config;
import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.jukebox.JukeboxesManager;
import fr.utarwyn.superjukebox.util.JUtil;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * With this command, this is very easy to convert a jukebox into a SuperJukebox!
 * @author Utarwyn
 * @since 1.0.0
 */
public class CreateCommand extends AbstractCommand {

	private JukeboxesManager manager;

	CreateCommand() {
		super("create");

		this.manager = SuperJukebox.getInstance().getInstance(JukeboxesManager.class);
	}

	@Override
	public void perform(CommandSender sender) {

	}

	@Override
	public void performPlayer(Player player) {
		if (!JUtil.playerHasPerm(player, "create")) {
			player.sendMessage(Config.PREFIX + ChatColor.RED + "You don't have permission to do that!");
			return;
		}

		Block block = player.getTargetBlock(null, 6);

		if (block == null || block.getType() != Config.MAT_JUKEBOX) {
			player.sendMessage(Config.PREFIX + ChatColor.RED + "The aimed block is not a valid jukebox.");
			return;
		}

		if (this.manager.getJukeboxAt(block) != null) {
			player.sendMessage(Config.PREFIX + ChatColor.RED + "The aimed block is already a super jukebox!");
			return;
		}

		// Create a superjukebox here!
		this.manager.createSuperJukebox(block);
		player.sendMessage(Config.PREFIX + ChatColor.GREEN + "This jukebox is now " + ChatColor.GOLD + "A SuperJukebox " + ChatColor.GREEN + "!");
	}

	@Override
	public void performConsole(CommandSender sender) {

	}
}
