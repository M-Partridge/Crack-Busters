package me.dumplingdash.crackBusters.Enums;

import me.dumplingdash.crackBusters.Utility.ColorUtils;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public enum Team {
    SPECTATOR("Spectator", new Color(156, 156, 156)),
    HUNTER("Hunter", new Color(158, 21, 0)),
    CRACK_BUSTER("Crack Buster", new Color(26, 98, 171));

    private final float darkenAmount = .75f; // Amount to darken [] on team name
    private final String name;
    private final Color color;

    Team(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public String toString() {
        String string = ChatColor.of(ColorUtils.darkenColor(color, darkenAmount)) + "[]";
        string = string.substring(0, 1) + ChatColor.of(color) + name + string.substring(1);
        return ColorUtils.colorizeText("#0#[#1#" + getName() + "#0#]", new ArrayList<>(Arrays.asList(ColorUtils.darkenColor(color, darkenAmount), color)));
    }
}
