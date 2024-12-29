package me.dumplingdash.crackBusters.Enums;

import me.dumplingdash.crackBusters.Utility.ColorUtils;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public enum Team {
    SPECTATOR("Spectator", new Color(156, 156, 156)),
    HUNTER("Hunter", new Color(158, 21, 0)),
    CRACK_BUSTER("Crack Buster", new Color(70, 150, 220));

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

    public String getColoredName() {
        return ColorUtils.colorizeText("#0#" + getName(), new ArrayList<>(Collections.singletonList(getColor())));
    }

    public String toString() {
        return ColorUtils.colorizeText("#0#[#1#" + getName() + "#0#]", new ArrayList<>(Arrays.asList(ColorUtils.darkenColor(color, darkenAmount), color)));
    }
}
