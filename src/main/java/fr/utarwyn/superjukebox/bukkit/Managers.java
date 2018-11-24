package fr.utarwyn.superjukebox.bukkit;

import java.util.HashMap;

/**
 * Class used to manage managers (manager-ception).
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class Managers {

	/**
	 * It's an utility class
	 */
	private Managers() {

	}

	/**
	 * Cache map for instances of managers
	 */
	private static HashMap<Class<? extends AbstractManager>, AbstractManager> instances = new HashMap<>();

	/**
	 * Register a specific manager in the memory
	 *
	 * @param clazz    Class of the manager
	 * @param instance Instance of the manager (Object)
	 * @throws IllegalAccessException If the manager is already registered
	 */
	static void registerManager(Class<? extends AbstractManager> clazz, AbstractManager instance) throws IllegalAccessException {
		if (instances.containsKey(clazz))
			throw new IllegalAccessException("Manager " + clazz + " duplicated! " + instance);

		instances.put(clazz, instance);
	}

	/**
	 * Gets an instance of a manager by its class
	 *
	 * @param clazz Class of the manager to get
	 * @param <T>   Class type of the manager
	 * @return Manager if found otherwise null
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T getInstance(Class<T> clazz) {
		AbstractManager instance = instances.get(clazz);

		if (instance == null)
			return null;
		if (clazz.isInstance(instance))
			return (T) instance;

		return null;
	}

	/**
	 * Reload all managers
	 */
	public static void reloadAll() {
		for (AbstractManager manager : instances.values()) {
			manager.unload();
			manager.initialize();
		}
	}

	/**
	 * Unload all managers
	 */
	public static void unloadAll() {
		for (AbstractManager manager : instances.values())
			manager.unload();
	}

	/**
	 * Reload manager by its class
	 *
	 * @param managerClazz Class of the manager to reload
	 */
	public static void reload(Class<? extends AbstractManager> managerClazz) {
		if (!instances.containsKey(managerClazz)) return;

		instances.get(managerClazz).unload();
		instances.get(managerClazz).initialize();
	}

}