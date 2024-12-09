package me.dumplingdash.crackBusters.Utility;

import me.dumplingdash.crackBusters.CrackBusters;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleUtils {
    public static void SpawnCube(World world, double x1, double y1, double z1, double x2, double y2, double z2, double particleStep, int ticks) {
        double minX = Math.min(x1, x2);
        double maxX = Math.max(x1, x2);
        double minY = Math.min(y1, y2);
        double maxY = Math.max(y1, y2);
        double minZ = Math.min(z1, z2);
        double maxZ = Math.max(z1, z2);

        BukkitRunnable task = new BukkitRunnable() {
            int ticksPast = 0;
            @Override
            public void run() {
                ++ticksPast;
                for(double x = minX; x < maxX; ++x) {
                    for(double y = minY; y < maxY; ++y) {
                        for(double z = minZ; z < maxZ; ++z) {
                            world.spawnParticle(Particle.REDSTONE, x, y, z, 1);
                        }
                    }
                }

                if(ticksPast > ticks) this.cancel();
            }
        };

        task.runTaskTimer(CrackBusters.instance, 0L, 1L);


    }
}
