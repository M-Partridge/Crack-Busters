package me.dumplingdash.crackBusters.Item.Items;

import me.dumplingdash.crackBusters.Core.Game.CBPlayer;
import me.dumplingdash.crackBusters.Core.Game.GameManager;
import me.dumplingdash.crackBusters.Core.Game.Zone;
import me.dumplingdash.crackBusters.Enums.GameState;
import me.dumplingdash.crackBusters.Enums.Team;
import me.dumplingdash.crackBusters.Item.CBItem;
import me.dumplingdash.crackBusters.Item.RightClickAbility;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Radar extends CBItem implements RightClickAbility {

    private static final HashMap<CBPlayer, Long> cooldown = new HashMap<>();
    public static final int cooldownTime = 60; // seconds
    @Override
    public String getName() {
        return ChatColor.of(new Color(247, 220, 40)) + "" + ChatColor.BOLD + "Radar";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                ChatColor.of(CBItem.loreColor) + "Right click to see highlight",
                ChatColor.of(CBItem.loreColor) + "the player closest to you for",
                ChatColor.of(CBItem.loreColor) + "2 seconds"
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
        return Material.SPECTRAL_ARROW;
    }

    @Override
    public void handleRightClick(PlayerInteractEvent event) {
        CBPlayer player = GameManager.getPlayer(event.getPlayer().getUniqueId());
        if(getCooldownTime(player) != 0) {
            player.getPlayer().sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Radar: " + ChatColor.RESET + "on cooldown for " + getCooldownTime(player) + " ms");
            return;
        }
        if(GameManager.getGameState() != GameState.BREAKING || player.getTeam() != Team.HUNTER) {
            return;
        }
        Zone zone = player.getZone();
        double distance = -1;
        CBPlayer closestPlayer = null;
        for(CBPlayer cb : GameManager.getTeam(Team.CRACK_BUSTER)) {
            if(cb.getZone() == zone && zone != null) {
                if(cb.getPlayer().getLocation().distance(player.getPlayer().getLocation()) < distance || distance == -1) {
                    distance = cb.getPlayer().getLocation().distance(player.getPlayer().getLocation());
                    closestPlayer = cb;
                }
            }
        }
        if(closestPlayer == null) {
            player.getPlayer().sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Radar: " + ChatColor.RESET + "No players in your zone");
        } else if(distance > 64) {
            player.getPlayer().sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Radar: " + ChatColor.RESET + "No players within 64 blocks");
        } else {
            closestPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 2 * 20, 0, false, false, false));
            cooldown.put(player, System.currentTimeMillis());
            Vector direction = new Vector((float) closestPlayer.getPlayer().getLocation().getX() - player.getPlayer().getLocation().getX(),
                    (float) closestPlayer.getPlayer().getLocation().getY() - player.getPlayer().getLocation().getY(),
                    (float) closestPlayer.getPlayer().getLocation().getZ() - player.getPlayer().getLocation().getZ());
            Location location = player.getPlayer().getLocation();
            location.setDirection(direction);
            player.getPlayer().teleport(location);
            closestPlayer.getPlayer().sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "LOCATION REVEALED", "", 0, 40, 0);
        }
    }

    public long getCooldownTime(CBPlayer player) {
        long lastUse = cooldown.getOrDefault(player, 0L);
        return Math.max((lastUse + Radar.cooldownTime * 1000) - System.currentTimeMillis(), 0);
    }
}
