package me.dumplingdash.crackBusters.Item.Items;

import me.dumplingdash.crackBusters.Item.CBItem;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class Pickaxe extends CBItem {
    @Override
    public String getName() {
        return "Pickaxe";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                "This awesome pickaxe insta breaks hidden blocks"
        );
    }

    @Override
    public ItemMeta modifyMeta(ItemMeta meta) {
        meta.setUnbreakable(true);
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
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
