package me.dumplingdash.crackBusters.Event.Listener;

import me.dumplingdash.crackBusters.Item.ActionBarHover;
import me.dumplingdash.crackBusters.Item.CBItem;
import me.dumplingdash.crackBusters.Utility.ItemUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerItemHeldListener implements Listener {

    @EventHandler
    public static void onItemHeldEvent(PlayerItemHeldEvent event) {
        CBItem cbItem = ItemUtil.getCBItem(event.getPlayer().getInventory().getItem(event.getNewSlot()));
        if(cbItem instanceof ActionBarHover) {
            ((ActionBarHover) cbItem).handleActionBarHover(event);
        }
    }
}
