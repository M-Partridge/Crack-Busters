package me.dumplingdash.crackBusters.Event.Listener;

import me.dumplingdash.crackBusters.Item.CBItem;
import me.dumplingdash.crackBusters.Item.InventoryClick;
import me.dumplingdash.crackBusters.Utility.ItemUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        CBItem cbItem = ItemUtil.getCBItem(item);
        if(cbItem instanceof InventoryClick) {
            ((InventoryClick) cbItem).handleInventoryClick(event);
        }
    }
}
