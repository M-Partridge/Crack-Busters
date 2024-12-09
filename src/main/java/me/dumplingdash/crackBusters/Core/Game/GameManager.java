package me.dumplingdash.crackBusters.Core.Game;

import me.dumplingdash.crackBusters.Config.CBConfig;
import me.dumplingdash.crackBusters.Config.ConfigPaths;
import me.dumplingdash.crackBusters.CrackBusters;
import me.dumplingdash.crackBusters.Enums.GameState;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerJoinEvent;

public class GameManager {
    private static Game game;
    private static Location lobbySpawn;
    private static Location gameSpawn;

    public static void enable() {
        game = new Game();
        lobbySpawn = (Location) CBConfig.loadLocation(ConfigPaths.lobbySpawn);
        gameSpawn = (Location) CBConfig.loadLocation(ConfigPaths.gameSpawn);
    }

    public static void tryStartGame() {
        if(game.getGameState() != GameState.LOBBY) {
            CrackBusters.logMessage("Could not start game because game has already started");
            return;
        }
        if(game.getHunters().isEmpty() || game.getCrackBusters().isEmpty()) {
            CrackBusters.logMessage("Could not start game because one or both teams don't have at least 1 player");
            return;
        }
        if(lobbySpawn == null) {
            CrackBusters.logMessage("Could not start game because no lobby spawn has been set");
            return;
        }
        if(gameSpawn == null) {
            CrackBusters.logMessage("Could not start game because no game spawn has been set");
            return;
        }

        startGame();
    }

    private static void startGame() {
        // Set game state
        // give players items
        // teleport players to respective spawns
        // once all blocks are placed teleport players again
    }
    public static void handlePlayerJoin(PlayerJoinEvent event) {
        game.handlePlayerJoin(event);
    }
}
