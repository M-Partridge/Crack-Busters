package me.dumplingdash.crackBusters.Commands;

import me.dumplingdash.crackBusters.Core.Game.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class StartGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        GameManager.tryStartGame();
        return true;
    }
}
