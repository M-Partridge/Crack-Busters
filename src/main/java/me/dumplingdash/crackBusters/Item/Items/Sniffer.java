package me.dumplingdash.crackBusters.Item.Items;

import me.dumplingdash.crackBusters.Core.Game.CBPlayer;
import me.dumplingdash.crackBusters.Core.Game.GameManager;
import me.dumplingdash.crackBusters.Core.Game.HiddenBlock;
import me.dumplingdash.crackBusters.Core.Game.Zone;
import me.dumplingdash.crackBusters.Enums.Team;
import me.dumplingdash.crackBusters.Item.CBItem;
import me.dumplingdash.crackBusters.Item.RightClickAbility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Sniffer extends CBItem implements RightClickAbility {
    private static final HashMap<CBPlayer, Long> cooldown = new HashMap<>();
    public static final int cooldownTime = 5; // seconds
    @Override
    public String getName() {
        return "The Sniffer";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                "Right click to see how far you are from the hidden block in your zone"
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
        return Material.RECOVERY_COMPASS;
    }

    @Override
    public void handleRightClick(PlayerInteractEvent event) {
        CBPlayer player = GameManager.getPlayer(event.getPlayer().getUniqueId());
        Zone zone = player.getZone();
        Team team = player.getTeam();

        if(zone == null || team != Team.CRACK_BUSTER) {
            return;
        }

        HiddenBlock hiddenBlock = GameManager.getHiddenBlock(zone.getMaterial());

        if(hiddenBlock == null) {
            return;
        }

        // check if on cooldown
        if(getCooldownTime(player) != 0) {
            player.sendErrorMessage("Sniffer on cooldown");
            return;
        }

        double distance = hiddenBlock.getLocation().distance(player.getPlayer().getLocation());
        String message;
        if(distance > 200) {
            message = "You are very far from the " + zone.getMaterial().name();
        } else if(distance > 100) {
            message = "You are within 200 blocks of the " + zone.getMaterial().name();
        } else if(distance > 50) {
            message = "You are within 100 blocks of the " + zone.getMaterial().name();
        } else if(distance > 25) {
            message = "You are within 50 blocks of the " + zone.getMaterial().name();
        } else if(distance > 10) {
            message = "You are within 25 blocks of the " + zone.getMaterial().name();
        } else {
            message = "Take a look around";
        }
        player.getPlayer().sendMessage(message);

        // apply cooldown
        cooldown.put(player, System.currentTimeMillis());
    }

    public long getCooldownTime(CBPlayer player) {
        long lastUse = cooldown.getOrDefault(player, 0L);
        return Math.max(System.currentTimeMillis() - (lastUse + Sniffer.cooldownTime * 1000), 0);
    }
}
