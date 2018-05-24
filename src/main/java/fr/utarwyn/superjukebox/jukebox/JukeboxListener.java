package fr.utarwyn.superjukebox.jukebox;

import fr.utarwyn.superjukebox.Config;
import fr.utarwyn.superjukebox.menu.JukeboxMainMenu;
import fr.utarwyn.superjukebox.util.JUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class JukeboxListener implements Listener {

	private JukeboxesManager manager;

	JukeboxListener(JukeboxesManager manager) {
		this.manager = manager;
	}

	@EventHandler
	public void onBlockInteract(PlayerInteractEvent event) {
		// Initial verifications
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) return;
		if (event.getClickedBlock().getType() != Config.MAT_JUKEBOX) return;

		// Is there a super jukebox here?
		Jukebox jukebox = this.manager.getJukeboxAt(event.getClickedBlock());
		if (jukebox == null) return; // No :'(

		// Open the menu if the player has the right permission
		Player player = event.getPlayer();
		if (player.isSneaking()) return;

		if (JUtil.playerHasPerm(player, "interact")) {
			JUtil.runSync(() -> new JukeboxMainMenu(jukebox, player).open(player));

			// Play opening sound
			JUtil.playSound(player, "NOTE_PLING", "BLOCK_NOTE_PLING");
		}
	}

}