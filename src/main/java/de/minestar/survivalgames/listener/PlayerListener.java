package de.minestar.survivalgames.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.Settings;
import de.minestar.survivalgames.manager.GameManager;
import de.minestar.survivalgames.manager.PlayerManager;
import de.minestar.survivalgames.utils.Chat;

public class PlayerListener implements Listener {

    private GameManager gameManager;
    private PlayerManager playerManager;

    public PlayerListener() {
        this.gameManager = Core.gameManager;
        this.playerManager = Core.playerManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // only right clicks on a block
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        // get the block
        Block block = event.getClickedBlock();

        // disallow interaction with dispensers
        if (Settings.isBlockDispenserInteraction() && block.getType().equals(Material.DISPENSER)) {
            event.setUseInteractedBlock(Result.DENY);
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
            return;
        }

        // disallow interaction with doors
        if (Settings.isBlockDoorInteraction() && block.getType().equals(Material.WOODEN_DOOR)) {
            event.setUseInteractedBlock(Result.DENY);
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
            return;
        }

        // disallow interaction with stonebuttons
        if (Settings.isBlockStoneButtonInteraction() && block.getType().equals(Material.STONE_BUTTON)) {
            event.setUseInteractedBlock(Result.DENY);
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
            return;
        }

        // disallow interaction with woodbuttons
        if (Settings.isBlockWoodButtonInteraction() && block.getType().equals(Material.WOOD_BUTTON)) {
            event.setUseInteractedBlock(Result.DENY);
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
            return;
        }

        // disallow interaction with levers
        if (Settings.isBlockLeverInteraction() && block.getType().equals(Material.LEVER)) {
            event.setUseInteractedBlock(Result.DENY);
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
            return;
        }

        // disallow interaction with furnaces
        if (Settings.isBlockFurnaceInteraction() && (block.getType().equals(Material.FURNACE) || block.getType().equals(Material.BURNING_FURNACE))) {
            event.setUseInteractedBlock(Result.DENY);
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
            return;
        }

        // disallow interaction with workbenches
        if (Settings.isBlockWorkbenchInteraction() && block.getType().equals(Material.WORKBENCH)) {
            event.setUseInteractedBlock(Result.DENY);
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        // block damage by spectators
        if (event.getDamager().getType().equals(EntityType.PLAYER)) {
            Player attacker = (Player) event.getDamager();
            if (this.playerManager.isSpecator(attacker.getName())) {
                event.setDamage(0);
                event.setCancelled(true);
                return;
            }
        }

        // block damage for spectators
        if (event.getEntity().getType().equals(EntityType.PLAYER)) {
            Player defender = (Player) event.getEntity();
            if (this.playerManager.isSpecator(defender.getName())) {
                event.setDamage(0);
                event.setCancelled(true);
                return;
            }
        }

        // player attacks someone/something (entity, or other player)
        if (event.getDamager().getType().equals(EntityType.PLAYER)) {
            if (!this.gameManager.isPVPEnabled()) {
                event.setDamage(0);
                event.setCancelled(true);
                return;
            }
        }

        if (event.getEntity().getType().equals(EntityType.PLAYER)) {
            // get the DamageCause
            DamageCause cause = event.getCause();
            // only check, if PVP is not enabled
            if (!this.gameManager.isPVPEnabled()) {
                // player got damage by another player, or an entity
                if (cause.equals(DamageCause.ENTITY_ATTACK) || cause.equals(DamageCause.PROJECTILE) || cause.equals(DamageCause.ENTITY_EXPLOSION)) {
                    event.setDamage(0);
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String playerName = event.getEntity().getPlayer().getName();
        if (!this.playerManager.isPlayer(playerName)) {
            return;
        }

        // player thundersound
        this.playThunderSound(event.getEntity());

        // make spectator
        this.playerManager.makeSpectator(playerName);

        // send infomessage
        if (this.playerManager.getPlayerCount() > 1) {
            Chat.broadcast(ChatColor.DARK_GREEN, "Another one bites the dust...");
            Chat.broadcast(ChatColor.GRAY, this.playerManager.getPlayerCount() + " survivors are still alive!");
            return;
        } else if (this.playerManager.hasGameAWinner()) {
            Chat.broadcast(ChatColor.RED, "The games have ended!");
            Chat.broadcast(ChatColor.GOLD, "'" + this.playerManager.getWinner() + "' is the winner!");
            return;
        } else if (this.playerManager.hasGameADraw()) {
            Chat.broadcast(ChatColor.RED, "The games have ended!");
            Chat.broadcast(ChatColor.GOLD, "Noone has survived... :{");
            return;
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        String playerName = event.getPlayer().getName();
        if (this.playerManager.isSpecator(playerName)) {
            this.playerManager.hidePlayer(playerName);
        }
    }

    public void onPlayerTeleport(PlayerTeleportEvent event) {
        String playerName = event.getPlayer().getName();
        if (this.playerManager.isSpecator(playerName)) {
            this.playerManager.hidePlayer(playerName);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        if (this.gameManager.isInGame()) {
            this.playerManager.makeSpectator(playerName);
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        this.updatePlayerOnDisconnect(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        this.updatePlayerOnDisconnect(event.getPlayer());
    }

    private void updatePlayerOnDisconnect(Player player) {
        String playerName = player.getName();
        if (this.gameManager.isInGame() && this.playerManager.isPlayer(playerName)) {
            this.playerManager.removeFromPlayerList(playerName);
        }
    }

    private void playThunderSound(Entity entity) {
        CraftWorld cWorld = (CraftWorld) entity.getWorld();
        cWorld.getHandle().makeSound(((CraftEntity) entity).getHandle(), "ambient.weather.thunder", 10000.0F, 1.0F);
    }
}
