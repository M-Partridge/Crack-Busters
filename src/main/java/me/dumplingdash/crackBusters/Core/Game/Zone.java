package me.dumplingdash.crackBusters.Core.Game;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum Zone {
    GOLD(Material.GOLD_BLOCK, "Gold Zone", ChatColor.GOLD),
    EMERALD(Material.EMERALD_BLOCK, "Emerald Zone", ChatColor.GREEN),
    DIAMOND(Material.DIAMOND_BLOCK, "Diamond Zone", ChatColor.AQUA),
    NETHERITE(Material.NETHERITE_BLOCK, "Netherite Zone", ChatColor.DARK_GRAY);

    private final Material material;
    private final String name;
    private final ChatColor color;
    Zone(Material material, String name, ChatColor color) {
        this.material = material;
        this.name = name;
        this.color = color;
    }
    public Material getMaterial() {
        return material;
    }
    public String getName() {
        return name;
    }
    public ChatColor getColor() {
        return color;
    }

}
