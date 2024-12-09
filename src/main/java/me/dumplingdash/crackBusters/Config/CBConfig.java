package me.dumplingdash.crackBusters.Config;

import me.dumplingdash.crackBusters.CrackBusters;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nullable;

public class CBConfig {

    private static FileConfiguration config;
    public static void enable(FileConfiguration c) {
        config = c;
    }

    public static void disable() {
        CrackBusters.instance.saveConfig();
    }

    public static int loadInteger(String path) {
        return config.getInt(path, -1);
    }
    @Nullable
    public static Location loadLocation(String path) {
        return config.getLocation(path, null);
    }

    public static void saveData(String path, Object value) {
        config.set(path, value);
    }

}
