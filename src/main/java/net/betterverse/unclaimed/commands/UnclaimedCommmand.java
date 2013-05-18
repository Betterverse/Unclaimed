package net.betterverse.unclaimed.commands;

import java.util.ArrayList;
import java.util.List;
import net.betterverse.unclaimed.Unclaimed;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.logging.Level;
import net.betterverse.unclaimed.util.UnclaimedRegistry;
import org.bukkit.Material;

public class UnclaimedCommmand implements CommandExecutor {
	private Unclaimed instance;

	//cache of recently found chunks. if a teleport request finds a chunk before it reaches its search limit,
	//it will continue until it hits the search limit or until the cache reaches a size of 20.
	//if a chunk is pulled from the cache on request, the search will not commence.
	//CURRENTLY NOT IMPLEMENTED. NOTE TO FUTURE DEVS:
	//IF THIS MESSAGE REMAINS AFTER 20/02/2013, FEEL FREE TO DELETE THE FOLLOWING LINE.
	// Deleted.  -evilmidget38
	public UnclaimedCommmand(Unclaimed instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if (sender instanceof Player) {
			if (args.length == 0) {
				//Boolean protection = getProtection(((Player) sender).getLocation());
				StringBuilder message = new StringBuilder("Your location is ");
				Player p = (Player) sender;
				if (!UnclaimedRegistry.isProtected(p.getLocation())) {
					message.append("not protected.");
				} else {
					message.append("protected by ");
					message.append(UnclaimedRegistry.getProtectedBy(p.getLocation()));
					message.append(".");
				}
				sender.sendMessage(message.toString());
				return true;
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					if (sender.hasPermission("unclaimed.reload")) {
						instance.reloadCustomConfig();
					} else {
						sender.sendMessage("You are not permitted.");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp")) {
					if (sender.hasPermission("unclaimed.teleport")) {
						teleport((Player) sender);
					} else {
						sender.sendMessage("You are not permitted.");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("regen")) {
					if (sender.hasPermission("unclaimed.admin.regen")) {
						Player p = (Player) sender;
						if (UnclaimedRegistry.isProtected(p.getLocation())) {
							sender.sendMessage("Cannot regen chunk, it is protected!");
						} else {
							p.getWorld().regenerateChunk(p.getLocation().getChunk().getX(), p.getLocation().getChunk().getZ());
						}
					} else {
						sender.sendMessage("You are not permitted.");
					}
					return true;
				}
				return false;
			}
		} else {
			if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				instance.reloadCustomConfig();
				return true;
			} else {
				sender.sendMessage(instance.getDescription().getPrefix() + "Console cannot do this.");
				return true;
			}
		}
		return true;
	}

	private void teleport(Player player) {
		if (!player.getWorld().getName().equalsIgnoreCase("world")) {
			player.sendMessage("This command is not available for the world " + player.getWorld().getName());
			return;
		}

		long remaining = UnclaimedCommandTeleportTask.getRemainingTime(player);
		if (remaining > 0) {
			player.sendMessage("You've teleported too recently! You can teleport again in " + (remaining / 1000) + " seconds.");
		} else {
			Location tpLoc = null;
			while (tpLoc == null) {
				tpLoc = instance.getUnclaimedLocation();
			}

			Bukkit.getScheduler().runTaskLater(instance, new UnclaimedCommandTeleportTask(player,
					System.currentTimeMillis() + (instance.getConfiguration().getTeleportCooldown() * 1000)),
					instance.getConfiguration().getTeleportCooldown() * 20);
			player.teleport(tpLoc);
			player.sendMessage("You've been teleported to an unclaimed area.");
		}
	}
}
