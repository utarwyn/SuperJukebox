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

	Jukebox getJukeboxAt(Block block) {
		for (Jukebox jukebox : this.jukeboxes)
			if (jukebox.getBlock().equals(block))
				return jukebox;

		return null;
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
			jukebox.getSettings().loadFromConfigurationSection(section.getConfigurationSection("settings"));

			// And put the jukebox into the memory list!
			this.jukeboxes.add(jukebox);
		}
	}

}
