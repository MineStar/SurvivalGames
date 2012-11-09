package de.minestar.survivalgames.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.manager.GameManager;

public class ItemListener implements Listener {

    private GameManager gameManager;

    public ItemListener() {
        this.gameManager = Core.gameManager;
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        // game must be running
        if (!this.gameManager.isInGame()) {
            return;
        }

        // only vines, mushrooms, wheat and melons and other normal items are allowed
        Material type = event.getEntity().getItemStack().getType();
        if (type.isBlock()) {
            if (type.equals(Material.VINE) || type.equals(Material.BROWN_MUSHROOM) || type.equals(Material.RED_MUSHROOM) || type.equals(Material.MELON) || type.equals(Material.WHEAT)) {
                return;
            }
            event.setCancelled(true);
        }
    }

}
