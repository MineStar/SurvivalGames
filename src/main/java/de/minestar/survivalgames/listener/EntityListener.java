package de.minestar.survivalgames.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.manager.GameManager;
import de.minestar.survivalgames.manager.PlayerManager;

public class EntityListener implements Listener {

    private GameManager gameManager;
    private PlayerManager playerManager;

    public EntityListener() {
        this.gameManager = Core.gameManager;
        this.playerManager = Core.playerManager;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    private boolean cancelTargetEvent(Entity target) {
        if (!this.gameManager.isInGame()) {
            return false;
        }

        if (target.getType().equals(EntityType.PLAYER)) {
            Player player = (Player) target;
            if (this.playerManager.isPlayer(player.getName())) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        if (this.cancelTargetEvent(event.getTarget())) {
            event.setTarget(null);
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (this.cancelTargetEvent(event.getTarget())) {
            event.setTarget(null);
            event.setCancelled(true);
            return;
        }
    }
}
