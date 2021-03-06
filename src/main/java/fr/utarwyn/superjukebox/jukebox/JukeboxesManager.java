package fr.utarwyn.superjukebox.jukebox;

import fr.utarwyn.superjukebox.AbstractManager;
import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.jukebox.config.ConfigurationLocationAdapter;
import fr.utarwyn.superjukebox.jukebox.config.JukeboxConfigurationException;
import fr.utarwyn.superjukebox.menu.MenuManager;
import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.MusicManager;
import fr.utarwyn.superjukebox.util.FlatFile;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Manages all super jukeboxes of the server!
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class JukeboxesManager extends AbstractManager {

    private FlatFile storage;

    private List<Jukebox> jukeboxes;

    public JukeboxesManager() {
        super(SuperJukebox.getInstance());
        this.registerListener(new JukeboxListener(this));
    }

    @Override
    public void initialize() {
        this.storage = new FlatFile("jukeboxes.yml");
        this.jukeboxes = new ArrayList<>();

        // Start loading jukeboxes in loaded worlds
        for (World world : getPlugin().getServer().getWorlds()) {
            this.loadJukeboxesOfWorld(world);
        }
    }

    @Override
    protected void unload() {
        // Unload all jukeboxes and clear storage
        this.jukeboxes.forEach(Jukebox::unload);
        this.jukeboxes.clear();

        // Close all menus
        SuperJukebox.getInstance().getInstance(MenuManager.class).closeAll();
    }

    public List<Jukebox> getJukeboxes() {
        return new ArrayList<>(this.jukeboxes);
    }

    public void createSuperJukebox(Block block) {
        Jukebox jukebox = new Jukebox(this.getNewJukeboxId(), block);

        // Save jukebox in memory
        this.jukeboxes.add(jukebox);

        // Save all jukebox's settings on disk
        this.storage.getConfig().createSection("jukebox" + jukebox.getId());

        this.saveJukeboxLocationOnDisk(jukebox);
        this.saveJukeboxSettingsOnDisk(jukebox);
        this.saveJukeboxPermissionsOnDisk(jukebox);
    }

    void loadJukeboxesOfWorld(World world) {
        Configuration configuration = this.storage.getConfig();
        int loaded = 0;
        for (String key : configuration.getKeys(false)) {
            ConfigurationSection section = configuration.getConfigurationSection(key);

            if (section != null && world.getName().equals(section.getString("location.world"))) {
                try {
                    this.jukeboxes.add(this.createJukebox(section));
                    loaded++;
                } catch (JukeboxConfigurationException e) {
                    this.logger.log(Level.WARNING, String.format("Error when loading %s", key), e);
                }
            }
        }

        this.getPlugin().getLogger().log(Level.INFO, "{0} jukebox(es) loaded for world {1}",
                new Object[]{loaded, world.getName()});
    }

    void unloadJukeboxesOfWorld(World world) {
        this.jukeboxes.removeIf(jukebox -> jukebox.getBlock().getWorld().equals(world));
        this.getPlugin().getLogger().log(
                Level.INFO,
                "All jukeboxes of world {0} were unloaded",
                world.getName()
        );
    }

    void removeSuperJukebox(Block block) {
        Jukebox jukebox = this.getJukeboxAt(block);
        if (jukebox == null) return;

        // Unload the jukebox and remove it from the memory
        jukebox.unload();
        this.jukeboxes.remove(jukebox);

        // Remove the jukebox from the configuration
        this.storage.getConfig().set("jukebox" + jukebox.getId(), null);
        this.storage.save();
    }

    public Jukebox getJukeboxAt(Block block) {
        for (Jukebox jukebox : this.jukeboxes)
            if (jukebox.getBlock().equals(block))
                return jukebox;

        return null;
    }

    private void saveJukeboxLocationOnDisk(Jukebox jukebox) {
        ConfigurationSection section = this.getJukeboxConfigSection(jukebox);

        if (!section.isConfigurationSection("location")) {
            section.createSection("location");
        }

        // Update jukebox location
        ConfigurationLocationAdapter locationAdapter =
                new ConfigurationLocationAdapter(jukebox.getBlock().getLocation());
        locationAdapter.updateConfigurationSection(section.getConfigurationSection("location"));

        this.storage.save();
    }

    public void saveJukeboxMusicsOnDisk(Jukebox jukebox) {
        ConfigurationSection section = this.getJukeboxConfigSection(jukebox);
        MusicManager musicManager = SuperJukebox.getInstance().getInstance(MusicManager.class);
        List<Integer> musicIdList = new ArrayList<>();

        for (Music music : jukebox.getMusics()) {
            Integer musicId = musicManager.getMusicId(music);

            if (musicId != null) {
                musicIdList.add(musicId);
            }
        }

        // Remove the list if there is no music to be saved!
        if (musicIdList.isEmpty()) {
            musicIdList = null;
        }

        section.set("musics", musicIdList);
        this.storage.save();
    }

    public void saveJukeboxSettingsOnDisk(Jukebox jukebox) {
        ConfigurationSection section = this.getJukeboxConfigSection(jukebox);

        if (!section.isConfigurationSection("settings")) {
            section.createSection("settings");
        }

        jukebox.getSettings().applySettingsToConfiguration(section.getConfigurationSection("settings"));
        this.storage.save();
    }

    public void saveJukeboxPermissionsOnDisk(Jukebox jukebox) {
        ConfigurationSection section = this.getJukeboxConfigSection(jukebox);

        if (!section.isConfigurationSection("permissions")) {
            section.createSection("permissions");
        }

        jukebox.getSettings().applyPermissionsToConfiguration(section.getConfigurationSection("permissions"));
        this.storage.save();
    }

    private Jukebox createJukebox(ConfigurationSection config)
            throws JukeboxConfigurationException {
        // Check configuration section
        if (config == null) {
            throw new JukeboxConfigurationException("malformatted configuration section");
        }

        // Check jukebox config section key
        if (!config.getName().startsWith("jukebox")) {
            throw new JukeboxConfigurationException("config section name must starts with 'jukebox'");
        }

        // Check jukebox location
        ConfigurationLocationAdapter locationAdapter = new ConfigurationLocationAdapter(
                config.getConfigurationSection("location")
        );
        Location location = locationAdapter.toLocation();

        if (location == null) {
            throw new JukeboxConfigurationException("bad location");
        }

        // Create the jukebox instance
        int id = Integer.parseInt(config.getName().replace("jukebox", ""));
        Jukebox jukebox = new Jukebox(id, location.getBlock());

        // Import settings into the jukebox settings object
        jukebox.getSettings().loadFromConfiguration(
                config.getConfigurationSection("settings"),
                config.getConfigurationSection("permissions")
        );

        // Import custom musics into the jukebox object
        if (config.isList("musics")) {
            jukebox.loadMusicsFromConfiguration(config.getIntegerList("musics"));
        }

        // Start the player if autoplay is set to true and jukebox has registered music
        if (jukebox.getSettings().getAutoplay().getValue() && !jukebox.getMusics().isEmpty()) {
            jukebox.play(jukebox.getMusics().get(0));
        }

        return jukebox;
    }

    private ConfigurationSection getJukeboxConfigSection(Jukebox jukebox) {
        return this.storage.getConfig().getConfigurationSection("jukebox" + jukebox.getId());
    }

    private int getNewJukeboxId() {
        int max = 0;

        for (Jukebox jukebox : this.jukeboxes) {
            if (jukebox.getId() > max) {
                max = jukebox.getId();
            }
        }

        return max + 1;
    }

}
