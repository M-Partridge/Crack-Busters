package me.dumplingdash.crackBusters.Enums;

public enum GameState {
    LOBBY("Lobby"),
    BREAKING("Breaking"),
    PLACING("Placing");

    private String name;
    GameState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
