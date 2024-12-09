package me.dumplingdash.crackBusters.Commands;

import me.dumplingdash.crackBusters.Core.Game.GameManager;
import me.dumplingdash.crackBusters.Enums.Team;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JoinTeamCommand implements CommandExecutor, TabCompleter {

    private final String huntersString = "hunters";
    private final String crackBustersString = "crackbusters";
    private final String spectatorString = "spectators";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length != 1) {
            return false;
        }
        if(sender instanceof Player player) {
            String team = args[0];
            if(team.equalsIgnoreCase(huntersString)) {
                GameManager.setPlayerTeam(player, Team.HUNTER);
            } else if(team.equalsIgnoreCase(crackBustersString)) {
                GameManager.setPlayerTeam(player, Team.CRACK_BUSTER);
            } else if(team.equalsIgnoreCase(spectatorString)) {
                GameManager.setPlayerTeam(player, Team.SPECTATOR);
            } else {
                return false;
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
            suggestions.add(huntersString);
            suggestions.add(crackBustersString);
            suggestions.add(spectatorString);
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
