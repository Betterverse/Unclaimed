package net.betterverse.unclaimed;

import net.betterverse.unclaimed.commands.UnclaimedCommmand;
import net.betterverse.unclaimed.util.UnclaimedRegistry;
import net.betterverse.unclaimed.util.WorldguardWrapper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class Unclaimed extends JavaPlugin {
    
    private Configuration configuration;

    @Override
    public void onEnable() {
        configuration = new Configuration(this);
        Bukkit.getServer().getPluginCommand("unclaimed").setExecutor(new UnclaimedCommmand(this));
        Bukkit.getLogger().info("Loaded " + this.getDescription().getVersion());
        if (getConfiguration().hasWorldguard()) {
            try {
                WorldguardWrapper worldguardWrapper = new WorldguardWrapper();
                UnclaimedRegistry.registerClass(worldguardWrapper);
            } catch (ClassNotFoundException e) {
                Bukkit.getLogger().warning("WorldGuard wrapping enabled in configuration, but WorldGuard not found");
            }
        }

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
}
