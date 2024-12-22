package me.dumplingdash.crackBusters.Utility;

import org.bukkit.*;

import java.awt.Color;

public class ParticleUtils {
    public static void spawnCube(Location corner1, Location corner2, double particleStep, Color color) {
        if(corner1 == null || corner2 == null) {
            return;
        }
        double minX = Math.min(corner1.getX(), corner2.getX());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        Location minCorner = new Location(corner1.getWorld(), minX, minY, minZ);
        Location maxCorner = new Location(corner1.getWorld(), maxX, maxY, maxZ);

        Particle.DustOptions dustOptions = new Particle.DustOptions(org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getRed()), 1f);
        corner1.getWorld().spawnParticle(Particle.REDSTONE, minCorner, 1, dustOptions);
        corner1.getWorld().spawnParticle(Particle.REDSTONE, maxCorner, 1, dustOptions);
    }
}
