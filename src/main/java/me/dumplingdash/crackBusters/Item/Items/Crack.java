package me.dumplingdash.crackBusters.Item.Items;

import me.dumplingdash.crackBusters.Core.Game.CBPlayer;
import me.dumplingdash.crackBusters.Core.Game.GameManager;
import me.dumplingdash.crackBusters.CrackBusters;
import me.dumplingdash.crackBusters.Enums.GameState;
import me.dumplingdash.crackBusters.Enums.Team;
import me.dumplingdash.crackBusters.Item.ActionBarHover;
import me.dumplingdash.crackBusters.Item.CBItem;
import me.dumplingdash.crackBusters.Item.RightClickAbility;
import me.dumplingdash.crackBusters.Utility.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Crack extends CBItem implements RightClickAbility, ActionBarHover {
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
        if(player.getTeam() != Team.CRACK_BUSTER) return;
        if(uses.containsKey(player)) {
            if(uses.get(player) == 0) {
                return;
            }
        } else {
            uses.put(player, 2);
        }
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 2, false, false ,true));
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10 * 20, 1, false, false ,true));
        uses.put(player, uses.get(player) - 1);

        player.getPlayer().getLocation().getWorld().playSound(player.getPlayer().getLocation(), Sound.BLOCK_GLASS_BREAK, 3, 1);
        player.getPlayer().getInventory().setHeldItemSlot(4);
    }

    public static void handleGameStart() {
        uses.clear();
        for (CBPlayer player : GameManager.getTeam(Team.CRACK_BUSTER)) {
            uses.put(player, 2);
        }
    }

    public int getUses(CBPlayer player) {
        return uses.getOrDefault(player, 0);
    }

    @Override
    public void handleActionBarHover(PlayerItemHeldEvent event) {
        CBPlayer player = GameManager.getPlayer(event.getPlayer().getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                // run until player stops holding the radar
                if(!(ItemUtil.getCBItem(player.getPlayer().getInventory().getItemInMainHand()) instanceof Crack)) {
                    cancel();
                }
                int uses = getUses(player);
                if(uses == 1) {
                    player.sendActionBarMessage(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + "" + net.md_5.bungee.api.ChatColor.BOLD + "1 Use!");
                } else {
                    player.sendActionBarMessage(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + "" + net.md_5.bungee.api.ChatColor.BOLD + uses + "0 Uses!");
                }

            }
        }.runTaskTimer(CrackBusters.instance, 0L, 1L);
    }
}
