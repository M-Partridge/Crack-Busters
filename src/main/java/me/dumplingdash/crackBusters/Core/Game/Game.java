package me.dumplingdash.crackBusters.Core.Game;

import me.dumplingdash.crackBusters.Enums.GameState;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Game {

    private GameState gameState;
    private final HashMap<UUID, CBPlayer> players;
    private final ArrayList<CBPlayer> spectators;
    private final ArrayList<CBPlayer> hunters;
    private final ArrayList<CBPlayer> crackBusters;
    public Game() {
        players = new HashMap<>();
        spectators = new ArrayList<>();
        hunters = new ArrayList<>();
        crackBusters = new ArrayList<>();
        gameState = GameState.LOBBY;
    }

    public void handlePlayerJoin(PlayerJoinEvent event) {
        players.put(event.getPlayer().getUniqueId(), new CBPlayer(event.getPlayer()));
        spectators.add(players.get(event.getPlayer().getUniqueId()));
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    public GameState getGameState() {
        return gameState;
    }

    public ArrayList<CBPlayer> getSpectators() {
        return spectators;
    }

    public ArrayList<CBPlayer> getCrackBusters() {
        return crackBusters;
    }
    public ArrayList<CBPlayer> getHunters() {
        return hunters;
    }
}
