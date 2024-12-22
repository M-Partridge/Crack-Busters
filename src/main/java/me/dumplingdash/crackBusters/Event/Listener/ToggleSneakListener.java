package me.dumplingdash.crackBusters.Event.Listener;

import me.dumplingdash.crackBusters.Core.Game.CBPlayer;
import me.dumplingdash.crackBusters.Core.Game.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class ToggleSneakListener implements Listener {

    @EventHandler
    public static void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        CBPlayer player = GameManager.getPlayer(event.getPlayer().getUniqueId());
        player.setSneaking(event.isSneaking());
    }
}
