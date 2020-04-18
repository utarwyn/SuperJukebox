package fr.utarwyn.superjukebox.util;

import fr.utarwyn.superjukebox.AbstractManager;
import fr.utarwyn.superjukebox.SuperJukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

public class Updater extends AbstractManager {

    private static final int PROJECT_ID = 62293;

    private static final String PROJECT_URL = "https://www.spigotmc.org/resources/" + PROJECT_ID;

    private URL checkURL;

    private String currentVersion;

    public Updater() {
        super(SuperJukebox.getInstance());
    }

    @Override
    public void initialize() {
        this.currentVersion = this.plugin.getDescription().getVersion();

        try {
            this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + PROJECT_ID);
        } catch (MalformedURLException e) {
            Log.log(Level.SEVERE, "Cannot generate the url to check for updates", e);
        }
    }

    @Override
    protected void unload() {
        // Updater do nothing at the disabling of the plugin
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (JUtil.playerHasPerm(player, "update")) {
            JUtil.runSync(() -> {
                try {
                    if (this.isNotUpToDate()) {
                        JUtil.sendMessage(player, "It looks like you don't have the latest version of the plugin. §8(§a" + currentVersion + "§8)");
                        JUtil.sendMessage(player, "Please download it at: §6" + PROJECT_URL);
                    }
                } catch (IOException e) {
                    Log.log(Level.WARNING, "Cannot search for updates", e);
                }
            });
        }
    }

    private boolean isNotUpToDate() throws IOException {
        URLConnection con = checkURL.openConnection();
        this.currentVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        return !plugin.getDescription().getVersion().equals(currentVersion);
    }

}
