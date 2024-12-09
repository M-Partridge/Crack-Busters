package me.dumplingdash.crackBusters.Core.Game;


import org.bukkit.Location;

import java.awt.*;

public class HiddenBlock {
    private boolean isPlaced;
    private boolean isFound;
    private Location location;
    private Location pedestalLocation;
    private final Color color;
    public HiddenBlock(Location pedestalLocation, Color color) {
        isPlaced = false;
        isFound = false;
        location = null;
        this.pedestalLocation = pedestalLocation;
        this.color = color;
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

    public Color getColor() {
        return color;
    }

    public void setPlaced(boolean isPlaced) {
        this.isPlaced = isPlaced;
    }

    public void setFound(boolean isFound) {
        this.isFound = isFound;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPedestalLocation(Location location) {
        pedestalLocation = location;
    }
}
