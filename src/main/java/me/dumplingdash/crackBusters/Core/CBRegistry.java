package me.dumplingdash.crackBusters.Core;

import me.dumplingdash.crackBusters.Commands.JoinTeamCommand;
import me.dumplingdash.crackBusters.Commands.SetSpawnCommand;
import me.dumplingdash.crackBusters.Commands.StartGameCommand;
import me.dumplingdash.crackBusters.CrackBusters;
import me.dumplingdash.crackBusters.Events.Spigot.BlockBreakListener;
import me.dumplingdash.crackBusters.Events.Spigot.BlockDamageListener;
import me.dumplingdash.crackBusters.Events.Spigot.BlockPlaceListener;
import me.dumplingdash.crackBusters.Events.Spigot.PlayerJoinListener;
import me.dumplingdash.crackBusters.Item.CBItem;
import me.dumplingdash.crackBusters.Item.Items.Pickaxe;
import me.dumplingdash.crackBusters.Item.Items.Sword;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class CBRegistry {
    public final static HashMap<String, CBItem> cbItems = new HashMap<>();

    public static void enable() {
        registerListeners();
        registerCommands();
        registerItems();
    }
    public static void registerListeners() {
        Arrays.asList(
                new PlayerJoinListener(),
                new BlockBreakListener(),
                new BlockDamageListener(),
                new BlockPlaceListener()
        ).forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, CrackBusters.instance));
    }
    public static void registerCommands() {
        Arrays.asList(
                new AbstractMap.SimpleEntry<>("jointeam", new JoinTeamCommand()),
                new AbstractMap.SimpleEntry<>("startgame", new StartGameCommand()),
                new AbstractMap.SimpleEntry<>("setspawn", new SetSpawnCommand())
        ).forEach(command -> {
            if(command.getValue() != null && command.getKey() != null) {
                PluginCommand pluginCommand = CrackBusters.instance.getCommand(command.getKey());
                if(pluginCommand != null) {
                    pluginCommand.setExecutor(command.getValue());
                    if(command.getValue() instanceof TabCompleter) {
                        pluginCommand.setTabCompleter((TabCompleter) command.getValue());
                    }
                }
            }

        });
    }
    public static void registerItems() {
        Arrays.asList(
                new Sword(),
                new Pickaxe()
        ).forEach(item -> {
            cbItems.put(item.getID(), item);
        });
    }

}
