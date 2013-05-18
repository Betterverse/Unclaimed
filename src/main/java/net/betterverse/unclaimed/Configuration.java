package net.betterverse.unclaimed;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration {
	private String buildMessage;

	private int maxX;

	private int maxZ;

	private int maxTeleportY;

	private int minTeleportY;

	private int teleportCooldown;

	private List<String> activeRegenerationWorlds;

	private int regenerationOffset;

	private Unclaimed instance;

	private YamlConfiguration config;

	public Configuration(Unclaimed instance) {
		this.instance = instance;
		reload();
		try {
			config.save(new File(instance.getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected final void reload() {
		config = (YamlConfiguration) instance.getConfig();
		buildMessage = config.getString("build-message");
		maxX = config.getInt("max-search-x");
		maxZ = config.getInt("max-search-z");
		teleportCooldown = config.getInt("teleport-cooldown");
		activeRegenerationWorlds = config.getStringList("regeneration-worlds");
		regenerationOffset = config.getInt("regeneration-interval");
		minTeleportY = config.getInt("teleportation.minY");
		maxTeleportY = config.getInt("teleportation.maxY");
		config.options().copyDefaults(true);
	}

	public String getBuildMessage() {
		return buildMessage;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxZ() {
		return maxZ;
	}

	public int getTeleportCooldown() {
		return teleportCooldown;
	}

	public List<String> getActiveRegenerationWorlds() {
		return activeRegenerationWorlds;
	}

	public int getRegenerationOffset() {
		return regenerationOffset;
	}

	public int getMaxTeleportationY() {
		if (this.maxTeleportY > 256) {
			maxTeleportY = 256;
		}
		return this.maxTeleportY;
	}

	public int getMinTeleportationY() {
		return minTeleportY;
	}
}
