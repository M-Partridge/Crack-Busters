package me.dumplingdash.crackBusters.Core.Game;

import me.dumplingdash.crackBusters.Enums.GameState;
import me.dumplingdash.crackBusters.Enums.Team;
import me.dumplingdash.crackBusters.Utility.ColorUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
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
    private boolean isInvincible;
    private boolean isDead;
    private boolean rebootCollected;
    private Team team;
    private Zone zone;
    public CBPlayer(Player player) {
        this.player = player;
        isSneaking = false;
        isInvincible = false;
        isDead = false;
        rebootCollected = false;
        zone = null;
    }
    public void handleEndGame() {
        isInvincible = false;
        isDead = false;
        zone = null;
        rebootCollected = false;
        setTeam(Team.CRACK_BUSTER);
    }

    public void applyPotionEffect(PotionEffectType type, int duration, int power) {
        player.addPotionEffect(new PotionEffect(type, duration, power, false, false, false));
    }
    public void setSneaking(boolean sneaking) {
        this.isSneaking = sneaking;
    }
    public void setInvincible(boolean invincible) {
        isInvincible = invincible;
        if(!invincible) {
            player.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "You Are No Longer", ChatColor.RED + "" + ChatColor.BOLD + "Invincible", 0, 50, 10);
        }
    }
    public void setDead(boolean dead) {
        isDead = dead;
        if(dead) {
            String displayName = player.getDisplayName();
            displayName += " ☆";
        }
    }
    public void setTeam(Team newTeam) {
        player.sendMessage("setting team to " + newTeam.getName());
        // Update team
        team = newTeam;
        Team.HUNTER.getTeam().removeEntry(player.getName());
        Team.CRACK_BUSTER.getTeam().removeEntry(player.getName());
        Team.SPECTATOR.getTeam().removeEntry(player.getName());
        newTeam.getTeam().addEntry(player.getName());

        // Update Display Name
        setName();
    }
    private void setName() {
        String name = team.toString() + " " + player.getName();
        player.setPlayerListName(name);
        player.setDisplayName(name);
    }
    public void setZone(Zone newZone) {
        zone = newZone;
    }
    public void setRebootCollected(boolean collected) {
        rebootCollected = collected;
        if(rebootCollected == true) {
            String displayName = player.getDisplayName();
            displayName += " ★";
        }
    }
    public void updateScoreboard() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        Objective objective;

        if(scoreboard.getObjective("crackBusters") == null) {
            objective = scoreboard.registerNewObjective("crackBusters", "dummy", ChatColor.BOLD + "Crack Busters");
        } else {
            objective = scoreboard.getObjective("crackBusters");
        }

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

        Score blank1 = objective.getScore(" ");
        blank1.setScore(num);
        ++num;

        Score gameState = objective.getScore(ChatColor.BOLD + "Game State: " + ChatColor.WHITE + GameManager.getGameState().getName());
        gameState.setScore(num);
        ++num;

        Score blank2 = objective.getScore("");
        blank2.setScore(num);
        ++num;

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
    public boolean isInvincible() {
        return isInvincible;
    }
    public boolean isDead() {
        return isDead;
    }
    public Zone getZone() {
        return zone;
    }
    public void sendErrorMessage(String error) {
        player.sendMessage(ColorUtils.colorizeText(error, new ArrayList<>(Arrays.asList(new Color(255, 0, 0)))));
    }
    public void sendActionBarMessage(String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}
