package fr.utarwyn.superjukebox.util;

import fr.utarwyn.superjukebox.SuperJukebox;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Used to send action message to players.
 * Thanks to ConnorLinfoot.
 *
 * @author Utarwyn
 * @since 1.0.0
 */
public class ActionBarUtil {

    private static String serverVersion;

    private static boolean useOldMethods;

    private ActionBarUtil() {

    }

    static {
        serverVersion = Bukkit.getServer().getClass().getPackage().getName();
        serverVersion = serverVersion.substring(serverVersion.lastIndexOf(".") + 1);

        ActionBarUtil.useOldMethods = serverVersion.equalsIgnoreCase("v1_8_R1");
    }

    public static void sendActionTitle(Player player, String message) {
        if (!player.isOnline()) {
            return;
        }

        if (MinecraftVersion.current() == MinecraftVersion.POST1_12) {
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
            }.runTaskLater(SuperJukebox.getInstance(), duration + 1);
        }

        // Re-sends the messages every 3 seconds so it doesn't go away from the player's screen.
        while (duration > 40) {
            duration -= 40;
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendActionTitle(player, message);
                }
            }.runTaskLater(SuperJukebox.getInstance(), (long) duration);
        }
    }

    public static void sendActionTitleToAllPlayers(String message) {
        sendActionTitleToAllPlayers(message, -1);
    }

    public static void sendActionTitleToAllPlayers(String message, int duration) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendActionTitle(p, message, duration);
        }
    }

    private static void sendActionTitlePre12(Player player, String message) {
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object ppoc;
            Class<?> c4 = Class.forName("net.minecraft.server." + serverVersion + ".PacketPlayOutChat");
            Class<?> c5 = Class.forName("net.minecraft.server." + serverVersion + ".Packet");

            if (useOldMethods) {
                Class<?> c2 = Class.forName("net.minecraft.server." + serverVersion + ".ChatSerializer");
                Class<?> c3 = Class.forName("net.minecraft.server." + serverVersion + ".IChatBaseComponent");
                Method m3 = c2.getDeclaredMethod("a", String.class);
                Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(cbc, (byte) 2);
            } else {
                Class<?> c2 = Class.forName("net.minecraft.server." + serverVersion + ".ChatComponentText");
                Class<?> c3 = Class.forName("net.minecraft.server." + serverVersion + ".IChatBaseComponent");
                Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);
            }

            Method m1 = craftPlayerClass.getDeclaredMethod("getHandle");
            Object h = m1.invoke(craftPlayer);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void sendActionTitlePost12(Player player, String message) {
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object ppoc;
            Class<?> c4 = Class.forName("net.minecraft.server." + serverVersion + ".PacketPlayOutChat");
            Class<?> c5 = Class.forName("net.minecraft.server." + serverVersion + ".Packet");
            Class<?> c2 = Class.forName("net.minecraft.server." + serverVersion + ".ChatComponentText");
            Class<?> c3 = Class.forName("net.minecraft.server." + serverVersion + ".IChatBaseComponent");
            Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + serverVersion + ".ChatMessageType");
            Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
            Object chatMessageType = null;
            for (Object obj : chatMessageTypes) {
                if (obj.toString().equals("GAME_INFO")) {
                    chatMessageType = obj;
                }
            }
            Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
            ppoc = c4.getConstructor(new Class<?>[]{c3, chatMessageTypeClass}).newInstance(o, chatMessageType);
            Method m1 = craftPlayerClass.getDeclaredMethod("getHandle");
            Object h = m1.invoke(craftPlayer);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
