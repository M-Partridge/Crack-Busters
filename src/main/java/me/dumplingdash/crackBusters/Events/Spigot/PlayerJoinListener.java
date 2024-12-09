package me.dumplingdash.crackBusters.Events.Spigot;

import me.dumplingdash.crackBusters.Core.Game.GameManager;
import me.dumplingdash.crackBusters.Utility.ColorUtils;
import me.dumplingdash.crackBusters.Utility.ParticleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.awt.*;
import java.util.ArrayList;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) {
        GameManager.handlePlayerJoin(event);
    }
}
