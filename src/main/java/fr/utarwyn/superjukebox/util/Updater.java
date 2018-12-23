package fr.utarwyn.superjukebox.util;

import fr.utarwyn.superjukebox.AbstractManager;
import fr.utarwyn.superjukebox.SuperJukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
			e.printStackTrace();
		}
	}

	@Override
	protected void unload() {

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) throws Exception {
		Player player = event.getPlayer();

		if (JUtil.playerHasPerm(player, "update")) {
			if (this.isNotUpToDate()) {
				player.sendMessage(Messages.PREFIX + "It looks like you don't have the latest version of the plugin. §8(§a"+ currentVersion +"§8)");
				player.sendMessage(Messages.PREFIX + "Please download it at: §6" + PROJECT_URL);
			}
		}
	}

	private boolean isNotUpToDate() throws Exception {
		URLConnection con = checkURL.openConnection();
		this.currentVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
		return !plugin.getDescription().getVersion().equals(currentVersion);
	}

}
