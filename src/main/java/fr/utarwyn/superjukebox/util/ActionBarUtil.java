package fr.utarwyn.superjukebox.util;

import fr.utarwyn.superjukebox.SuperJukebox;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Used to send action message to players.
 * Based on a class written by @ConnorLinfoot.
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class ActionBarUtil {

    /**
     * Package where NMS classes are stored. Used before 1.17
     */
    private static final String NMS_PACKAGE;

    /**
     * Package where Minecraft classes are stored. Used in 1.17+
     */
    private static final String MC_PACKAGE;

    /**
     * Package where Craftbukkit classes are stored
     */
    private static final String CRAFTBUKKIT_PACKAGE;

    /**
     * Tell utility class to use pre 1.8 NMS objects
     */
    private static final boolean USE_PRE_18;

    /**
     * Network chat 1.17+ NMS package
     */
    private static final String NETWORK_CHAT = "network.chat";

    static {
        String version = ServerVersion.getBukkitVersion();

        // Using remapped source code of Minecraft server? 1.17+
        MC_PACKAGE = ServerVersion.isNewerThan(ServerVersion.V1_16) ? "net.minecraft." : null;
        NMS_PACKAGE = "net.minecraft.server." + version + ".";
        CRAFTBUKKIT_PACKAGE = "org.bukkit.craftbukkit." + version + ".";

        USE_PRE_18 = version.equalsIgnoreCase("v1_8_R1");
    }

    private ActionBarUtil() {
        // not implemented
    }

    public static void sendActionTitle(Player player, String message) {
        if (!player.isOnline()) {
            return;
        }

        if (ServerVersion.isNewerThan(ServerVersion.V1_11)) {
            ActionBarUtil.sendActionTitlePost12(player, message);
        } else {
            ActionBarUtil.sendActionTitlePre12(player, message);
        }
    }

    public static void sendActionTitle(Player player, String message, int duration) {
        sendActionTitle(player, message);

        if (duration >= 0) {
            // Sends empty message at the end of the duration. Allows messages shorter than 3 seconds, ensures precision.
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendActionTitle(player, "");
                }
            }.runTaskLater(SuperJukebox.getInstance(), duration + 1L);
        }

        // Re-sends the messages every 3 seconds so it doesn't go away from the player's screen.
        while (duration > 40) {
            duration -= 40;
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendActionTitle(player, message);
                }
            }.runTaskLater(SuperJukebox.getInstance(), duration);
        }
    }

    public static void sendActionTitleToPlayers(String message, Iterable<Player> players) {
        sendActionTitleToPlayers(message, players, -1);
    }

    public static void sendActionTitleToPlayers(String message, Iterable<Player> players, int duration) {
        for (Player p : players) {
            sendActionTitle(p, message, duration);
        }
    }

    private static void sendActionTitlePre12(Player player, String message) {
        try {
            Class<?> craftPlayerClass = getCraftbukkitClass("entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object ppoc;
            Class<?> c3 = getNMSClass("IChatBaseComponent");
            Class<?> c4 = getNMSClass("PacketPlayOutChat");
            Class<?> c5 = getNMSClass("Packet");

            if (USE_PRE_18) {
                Class<?> c2 = getNMSClass("ChatSerializer");
                Method m3 = c2.getDeclaredMethod("a", String.class);
                Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
                ppoc = c4.getConstructor(c3, byte.class).newInstance(cbc, (byte) 2);
            } else {
                Class<?> c2 = getNMSClass("ChatComponentText");
                Object o = c2.getConstructor(String.class).newInstance(message);
                ppoc = c4.getConstructor(c3, byte.class).newInstance(o, (byte) 2);
            }

            Method m1 = craftPlayerClass.getDeclaredMethod("getHandle");
            Object h = m1.invoke(craftPlayer);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception ex) {
            Log.log(Level.WARNING, "Cannot send action title packet (<1.12) for " + player.getName() + "!", ex);
        }
    }

    private static void sendActionTitlePost12(Player player, String message) {
        try {
            Class<?> craftPlayerClass = getCraftbukkitClass("entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object ppoc;
            Class<?> c4 = getNMSClass("PacketPlayOutChat", "network.protocol.game");
            Class<?> c5 = getNMSClass("Packet", "network.protocol");
            Class<?> c2 = getNMSClass("ChatComponentText", NETWORK_CHAT);
            Class<?> c3 = getNMSClass("IChatBaseComponent", NETWORK_CHAT);
            Class<?> chatMessageTypeClass = getNMSClass("ChatMessageType", NETWORK_CHAT);
            Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
            Object chatMessageType = null;
            for (Object obj : chatMessageTypes) {
                if (obj.toString().equals("GAME_INFO")) {
                    chatMessageType = obj;
                }
            }
            Object o = c2.getConstructor(String.class).newInstance(message);
            if (ServerVersion.isOlderThan(ServerVersion.V1_16)) {
                ppoc = c4.getConstructor(c3, chatMessageTypeClass).newInstance(o, chatMessageType);
            } else {
                ppoc = c4.getConstructor(c3, chatMessageTypeClass, UUID.class).newInstance(o, chatMessageType, player.getUniqueId());
            }

            Method m1 = craftPlayerClass.getDeclaredMethod("getHandle");
            Object h = m1.invoke(craftPlayer);
            Field f1;

            // 1.17+ :: New way of retrieving player connection instance
            if (ServerVersion.isNewerThan(ServerVersion.V1_16)) {
                f1 = h.getClass().getField("b");
            } else {
                f1 = h.getClass().getField("playerConnection");
            }

            Object pc = f1.get(h);
            Method m5 = getNMSDynamicMethod(pc.getClass(), "sendPacket", "a", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception ex) {
            Log.log(Level.WARNING, "Cannot send action title packet (>=1.12) for " + player.getName() + "!", ex);
        }
    }

    private static Class<?> getCraftbukkitClass(String className) throws ClassNotFoundException {
        return Class.forName(CRAFTBUKKIT_PACKAGE + className);
    }

    private static Class<?> getNMSClass(String className) throws ClassNotFoundException {
        return Class.forName(NMS_PACKAGE + className);
    }

    private static Class<?> getNMSClass(String className, String package17)
            throws ClassNotFoundException {
        return Class.forName((MC_PACKAGE != null ? MC_PACKAGE + package17 + "." : NMS_PACKAGE) + className);
    }

    private static Method getNMSDynamicMethod(Class<?> clazz, String name, String name18, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        return clazz.getMethod(ServerVersion.isNewerThan(ServerVersion.V1_17) ? name18 : name, parameterTypes);
    }

}
