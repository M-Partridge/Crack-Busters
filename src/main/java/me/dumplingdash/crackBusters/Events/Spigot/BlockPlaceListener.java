package me.dumplingdash.crackBusters.Events.Spigot;

import me.dumplingdash.crackBusters.Core.Game.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent event) {
        GameManager.handleBlockPlaced(event);
    }
}
