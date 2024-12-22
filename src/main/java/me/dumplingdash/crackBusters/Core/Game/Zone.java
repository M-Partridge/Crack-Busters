package me.dumplingdash.crackBusters.Core.Game;

import org.bukkit.Material;

public enum Zone {
    GOLD(Material.GOLD_BLOCK, "Gold Zone"),
    EMERALD(Material.EMERALD_BLOCK, "Emerald Zone"),
    DIAMOND(Material.DIAMOND_BLOCK, "Diamond Zone"),
    NETHERITE(Material.NETHERITE_BLOCK, "Netherite Zone");

    private final Material material;
    private final String name;
    Zone(Material material, String name) {
        this.material = material;
        this.name = name;
    }
    public Material getMaterial() {
        return material;
    }
    public String getName() {
        return name;
    }

}
