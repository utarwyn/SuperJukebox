package fr.utarwyn.superjukebox.commands.main;

import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.commands.AbstractCommand;
import fr.utarwyn.superjukebox.jukebox.JukeboxesManager;
import fr.utarwyn.superjukebox.util.PluginMsg;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * With this command, this is very easy to convert a jukebox into a SuperJukebox!
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class CreateCommand extends AbstractCommand {

    private final JukeboxesManager manager;

    public CreateCommand() {
        super("create", "c");

        this.manager = SuperJukebox.getInstance().getInstance(JukeboxesManager.class);
        this.setPermission("create");
    }

    @Override
    public void performPlayer(Player player) {
        Block block = player.getTargetBlock(null, 6);

        if (block.getType() != Material.JUKEBOX) {
            PluginMsg.errorMessage(player, "Aimed block is not a valid jukebox.");
            return;
        }

        if (this.manager.getJukeboxAt(block) != null) {
            PluginMsg.errorMessage(player, "Aimed block is already a super jukebox!");
            return;
        }

        this.manager.createSuperJukebox(block);
        PluginMsg.successMessage(player, "This jukebox now looks super cool!");
    }

}
