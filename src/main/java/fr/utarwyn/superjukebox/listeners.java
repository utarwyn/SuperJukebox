package fr.utarwyn.superjukebox;

import fr.utarwyn.superjukebox.util.UpdateChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class listeners implements Listener {


    @EventHandler
    public void PlayerUpdateCheck(PlayerJoinEvent e) throws Exception {
        if(e.getPlayer().isOp()){

            UpdateChecker update = new UpdateChecker(SuperJukebox.getInstance());

            if (!update.checkForUpdates()){
                String latestversion = update.getLatestVersion();

                e.getPlayer().sendMessage(Config.PREFIX + "It looks like you don't have the latest version of the plugin. §8(§a"+ latestversion +"§8)");
                e.getPlayer().sendMessage(Config.PREFIX + "Please download it at: §6" + update.getResourceURL());
           }
        }
    }

}
