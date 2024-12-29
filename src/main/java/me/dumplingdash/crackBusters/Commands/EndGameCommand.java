package me.dumplingdash.crackBusters.Commands;

import me.dumplingdash.crackBusters.Core.Game.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player player) {
            GameManager.endGame(player.getWorld(), null);
        }

        return true;
    }
}
