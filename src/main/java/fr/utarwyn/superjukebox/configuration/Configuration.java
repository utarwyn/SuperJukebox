package fr.utarwyn.superjukebox.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Configuration class. Reflets the config.yml
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class Configuration extends YamlFile {

    @Configurable
    private boolean debug;

    @Configurable(key = "chat.prefix")
    private String prefix;

    @Configurable
    private String locale;

    @Configurable(key = "alljukeboxesaresuper")
    private boolean allJukeboxAreSuper;

    @Configurable(key = "jukeboxconfiguration.settings.distance")
    private int jukeboxDefaultDistance;

    @Configurable(key = "jukeboxconfiguration.settings.volume")
    private int jukeboxDefaultVolume;

    @Configurable(key = "jukeboxconfiguration.settings.globalmusics")
    private boolean jukeboxDefaultUseGlobalMusics;

    @Configurable(key = "jukeboxconfiguration.settings.autoplay")
    private boolean jukeboxDefaultUseAutoplay;

    @Configurable(key = "jukeboxconfiguration.settings.particles")
    private boolean jukeboxDefaultParticles;

    @Configurable(key = "jukeboxconfiguration.settings.playwithredstone")
    private boolean jukeboxDefaultPlayWithRedstone;

    @Configurable(key = "jukeboxconfiguration.settings.announcements")
    private boolean jukeboxDefaultAnnouncements;

    @Configurable(key = "jukeboxconfiguration.permissions.interact")
    private String jukeboxDefaultInteractPerm;

    @Configurable(key = "jukeboxconfiguration.permissions.editmusics")
    private String jukeboxDefaultEditMusicsPerm;

    @Configurable(key = "jukeboxconfiguration.permissions.editsettings")
    private String jukeboxDefaultEditSettingsPerm;

    Configuration(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    protected FileConfiguration getFileConfiguration() {
        if (!new File(this.plugin.getDataFolder(), "config.yml").exists()) {
            this.plugin.saveDefaultConfig();
        }

        return this.plugin.getConfig();
    }

    public boolean isDebug() {
        return this.debug;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getLocale() {
        return this.locale;
    }

    public boolean isAllJukeboxAreSuper() {
        return this.allJukeboxAreSuper;
    }

    public int getJukeboxDefaultDistance() {
        return this.jukeboxDefaultDistance;
    }

    public int getJukeboxDefaultVolume() {
        return this.jukeboxDefaultVolume;
    }

    public boolean isJukeboxDefaultUseGlobalMusics() {
        return this.jukeboxDefaultUseGlobalMusics;
    }

    public boolean isJukeboxDefaultUseAutoplay() {
        return this.jukeboxDefaultUseAutoplay;
    }

    public boolean isJukeboxDefaultParticles() {
        return this.jukeboxDefaultParticles;
    }

    public boolean isJukeboxDefaultPlayWithRedstone() {
        return this.jukeboxDefaultPlayWithRedstone;
    }

    public boolean isJukeboxDefaultAnnouncements() {
        return this.jukeboxDefaultAnnouncements;
    }

    public String getJukeboxDefaultInteractPerm() {
        return this.jukeboxDefaultInteractPerm;
    }

    public String getJukeboxDefaultEditMusicsPerm() {
        return this.jukeboxDefaultEditMusicsPerm;
    }

    public String getJukeboxDefaultEditSettingsPerm() {
        return this.jukeboxDefaultEditSettingsPerm;
    }

}
