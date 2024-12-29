package me.dumplingdash.crackBusters.Event.Listener;

import me.dumplingdash.crackBusters.Item.Items.PedestalTool;
import me.dumplingdash.crackBusters.Item.Items.ZoneTool;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public static void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.getInventory().clear();
        ItemStack zoneTool = (new ZoneTool()).getItem();
        player.getInventory().addItem(zoneTool);
        ItemStack pedestalTool = (new PedestalTool()).getItem();
        player.getInventory().addItem(pedestalTool);
    }
}
