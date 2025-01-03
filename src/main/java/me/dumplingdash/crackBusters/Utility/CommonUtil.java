package me.dumplingdash.crackBusters.Utility;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Locale;

public class CommonUtil {

    public static void sendTitleToAll(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    public static void sendActionBarMessageToAll(String message) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        }
    }

    public static Player findNearestPlayer(Location location) {
        Player closest = null;
        double distance = -1;
        for(Player player : Bukkit.getOnlinePlayers()) {
            double temp = player.getLocation().distance(location);
            if(distance == -1 || temp < distance) {
                closest = player;
                distance = player.getLocation().distance(location);
            }
        }
        return closest;
    }
    public static String getClassID(Class<?> c) {
        return c.getSimpleName().toLowerCase(Locale.ROOT);
    }
}
