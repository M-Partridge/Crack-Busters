package me.dumplingdash.crackBusters.Events.Spigot;

import me.dumplingdash.crackBusters.Core.Game.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent event) {
        GameManager.handleBlockBreak(event);
    }
}
