package fr.utarwyn.superjukebox.jukebox;

import fr.utarwyn.superjukebox.Config;
import fr.utarwyn.superjukebox.menu.jukebox.JukeboxMainMenu;
import fr.utarwyn.superjukebox.util.JUtil;
import fr.utarwyn.superjukebox.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Contains all events for interactions with super-jukeboxes!
 *
 * @author Utarwyn
 * @since 1.0.0
 */
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

		// Optimization trick to disable double interaction with the block
		//if (!event.getHand().equals(EquipmentSlot.HAND) || !event.getPlayer().getItemInHand().equals(EquipmentSlot.HAND)) return;


		// Is there a super jukebox here?
		Jukebox jukebox = this.manager.getJukeboxAt(event.getClickedBlock());
		if (jukebox == null) return; // No :'(

		// Open the menu if the player has the right permission
		Player player = event.getPlayer();
		if (player.isSneaking()) return;

		if (jukebox.getSettings().getInteractPerm().has(player)) {
			event.setCancelled(true);
			JUtil.runSync(() -> new JukeboxMainMenu(jukebox, player).open(player));

			// Play opening sound
			JUtil.playSound(player, "NOTE_PLING", "BLOCK_NOTE_PLING");
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();

		// Create a new super jukebox if needed!
		if (block.getType() == Config.MAT_JUKEBOX && Config.allJukeboxAreSuper) {
			JUtil.runSync(() -> this.manager.createSuperJukebox(block));
            event.getPlayer().sendMessage(Messages.PREFIX + ChatColor.translateAlternateColorCodes('&', "&7You just placed a &6SuperJukebox&7, right-click it to choose a song!"));

        } else if (block.getType() == Config.MAT_JUKEBOX && event.getPlayer().isSneaking() && event.getPlayer().hasPermission("SuperJukebox.place")) {
            JUtil.runSync(() -> this.manager.createSuperJukebox(block));
			event.getPlayer().sendMessage(Messages.PREFIX + ChatColor.translateAlternateColorCodes('&', "&7You just placed a &6SuperJukebox&7, right-click it to choose a song!"));
		}
	}



	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getType() != Config.MAT_JUKEBOX) return;

		// Is there a super jukebox here?
		Jukebox jukebox = this.manager.getJukeboxAt(block);
		if (jukebox == null) return; // No :'(

		// Remove it!
		JUtil.runSync(() -> this.manager.removeSuperJukebox(block));
        event.getPlayer().sendMessage(Messages.PREFIX + ChatColor.translateAlternateColorCodes('&', "&7You just removed a &6SuperJukebox&7!"));

    }

}
