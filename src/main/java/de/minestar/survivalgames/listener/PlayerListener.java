package de.minestar.survivalgames.listener;

import java.util.Iterator;

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
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.Settings;
import de.minestar.survivalgames.manager.GameManager;
import de.minestar.survivalgames.manager.PlayerManager;
import de.minestar.survivalgames.utils.Chat;
import de.minestar.survivalgames.utils.LocationUtils;

public class PlayerListener implements Listener {

    private GameManager gameManager;
    private PlayerManager playerManager;

    public PlayerListener() {
        this.gameManager = Core.gameManager;
        this.playerManager = Core.playerManager;
    }

    public void onEnable() {
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // only affect this, if the game is not in pregame, of if the player is a spectator
        if (!this.gameManager.isInPreGame() || this.playerManager.isSpectator(event.getPlayer().getName())) {
            return;
        }

        // don't move
        if (!LocationUtils.equals(event.getFrom(), event.getTo())) {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!this.gameManager.isInGame()) {
            return;
        }

        String playerName = event.getPlayer().getName();
        // players can not read what spectators are writing

        event.setFormat("%2$s");
        if (this.playerManager.isSpectator(playerName)) {
            event.setMessage(ChatColor.DARK_RED + "(SPEC) " + ChatColor.DARK_BLUE + playerName + ": " + ChatColor.GRAY + event.getMessage());
            Iterator<Player> iteratorPlayer = event.getRecipients().iterator();
            while (iteratorPlayer.hasNext()) {
                Player otherPlayer = iteratorPlayer.next();
                if (this.playerManager.isPlayer(otherPlayer.getName())) {
                    iteratorPlayer.remove();
                }
            }
        } else {
            event.setMessage(ChatColor.AQUA + playerName + ": " + ChatColor.GRAY + event.getMessage());
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (!this.gameManager.isInGame()) {
            return;
        }

        // only ops!
        if (event.getPlayer().isOp()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!this.gameManager.isInGame()) {
            return;
        }

        // only right clicks on a block
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        // get the block
        Block block = event.getClickedBlock();

        // check interaction
        if (Settings.isNonUseable(block.getType()) || Core.playerManager.isSpectator(event.getPlayer().getName())) {
            event.setUseInteractedBlock(Result.DENY);
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        // no damage, if there is no game
        if (!this.gameManager.isInGame() || this.gameManager.isInPreGame() || !this.gameManager.isPVPEnabled()) {
            event.setDamage(0);
            event.setCancelled(true);
            return;
        }

        // block damage by spectators
        if (event.getDamager().getType().equals(EntityType.PLAYER)) {
            Player attacker = (Player) event.getDamager();
            if (this.playerManager.isSpectator(attacker.getName())) {
                event.setDamage(0);
                event.setCancelled(true);
                return;
            }
        }

        // block damage for spectators
        if (event.getEntity().getType().equals(EntityType.PLAYER)) {
            Player defender = (Player) event.getEntity();
            if (this.playerManager.isSpectator(defender.getName())) {
                event.setDamage(0);
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!this.gameManager.isInGame()) {
            return;
        }

        String playerName = event.getEntity().getPlayer().getName();
        if (!this.playerManager.isPlayer(playerName)) {
            return;
        }

        // player thundersound
        this.playThunderSound(event.getEntity());

        // make spectator
        this.playerManager.makeSpectator(playerName);

        event.getEntity().sendMessage(ChatColor.RED + "--------------------------------------------");
        event.getEntity().sendMessage(ChatColor.RED + "YOU ARE DEAD!");
        event.getEntity().sendMessage(ChatColor.RED + "--------------------------------------------");

        event.setDeathMessage(null);

        if (this.playerManager.getPlayerCount() > 1) {
            Chat.broadcast(ChatColor.DARK_GREEN, "Another one bites the dust...");
            Chat.broadcast(ChatColor.GRAY, this.playerManager.getPlayerCount() + " survivors are still alive!");
        }

        this.gameManager.checkForWinner();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!this.gameManager.isInGame()) {
            event.setRespawnLocation(Settings.getLobbySpawn().getLocation());
            return;
        }

        String playerName = event.getPlayer().getName();
        if (this.playerManager.isSpectator(playerName)) {
            this.playerManager.hidePlayer(playerName);
            event.setRespawnLocation(Settings.getSpectatorSpawn().getLocation());
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!event.getEntityType().equals(EntityType.PLAYER)) {
            return;
        }

        Player player = (Player) event.getEntity();
        if (this.playerManager.isSpectator(player.getName())) {
            event.setCancelled(true);
            event.setFoodLevel(20);
            return;
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!this.gameManager.isInGame()) {
            event.setTo(Settings.getLobbySpawn().getLocation());
            return;
        }

        String playerName = event.getPlayer().getName();
        if (this.playerManager.isSpectator(playerName)) {
            this.playerManager.hidePlayer(playerName);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        if (this.gameManager.isInGame()) {
            this.playerManager.makeSpectator(playerName);
        } else {
            this.playerManager.showPlayer(playerName);
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

    @EventHandler
    public void onPlayerItemPickUp(PlayerPickupItemEvent event) {
        if (!this.gameManager.isInGame()) {
            return;
        }

        String playerName = event.getPlayer().getName();
        if (this.playerManager.isSpectator(playerName)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!this.gameManager.isInGame()) {
            return;
        }

        String playerName = event.getPlayer().getName();
        if (this.playerManager.isSpectator(playerName)) {
            event.setCancelled(true);
            return;
        }
    }

    private void updatePlayerOnDisconnect(Player player) {
        String playerName = player.getName();
        this.playerManager.removeFromPlayerList(playerName);
        this.playerManager.removeFromSpectatorList(playerName);

        ItemStack[] contents = player.getInventory().getContents();
        for (ItemStack stack : contents) {
            if (stack != null && !stack.getType().equals(Material.AIR)) {
                player.getWorld().dropItemNaturally(player.getLocation(), stack);
            }
        }

        if (this.gameManager.isInGame()) {
            this.gameManager.checkForWinner();
        }
    }

    private void playThunderSound(Entity entity) {
        CraftWorld cWorld = (CraftWorld) entity.getWorld();
        cWorld.getHandle().makeSound(((CraftEntity) entity).getHandle(), "ambient.weather.thunder", 10000.0F, 1.0F);
    }
}
