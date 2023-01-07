package fr.utarwyn.superjukebox.util;

import fr.utarwyn.superjukebox.AbstractManager;
import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.configuration.Files;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;

public class Updater extends AbstractManager implements Runnable {

    private static final int PROJECT_ID = 62293;

    private static final String DOWNLOAD_LINK = "https://github.com/utarwyn/SuperJukebox/releases";

    private static final String VERSION_API_URL = "https://api.spigotmc.org/legacy/update.php?resource=" + PROJECT_ID;

    private SemanticVersion currentVersion;

    private SemanticVersion latestVersion;

    public Updater() {
        super(SuperJukebox.getInstance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        // Check for updates if enabled by the server administrator
        if (Files.getConfiguration().isUpdateChecker()) {
            this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, this);
        } else {
            this.plugin.getLogger().warning("You have disabled update checking. Please be sure that the plugin is up to date.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void unload() {
        this.latestVersion = null;
        this.currentVersion = null;
    }

    /**
     * Detects the latest version of the plugin and stores it.
     * Also notifies the console about the check.
     */
    @Override
    public void run() {
        try {
            this.retreiveVersions();
            this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this::notifyConsole);
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE,
                    "Cannot retrieve the latest version of the plugin", e);
        }
    }

    /**
     * Method called when a player joins the server
     *
     * @param event The join event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Send update message to the player if it has the permission
        if (player.hasPermission("superjukebox.update") && this.hasToBeUpdated()) {
            this.notifyPlayer(player);
        }
    }

    /**
     * Notify the console if the plugin has to be updated or not.
     */
    public void notifyConsole() {
        if (this.hasToBeUpdated()) {
            logger.warning("-----------[Plugin Update]----------");
            logger.log(Level.WARNING, "  Your server is using v{0} of SuperJukebox. Latest is v{1}.",
                    new Object[]{this.currentVersion, this.latestVersion});
            logger.log(Level.WARNING, "  Download the new version here: {0}", DOWNLOAD_LINK);
            logger.warning("------------------------------------");
        } else {
            logger.log(Level.INFO, "You are using the newest version of the plugin ({0}).", this.currentVersion);
        }

        if (this.currentVersion.isDevelopment()) {
            logger.warning("**** IMPORTANT!");
            logger.warning("You are using a development build. This means that the plugin can be unstable.");
            logger.warning("If you have an issue during its execution, please report it on the Github repository.");
        } else if (this.currentVersion.getMajor() == 0) {
            logger.warning("**** IMPORTANT!");
            logger.warning("You are using an alpha build. This means that the plugin is still in its first stages.");
            logger.warning("If you have an issue during its execution, please report it on the Github repository.");
        }
    }

    /**
     * Notify player if the plugin has to be updated or not.
     *
     * @param player player that will be notified
     */
    public void notifyPlayer(Player player) {
        if (this.hasToBeUpdated()) {
            PluginMsg.errorMessage(player, "§6SuperJukebox §cis not up-to-date! Latest version is §ev" + latestVersion + "§c.");
            JUtil.playSound(player, "NOTE_PLING", "BLOCK_NOTE_PLING");
        }
    }

    /**
     * Start the checking of the plugin's version.
     *
     * @throws IOException throwed if the method cannot read the remote json
     */
    private void retreiveVersions() throws IOException {
        try (InputStream inputStream = new URL(VERSION_API_URL).openStream();
             InputStreamReader streamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(streamReader)) {
            this.currentVersion = new SemanticVersion(this.plugin.getDescription().getVersion());
            this.latestVersion = new SemanticVersion(reader.readLine());
        }
    }

    /**
     * Compares current and latest versions to check if the plugin has to be updated.
     *
     * @return true if the plugin needs to be updated
     */
    private boolean hasToBeUpdated() {
        return this.currentVersion != null && this.latestVersion != null
                && this.currentVersion.compareTo(this.latestVersion) < 0;
    }

}
