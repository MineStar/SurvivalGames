package de.minestar.craftz.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import de.minestar.craftz.Core;
import de.minestar.craftz.data.SurvivalPlayer;
import de.minestar.craftz.manager.GameManager;

public class EntityListener implements Listener {

    private GameManager gameManager;

    public EntityListener() {
        this.gameManager = Core.gameManager;
    }

    private boolean cancelTargetEvent(Entity target) {
        if (target.getType().equals(EntityType.PLAYER)) {
            Player player = (Player) target;
            SurvivalPlayer sPlayer = this.gameManager.getPlayer(player.getName());
            if (sPlayer != null && sPlayer.isSpectator()) {
                return true;
            }
        }
        return false;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        if (this.cancelTargetEvent(event.getTarget())) {
            event.setTarget(null);
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        if (this.cancelTargetEvent(event.getTarget())) {
            event.setTarget(null);
            event.setCancelled(true);
            return;
        }
    }
}
