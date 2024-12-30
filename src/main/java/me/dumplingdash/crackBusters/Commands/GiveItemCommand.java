package me.dumplingdash.crackBusters.Commands;

import me.dumplingdash.crackBusters.Core.CBRegistry;
import me.dumplingdash.crackBusters.Item.CBItem;
import me.dumplingdash.crackBusters.Utility.ItemUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GiveItemCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length != 1) {
            return false;
        }
        if(sender instanceof Player player) {
            CBItem item = CBRegistry.cbItems.get(args[0]);
            if(item != null) {
                player.getInventory().addItem(item.getItem());
            }
        }

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> items = CBRegistry.cbItems.keySet().stream().toList();

        if(args.length == 1) {
            return items.stream().filter(item -> item.toUpperCase(Locale.ROOT).startsWith(args[0].toUpperCase(Locale.ROOT))).collect(Collectors.toList());
        }
        return null;
    }
}
