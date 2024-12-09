package me.dumplingdash.crackBusters.Commands;

import me.dumplingdash.crackBusters.Core.Game.GameManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetSpawnCommand implements CommandExecutor, TabCompleter {
    private final String gameString = "game";
    private final String lobbyString = "lobby";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length != 1) {
            return false;
        }
        if(sender instanceof Player player) {
            Location location = player.getLocation();
            String spawn = args[0];
            if(spawn.equalsIgnoreCase(gameString)) {
                GameManager.setGameSpawn(location);
            } else if(spawn.equalsIgnoreCase(lobbyString)) {
                GameManager.setLobbySpawn(location);
            }
        } else {
            sender.sendMessage("Only players can run this command");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if(args.length == 1) {
            suggestions.add(gameString);
            suggestions.add(lobbyString);
        }

        String arg = args[args.length - 1];
        List<String> filtered = new ArrayList<>();
        for (String suggestion : suggestions) {
            if (suggestion.toLowerCase().startsWith(arg.toLowerCase())) {
                filtered.add(suggestion);
            }
        }

        return filtered;
    }
}
