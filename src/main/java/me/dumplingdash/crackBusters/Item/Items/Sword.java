package me.dumplingdash.crackBusters.Item.Items;

import me.dumplingdash.crackBusters.Item.CBItem;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sword extends CBItem {

    @Override
    public String getName() {
        return "Sword";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                "test lore 1",
                "test lore 2"
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
        return Material.NETHERITE_SWORD;
    }
}
