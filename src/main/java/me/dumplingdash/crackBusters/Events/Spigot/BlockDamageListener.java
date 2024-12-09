package me.dumplingdash.crackBusters.Events.Spigot;

import me.dumplingdash.crackBusters.Core.Game.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public class BlockDamageListener implements Listener {

    @EventHandler
    public static void onBlockDamage(BlockDamageEvent event) {
        GameManager.handleBlockDamage(event);
    }
}
