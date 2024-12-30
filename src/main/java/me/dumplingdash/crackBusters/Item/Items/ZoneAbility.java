package me.dumplingdash.crackBusters.Item.Items;

import me.dumplingdash.crackBusters.Item.CBItem;
import me.dumplingdash.crackBusters.Item.RightClickAbility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ZoneAbility extends CBItem implements RightClickAbility {
    @Override
    public String getName() {
        return ChatColor.BOLD + "Zone Ability";
    }

    @Override
    public List<String> getLore() {
        return null;
    }

    @Override
    public ItemMeta modifyMeta(ItemMeta meta) {
        return meta;
    }

    @Override
    public ItemStack enchantItem(ItemStack item) {
        return item;
    }

    @Override
    public Material getMaterial() {
        return Material.POTTED_OXEYE_DAISY;
    }

    @Override
    public void handleRightClick(PlayerInteractEvent event) {

        event.setCancelled(true);
    }
}
