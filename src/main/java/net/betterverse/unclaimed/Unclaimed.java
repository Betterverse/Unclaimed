package net.betterverse.unclaimed;

import net.betterverse.unclaimed.commands.UnclaimedCommmand;
import net.betterverse.unclaimed.util.RegenerationRunnable;
import net.betterverse.unclaimed.util.UnclaimedRegistry;
import net.betterverse.unclaimed.util.WorldguardWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class Unclaimed extends JavaPlugin {
	private Configuration configuration;

	private Unclaimer unclaimer;

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

		//new RegenerationRunnable(this, getConfiguration().getActiveRegenerationWorlds()).runTaskTimer(this, getConfiguration().getRegenerationOffset() * 20, getConfiguration().getRegenerationOffset() * 20);
		unclaimer = new Unclaimer(this);
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

	public Location getUnclaimedLocation() {
		return unclaimer.generateUnclaimedLocation();
	}
}
