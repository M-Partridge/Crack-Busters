package me.dumplingdash.crackBusters.Core.Game;

import me.dumplingdash.crackBusters.Enums.GameState;
import me.dumplingdash.crackBusters.Enums.Team;
import me.dumplingdash.crackBusters.Utility.ColorUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CBPlayer {
    private final Player player;
    private boolean isSneaking;
    private Team team;
    private Zone zone;
    public CBPlayer(Player player) {
        this.player = player;
        isSneaking = false;
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
    public void updateScoreboard() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("crackBusters", "dummy", ChatColor.BOLD + "Crack Busters");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        ChatColor notPlaced = ChatColor.RED;
        ChatColor hiddenColor = ChatColor.YELLOW;
        org.bukkit.ChatColor brokenColor = org.bukkit.ChatColor.BLUE;
        ChatColor foundColor = ChatColor.GREEN;

        HashMap<Material, HiddenBlock> hiddenBlocks = GameManager.getHiddenBlocks();
        int num = 0;
        for(Material block : hiddenBlocks.keySet()) {
            HiddenBlock hiddenBlock = hiddenBlocks.get(block);
            String string = hiddenBlock.getColor() + "" + ChatColor.BOLD + hiddenBlock.getName() + " Block: ";
            if(hiddenBlock.isFound()) {
                string += foundColor + "Found";
            } else if(hiddenBlock.isPlaced()) {
                string += hiddenColor + "Hidden";
            } else if(GameManager.getGameState() == GameState.BREAKING) {
                string += brokenColor + "Broken";
            } else {
                string += notPlaced + "Not Placed";
            }
            Score score = objective.getScore(string);
            score.setScore(num);
            ++num;
        }

        Score blank4 = objective.getScore(" ");
        blank4.setScore(4);

        Score gameState = objective.getScore(ChatColor.BOLD + "Game State: " + ChatColor.WHITE + GameManager.getGameState().getName());
        gameState.setScore(5);

        Score blank6 = objective.getScore("");
        blank6.setScore(6);

        player.getPlayer().setScoreboard(scoreboard);
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
