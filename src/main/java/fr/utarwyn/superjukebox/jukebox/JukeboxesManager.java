package fr.utarwyn.superjukebox.jukebox;

import fr.utarwyn.superjukebox.AbstractManager;
import fr.utarwyn.superjukebox.SuperJukebox;
import fr.utarwyn.superjukebox.menu.Menus;
import fr.utarwyn.superjukebox.util.FlatFile;
import fr.utarwyn.superjukebox.util.JUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

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

	void createSuperJukebox(Block block) {
		Jukebox jukebox = new Jukebox(this.getNewJukeboxId(), block);

		// Save jukebox in memory
		this.jukeboxes.add(jukebox);

		// Save all jukebox's settings on disk
		this.database.getConfiguration().createSection("jukebox" + jukebox.getId());

		this.saveJukeboxLocationOnDisk(jukebox);
		this.saveJukeboxSettingsOnDisk(jukebox);
		this.saveJukeboxPermissionsOnDisk(jukebox);
	}

	Jukebox getJukeboxAt(Block block) {
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
