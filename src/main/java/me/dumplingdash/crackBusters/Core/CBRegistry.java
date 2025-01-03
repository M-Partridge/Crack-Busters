package me.dumplingdash.crackBusters.Core;

import me.dumplingdash.crackBusters.Commands.*;
import me.dumplingdash.crackBusters.Core.Game.GameManager;
import me.dumplingdash.crackBusters.CrackBusters;
import me.dumplingdash.crackBusters.Event.Listener.PlayerInteractListener;
import me.dumplingdash.crackBusters.Event.Listener.PlayerItemHeldListener;
import me.dumplingdash.crackBusters.Event.Listener.ToggleSneakListener;
import me.dumplingdash.crackBusters.Item.CBItem;
import me.dumplingdash.crackBusters.Item.Items.*;
import org.bukkit.Bukkit;
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
                new GameManager(),
                new ToggleSneakListener(),
                new PlayerInteractListener(),
                new PlayerItemHeldListener()
        ).forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, CrackBusters.instance));
    }
    public static void registerCommands() {
        Arrays.asList(
                new AbstractMap.SimpleEntry<>("endgame", new EndGameCommand()),
                new AbstractMap.SimpleEntry<>("giveitem", new GiveItemCommand()),
                new AbstractMap.SimpleEntry<>("jointeam", new JoinTeamCommand()),
                new AbstractMap.SimpleEntry<>("startgame", new StartGameCommand()),
                new AbstractMap.SimpleEntry<>("setspawn", new SetSpawnCommand()),
                new AbstractMap.SimpleEntry<>("setzone", new SetZoneCommand())
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
                new Crack(),
                new Sniffer(),
                new Sword(),
                new PedestalTool(),
                new Pickaxe(),
                new Radar(),
                new ZoneTool(),
                new ZoneAbility()
        ).forEach(item -> {
            cbItems.put(item.getID(),  item);
        });
    }

}
