package fr.utarwyn.superjukebox.jukebox.settings;

import fr.utarwyn.superjukebox.configuration.Files;
import fr.utarwyn.superjukebox.jukebox.perm.Permission;
import fr.utarwyn.superjukebox.jukebox.perm.PermissionType;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Stores all settings for one jukebox.
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class JukeboxSettings {

    /**
     * Under this number of blocks, Minecraft seems to not respect the sound distance.
     * So we have to define a minimum distance to keep a good Jukebox behavior.
     */
    private static final int MIN_DISTANCE = 15;

    /**
     * Max distance where a player can hear the sound of the jukebox
     */
    private Setting<Integer> distance;

    /**
     * Volume of a jukebox, in percent.
     */
    private Setting<Integer> volume;

    /**
     * True if the jukebox have to run automatically new music
     */
    private Setting<Boolean> autoplay;

    /**
     * True if the jukebox uses the global musics stored by the server
     */
    private Setting<Boolean> useGlobalMusics;

    /**
     * True if the plugin has to display particles when playing music
     */
    private Setting<Boolean> particles;

    /**
     * If this setting is set to true, a redstone signal is needed to play music
     */
    private Setting<Boolean> playWithRedstone;

    /**
     * Activate music announcements
     */
    private Setting<Boolean> announcements;

    /**
     * Permission to interact with the jukebox and to show its main menu.
     */
    private Permission interactPerm;

    /**
     * Permission to edit musics of the jukebox.
     */
    private Permission editMusicsPerm;

    /**
     * Permission to change settings of the jukebox.
     */
    private Permission editSettingsPerm;

    // TODO: setting for announcing new music

    /**
     * Constructs the jukebox settings object and apply default configuration!
     */
    public JukeboxSettings() {
        // Default settings
        this.distance = new Setting<>("distance",
                Files.getConfiguration().getJukeboxDefaultDistance(),
                value -> value >= MIN_DISTANCE);

        this.volume = new Setting<>("volume",
                Files.getConfiguration().getJukeboxDefaultVolume(),
                value -> value >= 0);

        this.autoplay = new Setting<>("autoplay", Files.getConfiguration().isJukeboxDefaultUseAutoplay());
        this.useGlobalMusics = new Setting<>("globalmusics", Files.getConfiguration().isJukeboxDefaultUseGlobalMusics());
        this.particles = new Setting<>("particles", Files.getConfiguration().isJukeboxDefaultParticles());
        this.playWithRedstone = new Setting<>("playwithredstone", Files.getConfiguration().isJukeboxDefaultPlayWithRedstone());
        this.announcements = new Setting<>("announcements", Files.getConfiguration().isJukeboxDefaultAnnouncements());

        // Default permissions
        this.interactPerm = this.createPermissionNode(Files.getConfiguration().getJukeboxDefaultInteractPerm(), "interact");
        this.editMusicsPerm = this.createPermissionNode(Files.getConfiguration().getJukeboxDefaultEditMusicsPerm(), "editmusics");
        this.editSettingsPerm = this.createPermissionNode(Files.getConfiguration().getJukeboxDefaultEditSettingsPerm(), "editsettings");
    }

    /**
     * Returns the max distance of the jukebox
     *
     * @return Max distance of the jukebox
     */
    public Setting<Integer> getDistance() {
        return this.distance;
    }

    /**
     * Returns the volume of the jukebox, in percent.
     *
     * @return Volume in percent.
     */
    public Setting<Integer> getVolume() {
        return this.volume;
    }

    /**
     * Returns true if the jukebox is in an auto-playing mode
     *
     * @return True if autoplay option is activated for the linked jukebox
     */
    public Setting<Boolean> getAutoplay() {
        return this.autoplay;
    }

    /**
     * Returns true is the jukebox uses the global musics system.
     *
     * @return True for global musics.
     */
    public Setting<Boolean> getUseGlobalMusics() {
        return this.useGlobalMusics;
    }

    /**
     * Returns true is the jukebox has the particles system activated
     *
     * @return True is the plugin has to display particles
     */
    public Setting<Boolean> getParticles() {
        return this.particles;
    }

    /**
     * Returns true is the jukebox has to be powered to play music
     *
     * @return True if redstone has an impact on the jukebox behavior.
     */
    public Setting<Boolean> getPlayWithRedstone() {
        return this.playWithRedstone;
    }

    /**
     * Returns true if music have to be announced.
     *
     * @return Setting value
     */
    public Setting<Boolean> getAnnouncements() {
        return this.announcements;
    }

    /**
     * Returns the interaction permission of the linked jukebox
     *
     * @return The permission node
     */
    public Permission getInteractPerm() {
        return this.interactPerm;
    }

    /**
     * Returns the musics edition permission of the linked jukebox
     *
     * @return The permission node
     */
    public Permission getEditMusicsPerm() {
        return this.editMusicsPerm;
    }

    /**
     * Returns the settings edition permission of the linked jukebox
     *
     * @return The permission node
     */
    public Permission getEditSettingsPerm() {
        return this.editSettingsPerm;
    }

    /**
     * Loads all the jukebox settings from a Bukkit configuration section
     *
     * @param settingsSection Configuration section where all jukebox settings are stored
     * @param permsSection    Configuration section where all jukebox permissions are stored
     */
    public void loadFromConfiguration(ConfigurationSection settingsSection, ConfigurationSection permsSection) {
        // Load settings
        if (settingsSection != null) {
            this.distance.setValue(settingsSection.getInt("distance"));
            this.volume.setValue(settingsSection.getInt("volume"));
            this.autoplay.setValue(settingsSection.getBoolean("autoplay"));
            this.useGlobalMusics.setValue(settingsSection.getBoolean("globalmusics"));
            this.particles.setValue(settingsSection.getBoolean("particles"));
            this.playWithRedstone.setValue(settingsSection.getBoolean("playwithredstone"));
            this.announcements.setValue(settingsSection.getBoolean("announcements"));
        }

        // Load permissions
        if (permsSection != null) {
            this.interactPerm = this.createPermissionNode(permsSection, "interact");
            this.editMusicsPerm = this.createPermissionNode(permsSection, "editmusics");
            this.editSettingsPerm = this.createPermissionNode(permsSection, "editsettings");
        }
    }

    /**
     * Applies all settings values in a Bukkit configuration section
     *
     * @param section The Bukkit configuration section
     */
    public void applySettingsToConfiguration(ConfigurationSection section) {
        section.set("distance", this.distance.getValue());
        section.set("volume", this.volume.getValue());
        section.set("autoplay", this.autoplay.getValue());
        section.set("globalmusics", this.useGlobalMusics.getValue());
        section.set("particles", this.particles.getValue());
        section.set("playwithredstone", this.playWithRedstone.getValue());
        section.set("announcements", this.announcements.getValue());
    }

    /**
     * Applies all permissions in a Bukkit configuration section
     *
     * @param section The Bukkit configuration section
     */
    public void applyPermissionsToConfiguration(ConfigurationSection section) {
        section.set("interact", this.interactPerm.getType().name());
        section.set("editmusics", this.editMusicsPerm.getType().name());
        section.set("editsettings", this.editSettingsPerm.getType().name());
    }

    /**
     * Creates a permission node from a name and the configuration section
     *
     * @param configSection  Configuration section used to get the type of the permission
     * @param permissionName Permission name used to create the Bukkit permission
     * @return The generated permission node
     */
    private Permission createPermissionNode(ConfigurationSection configSection, String permissionName) {
        return new Permission(PermissionType.getByName(configSection.getString(permissionName)), permissionName);
    }

    /**
     * Creates a permission node from a permission type and a name
     *
     * @param permissionType Permission type
     * @param permissionName Permission name used to create the Bukkit permission
     * @return The generated permission node
     */
    private Permission createPermissionNode(String permissionType, String permissionName) {
        return new Permission(PermissionType.getByName(permissionType), permissionName);
    }

}
