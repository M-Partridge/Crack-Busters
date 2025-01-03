package me.dumplingdash.crackBusters.Utility;

import me.dumplingdash.crackBusters.Core.CBRegistry;
import me.dumplingdash.crackBusters.Core.Keys;
import me.dumplingdash.crackBusters.Item.CBItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemUtil {
    public static CBItem getCBItem(ItemStack item) {
        if(item == null) return null;
        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            return null;
        }
        PersistentDataContainer data = meta.getPersistentDataContainer();
        if(!data.has(Keys.cbItem)) {
            return null;
        }
        return CBRegistry.cbItems.get(data.get(Keys.cbItem, PersistentDataType.STRING));
    }
}
