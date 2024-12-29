package me.dumplingdash.crackBusters.Item;

import me.dumplingdash.crackBusters.Core.Keys;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class CBItem {
    public static final Color loreColor = new Color(92, 92, 92);
    public abstract String getName();
    public abstract List<String> getLore();
    public abstract ItemMeta modifyMeta(ItemMeta meta);
    public abstract ItemStack enchantItem(ItemStack item);
    public abstract Material getMaterial();
    public ItemStack getItem(int amount) {
        ItemStack item = new ItemStack(getMaterial(), amount);
        item = enchantItem(item);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            return item;
        }
        meta = modifyMeta(meta);
        meta.setDisplayName(getName());
        meta.setLore(getLore());
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(Keys.cbItem, PersistentDataType.STRING, getID());
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack getItem() {
        return getItem(1);
    }
    public String getID() {
        return getClass().getSimpleName().toLowerCase(Locale.ROOT);
    }
}
