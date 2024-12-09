package me.dumplingdash.crackBusters.Config;

import me.dumplingdash.crackBusters.CrackBusters;
import org.bukkit.configuration.file.FileConfiguration;

public class CBConfig {

    private static FileConfiguration config;
    public static void enable(FileConfiguration c) {
        config = c;
    }

    public static void disable() {
        CrackBusters.instance.saveConfig();
    }

    public static Object loadLocation(String path) {
        return config.getLocation(path, null);
    }

    public static void saveData(String path, Object value) {
        config.set(path, value);
    }

}
