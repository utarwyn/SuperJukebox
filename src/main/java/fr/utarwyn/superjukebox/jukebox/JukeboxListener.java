package fr.utarwyn.superjukebox.jukebox;

import fr.utarwyn.superjukebox.configuration.Files;
import fr.utarwyn.superjukebox.menu.jukebox.JukeboxMainMenu;
import fr.utarwyn.superjukebox.util.JUtil;
import fr.utarwyn.superjukebox.util.PluginMsg;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

/**
 * Contains all events for interactions with super-jukeboxes!
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class JukeboxListener implements Listener {

    private final JukeboxesManager manager;

    JukeboxListener(JukeboxesManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        // Initial verifications
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.JUKEBOX) return;

        // Is there a super jukebox here?
        Jukebox jukebox = this.manager.getJukeboxAt(event.getClickedBlock());
        if (jukebox == null) return;

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
        Player player = event.getPlayer();
        Block block = event.getBlock();

        // Create a new super jukebox if needed!
        if (block.getType() == Material.JUKEBOX) {
            boolean interactSuperJukebox = Files.getConfiguration().isAllJukeboxAreSuper()
                    || player.isSneaking() && JUtil.playerHasPerm(player, "place");

            if (interactSuperJukebox) {
                JUtil.runSync(() -> this.manager.createSuperJukebox(block));
                PluginMsg.infoMessage(event.getPlayer(), "You just placed a §6SuperJukebox§7, right-click it to choose a song!");
            }
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        this.manager.loadJukeboxesOfWorld(event.getWorld());
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        this.manager.unloadJukeboxesOfWorld(event.getWorld());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() != Material.JUKEBOX) return;

        // Is there a super jukebox here?
        Jukebox jukebox = this.manager.getJukeboxAt(block);
        if (jukebox == null) return;

        // Remove it!
        JUtil.runSync(() -> this.manager.removeSuperJukebox(block));
        PluginMsg.infoMessage(event.getPlayer(), "You just removed a §6SuperJukebox§7!");
    }

}
