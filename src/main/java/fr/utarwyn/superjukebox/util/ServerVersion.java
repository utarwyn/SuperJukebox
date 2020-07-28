package fr.utarwyn.superjukebox.util;

import org.bukkit.Bukkit;

public enum ServerVersion {

    V1_8,
    V1_9,
    V1_10,
    V1_11,
    V1_12,
    V1_13,
    V1_14,
    V1_15,
    V1_16;

    private static ServerVersion currentVersion;

    private static final String BUKKIT_VERSION;

    static {
        // Getting the bukkit Server version!
        String path = Bukkit.getServer().getClass().getPackage().getName();
        BUKKIT_VERSION = path.substring(path.lastIndexOf('.') + 1);

        for (ServerVersion version : values()) {
            if (BUKKIT_VERSION.startsWith(version.name().toLowerCase())) {
                currentVersion = version;
                break;
            }
        }
    }

    public static ServerVersion get() {
        return currentVersion;
    }

    public static String getBukkitVersion() {
        return BUKKIT_VERSION;
    }

    public static boolean is(ServerVersion version) {
        return currentVersion.equals(version);
    }

    public static boolean isOlderThan(ServerVersion version) {
        return get().ordinal() < version.ordinal();
    }

    public static boolean isNewerThan(ServerVersion version) {
        return get().ordinal() > version.ordinal();
    }

}
