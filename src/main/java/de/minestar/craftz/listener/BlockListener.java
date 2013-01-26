package de.minestar.craftz.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import de.minestar.craftz.Core;
import de.minestar.craftz.data.SurvivalPlayer;
import de.minestar.craftz.manager.GameManager;

public class BlockListener implements Listener {

    private GameManager gameManager;

    public BlockListener() {
        this.gameManager = Core.gameManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        // get the player
        SurvivalPlayer sPlayer = this.gameManager.getPlayer(event.getPlayer().getName());
        if (sPlayer == null) {
            return;
        }

        // cancel event
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketFill(PlayerBucketFillEvent event) {
        // get the player
        SurvivalPlayer sPlayer = this.gameManager.getPlayer(event.getPlayer().getName());
        if (sPlayer == null) {
            return;
        }

        // cancel event
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        // get the player
        SurvivalPlayer sPlayer = this.gameManager.getPlayer(event.getPlayer().getName());
        if (sPlayer == null) {
            return;
        }

        // validate blockplace
        Block block = event.getBlockPlaced();
        if (sPlayer.getCurrentGame().getSettings().isPlaceable(block.getType()) && sPlayer.isPlayer()) {
            sPlayer.getCurrentGame().addBlockUpdate(event.getBlock().getLocation());
            return;
        }

        event.setBuild(false);
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        // get the player
        SurvivalPlayer sPlayer = this.gameManager.getPlayer(event.getPlayer().getName());
        if (sPlayer == null) {
            return;
        }

        // validate blockbreak
        Block block = event.getBlock();
        if (sPlayer.getCurrentGame().getSettings().isBreakable(block.getType()) && sPlayer.isPlayer()) {
            sPlayer.getCurrentGame().addBlockUpdate(event.getBlock().getLocation());
            return;
        }

        // cancel event
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent event) {
        if (!event.getSource().getType().equals(Material.RED_MUSHROOM) && !event.getSource().getType().equals(Material.BROWN_MUSHROOM)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        IgniteCause cause = event.getCause();
        if (cause.equals(IgniteCause.LAVA) || cause.equals(IgniteCause.SPREAD) || cause.equals(IgniteCause.LIGHTNING) || cause.equals(IgniteCause.FIREBALL)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }
}
