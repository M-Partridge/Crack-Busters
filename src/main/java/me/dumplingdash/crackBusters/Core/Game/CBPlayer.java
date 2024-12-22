package me.dumplingdash.crackBusters.Core.Game;

import me.dumplingdash.crackBusters.Enums.Team;
import me.dumplingdash.crackBusters.Utility.ColorUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CBPlayer {
    private final Player player;
    private boolean isSneaking;
    private Team team;
    private Zone zone;
    public CBPlayer(Player player) {
        this.player = player;
        isSneaking = false;
        setTeam(Team.SPECTATOR);
        zone = null;
    }

    public void applyPotionEffect(PotionEffectType type, int duration, int power) {
        player.addPotionEffect(new PotionEffect(type, duration, power, false, false, false));
    }
    public void setSneaking(boolean sneaking) {
        this.isSneaking = sneaking;
    }
    public void setTeam(Team newTeam) {
        player.sendMessage("setting team to " + newTeam.getName());
        // Update team
        team = newTeam;

        // Update Display Name
        String name = team.toString() + " " + player.getName();
        player.setPlayerListName(name);
        player.setDisplayName(name);
    }
    public void setZone(Zone newZone) {
        zone = newZone;
    }

    public Team getTeam() {
        return team;
    }
    public Player getPlayer() {
        return player;
    }
    public boolean isSneaking() {
        return isSneaking;
    }
    public Zone getZone() {
        return zone;
    }
    public void sendErrorMessage(String error) {
        player.sendMessage(ColorUtils.colorizeText(error, new ArrayList<>(Arrays.asList(new Color(255, 0, 0)))));
    }
}
