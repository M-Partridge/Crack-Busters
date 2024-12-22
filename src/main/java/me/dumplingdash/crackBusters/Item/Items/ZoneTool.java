package me.dumplingdash.crackBusters.Item.Items;

import me.dumplingdash.crackBusters.Core.Game.CBPlayer;
import me.dumplingdash.crackBusters.Core.Game.GameManager;
import me.dumplingdash.crackBusters.Core.Game.Zone;
import me.dumplingdash.crackBusters.CrackBusters;
import me.dumplingdash.crackBusters.Item.CBItem;
import me.dumplingdash.crackBusters.Item.LeftClickAbility;
import me.dumplingdash.crackBusters.Item.RightClickAbility;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class ZoneTool extends CBItem implements RightClickAbility, LeftClickAbility {
    private final static NamespacedKey modeKey = new NamespacedKey(CrackBusters.instance, "ZoneToolMode");
    @Override
    public String getName() {
        return "Zone Tool";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                "Use this item to set zone barriers"
        );
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
        return Material.STICK;
    }

    @Override
    public void handleLeftClick(PlayerInteractEvent event) {
        CBPlayer player = GameManager.getPlayer(event.getPlayer().getUniqueId());
        Zone mode = getMode(event.getPlayer().getInventory().getItemInMainHand());
        // if sneak left click cycle mode
        if(player.isSneaking() && event.getAction() == Action.LEFT_CLICK_AIR) {
            if(mode == null) {
                mode = Zone.NETHERITE;
            }
            Zone[] zones = Zone.values();
            Zone nextMode = zones[(mode.ordinal() + 1) % zones.length];
            ItemStack item = setMode(nextMode, event.getPlayer().getInventory().getItemInMainHand());
            event.getPlayer().getInventory().setItemInMainHand(item);
            player.getPlayer().sendMessage("Set mode to " + nextMode.name());
        } else {
            if(mode == null || event.getClickedBlock() == null) {
                return;
            }
            GameManager.setZoneLocation(mode, event.getClickedBlock().getLocation(), 1);
        }
    }

    @Override
    public void handleRightClick(PlayerInteractEvent event) {
        Zone mode = getMode(event.getPlayer().getInventory().getItemInMainHand());
        if(mode == null || event.getClickedBlock() == null) {
            return;
        }
        GameManager.setZoneLocation(mode, event.getClickedBlock().getLocation(), 2);
    }

    private Zone getMode(ItemStack item) {
        if(item.getItemMeta() != null) {
            if(item.getItemMeta().getPersistentDataContainer().has(modeKey)) {
                return Zone.valueOf(item.getItemMeta().getPersistentDataContainer().get(modeKey, PersistentDataType.STRING));
            }
        }
        return null;
    }

    private ItemStack setMode(Zone newMode, ItemStack item) {
        if(item.getItemMeta() != null) {
            ItemMeta meta = item.getItemMeta();
            meta.getPersistentDataContainer().set(modeKey, PersistentDataType.STRING, newMode.name());
            item.setItemMeta(meta);
        }
        return item;
    }
}