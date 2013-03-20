package net.betterverse.unclaimed.util;

import java.util.Random;
import net.betterverse.unclaimed.Unclaimed;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Class to find any unclaimed chunks in the background. Any unclaimed chunks 
 * found will be added to a list. The /unclaimed teleport command will grab a 
 * random chunk from that list to teleport to.
 * 
 * @author Ryan
 */
public class UnclaimedRunnable extends BukkitRunnable {
    private final Unclaimed unclaimed;
 
    public UnclaimedRunnable(Unclaimed plugin) {
        this.unclaimed = plugin;
    }

    @Override
    public void run() {
        Random random = new Random();
        int x;
        int z;
        Chunk chunk;
        int i = 0;
        Location tpLoc = null;
        do {
            i++;
            x = random.nextInt(unclaimed.getConfiguration().getMaxX() * 2) - unclaimed.getConfiguration().getMaxX();
            z = random.nextInt(unclaimed.getConfiguration().getMaxZ() * 2) - unclaimed.getConfiguration().getMaxZ();
            chunk = unclaimed.getServer().getWorld("world").getChunkAt(x, z);
        } while ((UnclaimedRegistry.isProtected(chunk) || (tpLoc = getLocationFor(chunk)) == null) && i < 8); //we don't need to do it as many times
        unclaimed.addChunk(chunk);
    }
    
    private Location getLocationFor(Chunk c) {
        // First we want to try and provide a random result.
        Location location = getRandomLocationFor(c, unclaimed.getConfiguration().getMinTeleportationY(), unclaimed.getConfiguration().getMaxTeleportationY());
        if (location != null) {
            return location;
        }
        // Since our random attempts have failed, now we systematically go through the chunk looking for a valid place to set the player.
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                location = getLocationFor(c, x, z, unclaimed.getConfiguration().getMinTeleportationY(), unclaimed.getConfiguration().getMaxTeleportationY());
                if (location != null) {
                    return location;
                }
            }
        }
        return null;
    }
    
    private Location getRandomLocationFor(Chunk c, int minY, int maxY) {
        Random rand = new Random();
        // Make five attempts to place them randomly.  If all five of these fail, then we return null.
        // Perhaps in the future this should/could/needs to be added to the config.
        for (int attempts = 0; attempts < 5; attempts++) {
            int x = rand.nextInt(16);
            int z = rand.nextInt(16);
            Location loc = getLocationFor(c, x, z, minY, maxY);
            if (loc != null) {
                return loc;
            }
        }
        return null;
    }

    private Location getLocationFor(Chunk c, int x, int z, int minY, int maxY) {
        // y is decremented by three because we need two blocks worth of space for the player to stand on.  
        for (int y = maxY; y >= minY; y-=3) {
            Block b = c.getBlock(x, y, z);
            if (b.getType().isSolid()) {
                return b.getLocation().add(0, 2, 0);
            }
        }
        return null;
    }
}
