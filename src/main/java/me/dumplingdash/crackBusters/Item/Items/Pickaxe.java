package me.dumplingdash.crackBusters.Item.Items;

import me.dumplingdash.crackBusters.Item.CBItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class Pickaxe extends CBItem {
    @Override
    public String getName() {
        return ChatColor.BOLD + "Buster";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                ChatColor.of(CBItem.loreColor) + "Efficiency ∞"
        );
    }

    @Override
    public ItemMeta modifyMeta(ItemMeta meta) {
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.DAMAGE_ARTHROPODS, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return meta;
    }

    @Override
    public ItemStack enchantItem(ItemStack item) {
        return item;
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_PICKAXE;
    }
}
