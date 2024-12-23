package me.dumplingdash.crackBusters.Utility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Locale;

public class CommonUtil {

    public static void sendTitleToAll(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    public static String getClassID(Class<?> c) {
        return c.getSimpleName().toLowerCase(Locale.ROOT);
    }
}
