package me.dumplingdash.crackBusters.Item.Items;

import me.dumplingdash.crackBusters.Core.Game.CBPlayer;
import me.dumplingdash.crackBusters.Core.Game.GameManager;
import me.dumplingdash.crackBusters.Enums.GameState;
import me.dumplingdash.crackBusters.Item.CBItem;
import me.dumplingdash.crackBusters.Item.RightClickAbility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Crack extends CBItem implements RightClickAbility {
    private static final HashMap<CBPlayer, Integer> uses = new HashMap<>();
    @Override
    public String getName() {
        return ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Crack";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                net.md_5.bungee.api.ChatColor.of(CBItem.loreColor) + "Right Click for Speed 2 and Invisibility"
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
        return Material.SUGAR;
    }

    @Override
    public void handleRightClick(PlayerInteractEvent event) {
        if(GameManager.getGameState() != GameState.BREAKING) return;
        CBPlayer player = GameManager.getPlayer(event.getPlayer().getUniqueId());
        if(uses.containsKey(player)) {
            if(uses.get(player) == 0) {
                return;
            }
        } else {
            uses.put(player, 2);
        }
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 2, false, false ,false));
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10 * 20, 1, false, false ,false));
        uses.put(player, uses.get(player) - 1);

        player.getPlayer().getLocation().getWorld().playSound(player.getPlayer().getLocation(), Sound.BLOCK_GLASS_BREAK, 3, 1);
        event.getItem().setAmount(uses.get(player));
    }
}
