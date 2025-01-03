package me.dumplingdash.crackBusters.Enums;

import me.dumplingdash.crackBusters.Utility.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public enum Team {
    SPECTATOR("Spectator", ChatColor.GRAY, org.bukkit.scoreboard.Team.OptionStatus.NEVER),
    HUNTER("Hunter", ChatColor.RED, org.bukkit.scoreboard.Team.OptionStatus.ALWAYS),
    CRACK_BUSTER("Crack Buster", ChatColor.AQUA, org.bukkit.scoreboard.Team.OptionStatus.FOR_OTHER_TEAMS);

    private final float darkenAmount = .75f; // Amount to darken [] on team name
    private final String name;
    private final org.bukkit.ChatColor color;
    private final org.bukkit.scoreboard.Team team;

    Team(String name, ChatColor color, org.bukkit.scoreboard.Team.OptionStatus nametags) {
        this.name = name;
        this.color = color;
        if(Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name) == null) {
            team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(name);
        } else {
            team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name);
        }
        team.setColor(color);
        team.setOption(org.bukkit.scoreboard.Team.Option.NAME_TAG_VISIBILITY, nametags);
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color.asBungee().getColor();
    }

    public String getColoredName() {
        return ColorUtils.colorizeText("#0#" + getName(), new ArrayList<>(Collections.singletonList(getColor())));
    }
    public String toString() {
        Color darkened = ColorUtils.darkenColor(color.asBungee().getColor(), darkenAmount);
        return net.md_5.bungee.api.ChatColor.of(darkened) + "[" + color + getName() + net.md_5.bungee.api.ChatColor.of(darkened) + "]" + ChatColor.WHITE;
    }
    public org.bukkit.scoreboard.Team getTeam() {
        return team;
    }

    public static void unregisterTeams() {
        for (org.bukkit.scoreboard.Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
            team.unregister();
        }
    }
}
