package me.dumplingdash.crackBusters;

import me.dumplingdash.crackBusters.Config.CBConfig;
import me.dumplingdash.crackBusters.Core.CBRegistry;
import me.dumplingdash.crackBusters.Core.Game.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrackBusters extends JavaPlugin {

    public static CrackBusters instance;
    @Override
    public void onEnable() {
        System.out.println("CrackBusters Starting");

        if(instance == null) {
            instance = this;
        }

        CBConfig.enable(getConfig());
        CBRegistry.enable();
        GameManager.enable();



    }


    @Override
    public void onDisable() {
        System.out.println("CrackBusters Stopping");

        CBConfig.disable();
    }

    public static void logMessage(String message) {
        System.out.println("CB : " + message);
    }
}