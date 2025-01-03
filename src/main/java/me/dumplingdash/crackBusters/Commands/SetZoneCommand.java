package me.dumplingdash.crackBusters.Commands;

import me.dumplingdash.crackBusters.Core.Game.GameManager;
import me.dumplingdash.crackBusters.Core.Game.Zone;
import me.dumplingdash.crackBusters.Enums.GameState;
import me.dumplingdash.crackBusters.Utility.CommonUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SetZoneCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof BlockCommandSender commandBlock)) {
            return true;
        }
        if(GameManager.getGameState() != GameState.BREAKING) {
            return true;
        }
        if(args.length != 2) {
            return false;
        }
        Zone zone = null;
        try {
            zone = Zone.valueOf(args[1].toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
        }
        Player player;
        if(args[0].equalsIgnoreCase("@p")) {
            player = CommonUtil.findNearestPlayer(commandBlock.getBlock().getLocation());
        } else {
            player = Bukkit.getPlayer(args[0]);
        }


        if(player == null || zone == null) {
            return false;
        }

        GameManager.getPlayer(player.getUniqueId()).setZone(zone);
        GameManager.getPlayer(player.getUniqueId()).updateScoreboard();

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> players = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()) {
            players.add(player.getName());
        }

        List<String> zones = new ArrayList<>();
        for(Zone zone : Zone.values()) {
            zones.add(zone.getName());
        }

        if(args.length == 1) {
            return players.stream().filter(player -> player.toUpperCase(Locale.ROOT).startsWith(args[0].toUpperCase(Locale.ROOT))).collect(Collectors.toList());
        }

        if(args.length == 2) {
            return zones.stream().filter(zone -> zone.toUpperCase(Locale.ROOT).startsWith(args[1].toUpperCase(Locale.ROOT))).collect(Collectors.toList());
        }

        return null;
    }
}
