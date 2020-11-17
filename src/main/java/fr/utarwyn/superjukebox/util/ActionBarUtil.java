package fr.utarwyn.superjukebox.util;

import fr.utarwyn.superjukebox.SuperJukebox;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Used to send action message to players.
 * Thanks to ConnorLinfoot.
 *
 * @author Utarwyn
 * @since 0.1.0
 */
public class ActionBarUtil {

    private static final String NMS_PACKAGE = "net.minecraft.server.";

    private static String serverVersion;

    private static boolean useOldMethods;

    static {
        serverVersion = Bukkit.getServer().getClass().getPackage().getName();
        serverVersion = serverVersion.substring(serverVersion.lastIndexOf('.') + 1);

        ActionBarUtil.useOldMethods = serverVersion.equalsIgnoreCase("v1_8_R1");
    }

    private ActionBarUtil() {

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
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object ppoc;
            Class<?> c4 = Class.forName(NMS_PACKAGE + serverVersion + ".PacketPlayOutChat");
            Class<?> c5 = Class.forName(NMS_PACKAGE + serverVersion + ".Packet");

            if (useOldMethods) {
                Class<?> c2 = Class.forName(NMS_PACKAGE + serverVersion + ".ChatSerializer");
                Class<?> c3 = Class.forName(NMS_PACKAGE + serverVersion + ".IChatBaseComponent");
                Method m3 = c2.getDeclaredMethod("a", String.class);
                Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
                ppoc = c4.getConstructor(c3, byte.class).newInstance(cbc, (byte) 2);
            } else {
                Class<?> c2 = Class.forName(NMS_PACKAGE + serverVersion + ".ChatComponentText");
                Class<?> c3 = Class.forName(NMS_PACKAGE + serverVersion + ".IChatBaseComponent");
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
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object ppoc;
            Class<?> c4 = Class.forName(NMS_PACKAGE + serverVersion + ".PacketPlayOutChat");
            Class<?> c5 = Class.forName(NMS_PACKAGE + serverVersion + ".Packet");
            Class<?> c2 = Class.forName(NMS_PACKAGE + serverVersion + ".ChatComponentText");
            Class<?> c3 = Class.forName(NMS_PACKAGE + serverVersion + ".IChatBaseComponent");
            Class<?> chatMessageTypeClass = Class.forName(NMS_PACKAGE + serverVersion + ".ChatMessageType");
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
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception ex) {
            Log.log(Level.WARNING, "Cannot send action title packet (>=1.12) for " + player.getName() + "!", ex);
        }
    }

}
