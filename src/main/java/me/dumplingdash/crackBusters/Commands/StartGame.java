package me.dumplingdash.crackBusters.Commands;

import me.dumplingdash.crackBusters.Core.Game.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartGame implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        GameManager.tryStartGame();

        return true;
    }
}