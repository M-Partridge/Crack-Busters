package me.dumplingdash.crackBusters.Event.Listener;

import me.dumplingdash.crackBusters.Item.CBItem;
import me.dumplingdash.crackBusters.Item.LeftClickAbility;
import me.dumplingdash.crackBusters.Item.RightClickAbility;
import me.dumplingdash.crackBusters.Utility.ItemUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public static void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        CBItem cbItem = ItemUtil.getCBItem(item);
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(cbItem instanceof RightClickAbility) {
                ((RightClickAbility) cbItem).handleRightClick(event);
            }
        } else if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            if(cbItem instanceof LeftClickAbility) {
                ((LeftClickAbility) cbItem).handleLeftClick(event);
            }
        }
    }
}
