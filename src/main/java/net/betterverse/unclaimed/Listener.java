package net.betterverse.unclaimed;

import net.betterverse.unclaimed.commands.UnclaimedCommandTeleportTask;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class Listener implements org.bukkit.event.Listener {
	private Unclaimed instance;

	public Listener(Unclaimed instance) {
		this.instance = instance;
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(checkProtection(event.getPlayer(), event.getBlock().getLocation()));
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		event.setCancelled(checkProtection(event.getPlayer(), event.getBlock().getLocation()));
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerBucket(PlayerBucketEmptyEvent event) {
		event.setCancelled(checkProtection(event.getPlayer(), event.getBlockClicked().getLocation()));
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerBucket(PlayerBucketFillEvent event) {
		event.setCancelled(checkProtection(event.getPlayer(), event.getBlockClicked().getLocation()));
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		UnclaimedCommandTeleportTask.reset(event.getEntity());
	}

	@EventHandler(ignoreCancelled = true)
	public void onHangingBreakByEntity(HangingBreakByEntityEvent e) {
		if (e.getRemover() instanceof Player) {
			e.setCancelled(checkProtection((Player) e.getRemover(), e.getEntity().getLocation()));
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onHangingPlace(HangingPlaceEvent e) {
		e.setCancelled(checkProtection(e.getPlayer(), e.getEntity().getLocation()));
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityEnterPortal(EntityPortalEvent e){
		if(e.getEntityType() == EntityType.PLAYER) {
			e.setCancelled(checkProtection((Player) e.getEntity(), e.getTo()));
		}
		
		if(e.isCancelled())
		{
			((Player) e.getEntity()).sendMessage(ChatColor.RED + "There is no valid portal on the other side");
		}
	}
	
	public boolean checkProtection(Player player, Location location) {
		if (!player.hasPermission("unclaimed.build")) {
			player.sendMessage(instance.getDescription().getPrefix() + " " + instance.getConfiguration().getBuildMessage());
			return true;
		}
		return false;

		//return UnclaimedRegistry.isProtectedFrom(player, location);
	}
}
