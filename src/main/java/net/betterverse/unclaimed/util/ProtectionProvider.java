package net.betterverse.unclaimed.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * An interface defining a protection provider for Unclaimed
 */
public interface ProtectionProvider {

    /**
     * @param chunk {@link org.bukkit.Chunk} to check
     * @return a description of why this {@link org.bukkit.Chunk} is protected, or {@code null}
     */
    public String getMessage(Chunk chunk);

    /**
     * @param location {@link org.bukkit.Location} to check
     * @return a description of why this {@link org.bukkit.Location} is protected, or {@code null}
     */
    public String getMessage(Location location);

    /**
     * @return the name of this protection provider
     */
    public String getName();

    /**
     * @param chunk {@link org.bukkit.Chunk} to check
     * @return {@code true} if the chunk contains or is contained by any protection(s), otherwise {@code false}
     */
    public boolean isProtected(Chunk chunk);

    /**
     * @param location {@link org.bukkit.Location} to check
     * @return {@code true} if the location contains or is contained by any protection(s), otherwise {@code false}
     */
    public boolean isProtected(Location location);

    public boolean isProtectedFrom(Player player, Location location);
}
