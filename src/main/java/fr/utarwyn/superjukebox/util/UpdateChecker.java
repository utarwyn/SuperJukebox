package fr.utarwyn.superjukebox.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.plugin.java.JavaPlugin;


public class UpdateChecker {


    private int project = 62293;
    private URL checkURL;
    private String currentVersion = "1.0.1";
    private JavaPlugin plugin;


    public UpdateChecker(JavaPlugin plugin) {
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion();

        try {
            this.checkURL = new URL(
                    "https://api.spigotmc.org/legacy/update.php?resource=" + project);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public int getProjectID() {
        return project;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public String getLatestVersion() {
        return currentVersion;
    }

    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + project;
    }

    public boolean checkForUpdates() throws Exception {
        URLConnection con = checkURL.openConnection();
        this.currentVersion = new BufferedReader(new InputStreamReader(con.getInputStream()))
                .readLine();
        return !plugin.getDescription().getVersion().equals(currentVersion);
    }
}