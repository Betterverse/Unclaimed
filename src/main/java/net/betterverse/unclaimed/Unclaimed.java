package net.betterverse.unclaimed;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.betterverse.unclaimed.commands.UnclaimedCommmand;
import net.betterverse.unclaimed.util.RegenerationRunnable;
import net.betterverse.unclaimed.util.UnclaimedRegistry;
import net.betterverse.unclaimed.util.UnclaimedRunnable;
import net.betterverse.unclaimed.util.WorldguardWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Unclaimed extends JavaPlugin {

    private Configuration configuration;
    private List<Chunk> unclaimedChunks = new ArrayList<Chunk>();


    @Override
    public void onEnable() {
        configuration = new Configuration(this);
        Bukkit.getServer().getPluginCommand("unclaimed").setExecutor(new UnclaimedCommmand(this));
        Bukkit.getLogger().info("Loaded " + this.getDescription().getVersion());
        try {
            WorldguardWrapper worldguardWrapper = new WorldguardWrapper();
            UnclaimedRegistry.registerClass(worldguardWrapper);
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().warning("WorldGuard not found");
        }
        Bukkit.getPluginManager().registerEvents(new Listener(this), this);
        new RegenerationRunnable(this, getConfiguration().getActiveRegenerationWorlds()).runTaskTimer(this, getConfiguration().getRegenerationOffset() * 60 * 20, getConfiguration().getRegenerationOffset() * 3600 * 20);

        //start the unclaimed chunk finder and run it every 5 seconds (100 ticks)
        BukkitTask task = new UnclaimedRunnable(this).runTaskTimer(this, 100, 100);
        
        //new RegenerationRunnable(this, getConfiguration().getActiveRegenerationWorlds()).runTaskTimer(this, getConfiguration().getRegenerationOffset() * 20, getConfiguration().getRegenerationOffset() * 20);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Reloads configuration file and {@link Configuration} class
     */
    public void reloadCustomConfig() {
        reloadConfig();
        configuration.reload();
    }
    
    public Chunk getUnclaimedChunk() {
        Chunk c = null;
        int i = unclaimedChunks.size(); //how many chunks are in the list
        Random random = new Random();
        int chunkID = random.nextInt(i); //randomly get a chunk
        c = (Chunk) unclaimedChunks.get(chunkID); 
        unclaimedChunks.remove(chunkID); //remove the chunk from the list
        return c;
    }
    
    public void addChunk(Chunk chunk) {
        unclaimedChunks.add(chunk);
    }
}
