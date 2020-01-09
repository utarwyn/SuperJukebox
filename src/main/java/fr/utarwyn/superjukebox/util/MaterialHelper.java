package fr.utarwyn.superjukebox.util;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Class used to manipulate materials for
 * multiple versions of Minecraft.
 *
 * @author Utarwyn
 * @since 1.2.0
 */
public class MaterialHelper {

    private static ConcurrentMap<String, Material> cache;

    static {
        cache = new ConcurrentHashMap<>();
    }

    private MaterialHelper() {
        // Not implemented
    }

    /**
     * Find a material from a list of names.
     *
     * @param names names that the material could have
     * @return the material with one of names, if exists
     */
    public static Material findMaterial(String... names) {
        if (cache.containsKey(names[0])) {
            return cache.get(names[0]);
        }

        for (Material material : Material.values()) {
            for (String name : names) {
                if (material.name().equalsIgnoreCase(name)) {
                    cache.put(names[0], material);
                    return material;
                }
            }
        }

        throw new NullPointerException("A material cannot be found with names " + Arrays.toString(names));
    }

}
