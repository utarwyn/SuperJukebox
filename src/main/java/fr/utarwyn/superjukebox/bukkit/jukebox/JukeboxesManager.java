package fr.utarwyn.superjukebox.jukebox;

import fr.utarwyn.superjukebox.AbstractManager;
import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.menu.Menus;
import fr.utarwyn.superjukebox.music.Music;
import fr.utarwyn.superjukebox.music.MusicManager;
import fr.utarwyn.superjukebox.util.FlatFile;
import fr.utarwyn.superjukebox.util.JUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all super jukeboxes of the server!
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class JukeboxesManager extends AbstractManager {

	private FlatFile database;

	private List<Jukebox> jukeboxes;

	public JukeboxesManager() {
		super(SuperJukebox.getInstance());

		this.registerListener(new JukeboxListener(this));
	}

	@Override
	public void initialize() {
		this.jukeboxes = new ArrayList<>();

		if (this.database == null)
			this.database = new FlatFile("jukeboxes.yml");

		this.reloadDatabase();
	}

	@Override
	protected void unload() {
		// Unload all jukeboxes
		for (Jukebox jukebox : this.jukeboxes)
			jukebox.unload();

		// Close all menus
		Menus.closeAll();
	}

	public List<Jukebox> getJukeboxes() {
		return new ArrayList<>(this.jukeboxes);
	}

	public void createSuperJukebox(Block block) {
		Jukebox jukebox = new Jukebox(this.getNewJukeboxId(), block);

		// Save jukebox in memory
		this.jukeboxes.add(jukebox);

		// Save all jukebox's settings on disk
		this.database.getConfiguration().createSection("jukebox" + jukebox.getId());

		this.saveJukeboxLocationOnDisk(jukebox);
		this.saveJukeboxSettingsOnDisk(jukebox);
		this.saveJukeboxPermissionsOnDisk(jukebox);
	}

	void removeSuperJukebox(Block block) {
		Jukebox jukebox = this.getJukeboxAt(block);
		if (jukebox == null) return;

		// Unload the jukebox and remove it from the memory
		jukebox.unload();
		this.jukeboxes.remove(jukebox);

		// Remove the jukebox from the configuration
		this.database.getConfiguration().set("jukebox" + jukebox.getId(), null);
		this.database.save();
	}

	public Jukebox getJukeboxAt(Block block) {
		for (Jukebox jukebox : this.jukeboxes)
			if (jukebox.getBlock().equals(block))
				return jukebox;

		return null;
	}

	public void saveJukeboxLocationOnDisk(Jukebox jukebox) {
		ConfigurationSection section = this.getJukeboxConfigSection(jukebox);

		if (!section.isConfigurationSection("location")) {
			section.createSection("location");
		}

		JUtil.saveLocationIntoConfig(section.getConfigurationSection("location"), jukebox.getBlock().getLocation());
		this.database.save();
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
		this.database.save();
	}

	public void saveJukeboxSettingsOnDisk(Jukebox jukebox) {
		ConfigurationSection section = this.getJukeboxConfigSection(jukebox);

		if (!section.isConfigurationSection("settings")) {
			section.createSection("settings");
		}

		jukebox.getSettings().applySettingsToConfiguration(section.getConfigurationSection("settings"));
		this.database.save();
	}

	public void saveJukeboxPermissionsOnDisk(Jukebox jukebox) {
		ConfigurationSection section = this.getJukeboxConfigSection(jukebox);

		if (!section.isConfigurationSection("permissions")) {
			section.createSection("permissions");
		}

		jukebox.getSettings().applyPermissionsToConfiguration(section.getConfigurationSection("permissions"));
		this.database.save();
	}

	private void reloadDatabase() {
		ConfigurationSection section;
		YamlConfiguration conf = this.database.getConfiguration();

		this.jukeboxes.clear();

		for (String confKey : conf.getKeys(false)) {
			// Not a good jukebox?
			if (!confKey.contains("jukebox"))
				continue;

			section = conf.getConfigurationSection(confKey);

			int id = Integer.parseInt(confKey.replace("jukebox", ""));
			Location loc = JUtil.getLocationFromConfig(section.getConfigurationSection("location"));
			Jukebox jukebox = new Jukebox(id, loc.getBlock());

			// Import settings into the jukebox settings object
			jukebox.getSettings().loadFromConfiguration(
					section.getConfigurationSection("settings"),
					section.getConfigurationSection("permissions")
			);

			// Import custom musics into the jukebox object
			if (section.isList("musics"))
				jukebox.loadMusicsFromConfiguration(section.getIntegerList("musics"));

			// And put the jukebox into the memory list!
			this.jukeboxes.add(jukebox);
		}
	}

	private ConfigurationSection getJukeboxConfigSection(Jukebox jukebox) {
		return this.database.getConfiguration().getConfigurationSection("jukebox" + jukebox.getId());
	}

	private int getNewJukeboxId() {
		int max = 0;

		for (Jukebox jukebox : this.jukeboxes)
			if (jukebox.getId() > max)
				max = jukebox.getId();

		return max + 1;
	}

}
