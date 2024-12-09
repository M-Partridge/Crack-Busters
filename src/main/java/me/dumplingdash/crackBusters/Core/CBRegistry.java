package me.dumplingdash.crackBusters.Core;

import me.dumplingdash.crackBusters.CrackBusters;
import me.dumplingdash.crackBusters.Events.Spigot.PlayerJoinListener;
import org.bukkit.Bukkit;

import java.util.Arrays;

public class CBRegistry {

    public static void enable() {
        registerListeners();
        registerCommands();
        registerItems();
    }
    public static void registerListeners() {
        Arrays.asList(
                new PlayerJoinListener()
        ).forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, CrackBusters.instance));
    }
    public static void  registerCommands() {

    }
    public static void registerItems() {

    }


}
