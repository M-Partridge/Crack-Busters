package me.dumplingdash.crackBusters.Core.Game;


import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.awt.*;

public class HiddenBlock {
    private boolean isPlaced;
    private boolean isFound;
    private Location location;
    private Location pedestalLocation;
    private final ChatColor color;
    private final String name;
    public HiddenBlock(Location pedestalLocation, ChatColor color, String name) {
        isPlaced = false;
        isFound = false;
        location = null;
        this.pedestalLocation = pedestalLocation;
        this.color = color;
        this.name = name;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    public boolean isFound() {
        return isFound;
    }

    public Location getLocation() {
        return location;
    }

    public Location getPedestalLocation() {
        return pedestalLocation;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public void setPlaced(boolean isPlaced) {
        this.isPlaced = isPlaced;
        GameManager.updateAllPlayerScoreboards();
    }

    public void setFound(boolean isFound) {
        this.isFound = isFound;
        GameManager.updateAllPlayerScoreboards();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPedestalLocation(Location location) {
        pedestalLocation = location;
        System.out.println("Setting Pedestal Location " + name + " " + location);
    }
}
