package me.dumplingdash.crackBusters.Core.Game;

import me.dumplingdash.crackBusters.Enums.Team;
import org.bukkit.entity.Player;

public class CBPlayer {
    private final Player player;
    private Team team;
    public CBPlayer(Player player) {
        this.player = player;
        setTeam(Team.SPECTATOR);
    }

    public void setTeam(Team newTeam) {
        // Update team
        team = newTeam;

        // Update Display Name
        String name = team.toString() + " " + player.getDisplayName();
        player.setPlayerListName(name);
        player.setDisplayName(name);
    }

    public Team getTeam() {
        return team;
    }
}
