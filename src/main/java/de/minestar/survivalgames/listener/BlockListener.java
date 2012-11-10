package de.minestar.survivalgames.listener;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.manager.GameManager;

public class BlockListener implements Listener {

    public static HashSet<Integer> breakableBlocks = new HashSet<Integer>(Arrays.asList(Material.VINE.getId(), Material.MELON.getId(), Material.WHEAT.getId(), Material.BROWN_MUSHROOM.getId(), Material.RED_MUSHROOM.getId(), Material.SUGAR_CANE_BLOCK.getId()));
    public static HashSet<Integer> placeableBlocks = new HashSet<Integer>(Arrays.asList(Material.VINE.getId(), Material.CAKE_BLOCK.getId(), Material.CAKE.getId()));

    private GameManager gameManager;

    public BlockListener() {
        this.gameManager = Core.gameManager;
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        // game must be running
        if (!this.gameManager.isInGame()) {
            return;
        }

        // cancel event
        event.setCancelled(true);
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        // game must be running
        if (!this.gameManager.isInGame()) {
            return;
        }

        // cancel event
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        // game must be running
        if (!this.gameManager.isInGame()) {
            return;
        }

        // validate blockplace
        Block block = event.getBlockPlaced();
        if (placeableBlocks.contains(block.getTypeId()) && !Core.playerManager.isPlayer(event.getPlayer().getName())) {
            return;
        }

        event.setBuild(false);
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // game must be running
        if (!this.gameManager.isInGame()) {
            return;
        }

        // validate blockbreak
        Block block = event.getBlock();
        if (BlockListener.breakableBlocks.contains(block.getTypeId()) && !Core.playerManager.isPlayer(event.getPlayer().getName())) {
            return;
        }

        // cancel event
        event.setCancelled(true);
    }
}
