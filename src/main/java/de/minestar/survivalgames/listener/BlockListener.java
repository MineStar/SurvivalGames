package de.minestar.survivalgames.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.manager.GameManager;

public class BlockListener implements Listener {

    private GameManager gameManager;

    public BlockListener() {
        this.gameManager = Core.gameManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        // game must be running
        if (!this.gameManager.isInGame()) {
            return;
        }

        // only vines are allowed
        Block block = event.getBlockPlaced();
        if (!block.getType().equals(Material.VINE)) {
            event.setBuild(false);
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // game must be running
        if (!this.gameManager.isInGame()) {
            return;
        }

        // only vines, mushrooms, wheat and melons are allowed
        Block block = event.getBlock();
        if (block.getType().equals(Material.VINE) || block.getType().equals(Material.MELON) || block.getType().equals(Material.WHEAT) || block.getType().equals(Material.BROWN_MUSHROOM) || block.getType().equals(Material.RED_MUSHROOM)) {
            return;
        }

        // cancel event
        event.setCancelled(true);
    }
}
