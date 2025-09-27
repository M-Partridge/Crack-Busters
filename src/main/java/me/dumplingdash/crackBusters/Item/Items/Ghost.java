package me.dumplingdash.crackBusters.Item.Items;

import me.dumplingdash.crackBusters.Core.Game.CBPlayer;
import me.dumplingdash.crackBusters.Core.Game.GameManager;
import me.dumplingdash.crackBusters.Enums.Team;
import me.dumplingdash.crackBusters.Item.CBItem;
import me.dumplingdash.crackBusters.Item.InventoryClick;
import me.dumplingdash.crackBusters.Item.RightClickAbility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Ghost extends CBItem implements RightClickAbility, InventoryClick {
    @Override
    public String getName() {
        return ChatColor.GRAY + "" + ChatColor.BOLD + "Ghost";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(net.md_5.bungee.api.ChatColor.of(CBItem.loreColor) + "Right Click to open a menu and teleport",
                net.md_5.bungee.api.ChatColor.of(CBItem.loreColor) + "to an alive crack buster"
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
        return Material.GRAY_DYE;
    }

    @Override
    public void handleRightClick(PlayerInteractEvent event) {
        CBPlayer player = GameManager.getPlayer(event.getPlayer().getUniqueId());
        if(!player.isDead()) return;
        if(player.getTeam() != Team.CRACK_BUSTER) return;
        int size = GameManager.getTeam(Team.CRACK_BUSTER).size();
        size += 9 - size % 9;

        Inventory inventory = Bukkit.createInventory(null, size, "Ghost");
        int count = 0;
        for (CBPlayer cbPlayer : GameManager.getTeam(Team.CRACK_BUSTER)) {
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
            if(meta != null) {
                meta.setOwnerProfile(Bukkit.getServer().createPlayerProfile(cbPlayer.getPlayer().getUniqueId(), cbPlayer.getPlayer().getName()));
                playerHead.setItemMeta(meta);
                inventory.setItem(count, playerHead);
            }
        }
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if(!item.hasItemMeta()) {
            event.setCancelled(true);
            return;
        }
        if(item.getItemMeta() instanceof SkullMeta skullMeta) {
            UUID uuid = skullMeta.getOwnerProfile().getUniqueId();
            CBPlayer player = GameManager.getPlayer(uuid);
            if(!player.isDead()) {
                event.getWhoClicked().teleport(player.getPlayer().getLocation());
            }
        }
    }
}
