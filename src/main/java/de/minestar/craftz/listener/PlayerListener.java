package de.minestar.craftz.listener;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_4_6.CraftWorld;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
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
import org.bukkit.inventory.ItemStack;

import de.minestar.craftz.Core;
import de.minestar.craftz.data.SurvivalGame;
import de.minestar.craftz.data.SurvivalPlayer;
import de.minestar.craftz.manager.GameManager;
import de.minestar.craftz.utils.LocationUtils;

public class PlayerListener implements Listener {

    private GameManager gameManager;

    public PlayerListener() {
        this.gameManager = Core.gameManager;
    }

    public void onEnable() {
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        // only on blockchanges
        if (LocationUtils.equalsXZ(event.getFrom(), event.getTo())) {
            return;
        }

        // get the player
        SurvivalPlayer sPlayer = this.gameManager.getPlayer(event.getPlayer().getName());
        if (sPlayer == null) {
            return;
        }

        // only affect this, if the game is not in pregame, of if the player is a spectator
        if (sPlayer.getCurrentGame().isGameInPreGame() && sPlayer.isPlayer()) {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        SurvivalGame game = Core.gameManager.getMainGame();
        if (game != null) {
            Core.gameManager.playerJoinGame(game.getGameName(), event.getPlayer().getName());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // get the player
        SurvivalPlayer sPlayer = this.gameManager.getPlayer(event.getPlayer().getName());
        if (sPlayer == null) {
            return;
        }

        String playerName = event.getPlayer().getName();

        // players can not read what spectators are writing
        event.setFormat("%2$s");
        if (sPlayer.isSpectator()) {
            event.setMessage(ChatColor.DARK_RED + "(SPEC) " + ChatColor.LIGHT_PURPLE + playerName + ": " + ChatColor.WHITE + event.getMessage());
            Iterator<Player> iteratorPlayer = event.getRecipients().iterator();
            while (iteratorPlayer.hasNext()) {
                Player otherPlayer = iteratorPlayer.next();
                SurvivalPlayer otherSPlayer = this.gameManager.getPlayer(otherPlayer.getName());
                if (otherSPlayer == null || otherSPlayer.isPlayer() || !otherSPlayer.getCurrentGame().equals(sPlayer.getCurrentGame())) {
                    iteratorPlayer.remove();
                }
            }
        } else {
            event.setMessage(ChatColor.AQUA + playerName + ": " + ChatColor.WHITE + event.getMessage());
            Iterator<Player> iteratorPlayer = event.getRecipients().iterator();
            while (iteratorPlayer.hasNext()) {
                Player otherPlayer = iteratorPlayer.next();
                SurvivalPlayer otherSPlayer = this.gameManager.getPlayer(otherPlayer.getName());
                if (otherSPlayer == null || !otherSPlayer.getCurrentGame().equals(sPlayer.getCurrentGame())) {
                    iteratorPlayer.remove();
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        // get the player
        SurvivalPlayer sPlayer = this.gameManager.getPlayer(event.getPlayer().getName());
        if (sPlayer == null) {
            return;
        }

        // only ops!
        if (!event.getPlayer().isOp()) {
            if (!event.getMessage().startsWith("/game ")) {
                event.setCancelled(true);
            }
        }
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
        // get the player
        SurvivalPlayer sPlayer = this.gameManager.getPlayer(event.getPlayer().getName());
        if (sPlayer == null) {
            return;
        }

        if (sPlayer.getCurrentGame().isGameInLobby()) {
            return;
        }

        // only left & right clicks on a block
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        // get the block
        Block block = event.getClickedBlock();
        if (event.getClickedBlock() == null) {
            return;
        }

        // check interaction
        if (sPlayer.getCurrentGame().getSettings().isNonUseable(block.getType()) || sPlayer.isSpectator()) {
            event.setUseInteractedBlock(Result.DENY);
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
            return;
        } else {
            sPlayer.getCurrentGame().addBlockUpdate(event.getClickedBlock().getLocation());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {

        // get the players
        Player attacker = null, defender = null;
        SurvivalPlayer sAttacker = null, sDefender = null;

        if (event.getDamager().getType().equals(EntityType.PLAYER)) {
            attacker = (Player) event.getDamager();
            sAttacker = this.gameManager.getPlayer(attacker.getName());
        }

        if (event.getDamager().getType().equals(EntityType.ARROW)) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter().getType().equals(EntityType.PLAYER)) {
                attacker = (Player) arrow.getShooter();
                sAttacker = this.gameManager.getPlayer(attacker.getName());
            }
        }

        if (event.getEntity().getType().equals(EntityType.PLAYER)) {
            defender = (Player) event.getEntity();
            sDefender = this.gameManager.getPlayer(defender.getName());
        }

        // only handle, if we have at least one player
        if (attacker == null && defender == null) {
            return;
        }

        // attacker : player
        // defender : null (this means a mob)
        if (sAttacker != null && sDefender == null) {
            if (!sAttacker.getCurrentGame().isGameInSurvival() && !sAttacker.getCurrentGame().isGameInDeathmatch()) {
                event.setDamage(0);
                event.setCancelled(true);
            }
            return;
        }

        // attacker : null (projectile, entity, or other things)
        // defender : player
        if (sAttacker == null && sDefender != null) {
            if (!sDefender.getCurrentGame().isGameInSurvival() && !sDefender.getCurrentGame().isGameInDeathmatch()) {
                if (event.getCause().equals(DamageCause.POISON) || event.getCause().equals(DamageCause.PROJECTILE) || event.getCause().equals(DamageCause.MAGIC) || event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
            return;
        }

        // attacker : player
        // defender : player
        if (sAttacker != null && sDefender != null) {
            if (!sAttacker.getCurrentGame().equals(sDefender.getCurrentGame())) {
                event.setDamage(0);
                event.setCancelled(true);
            }
            if (!sAttacker.getCurrentGame().isGameInSurvival() && !sAttacker.getCurrentGame().isGameInDeathmatch()) {
                event.setDamage(0);
                event.setCancelled(true);
            }
            return;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        // get the player
        String playerName = event.getEntity().getPlayer().getName();
        SurvivalPlayer sPlayer = this.gameManager.getPlayer(playerName);
        if (sPlayer == null) {
            return;
        }

        // spectators are not affected
        if (sPlayer.isSpectator()) {
            return;
        }

        // player thundersound
        this.playThunderSound(event.getEntity());

        // make spectator
        sPlayer.makePlayer();
        sPlayer.hide();

        // broadcast message to the game
        sPlayer.broadcast(ChatColor.RED + SurvivalGame.LIMITER);
        sPlayer.broadcast(ChatColor.RED + "YOU ARE DEAD!");
        sPlayer.broadcast(ChatColor.RED + SurvivalGame.LIMITER);

        // remove deathmessage
        event.setDeathMessage(null);
        event.getEntity().getPlayer().setHealth(20);
        event.getEntity().getPlayer().setFoodLevel(20);

        // update game
        sPlayer.getCurrentGame().onPlayerDeath(sPlayer);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        // we need a player fo this
        if (!event.getEntityType().equals(EntityType.PLAYER)) {
            return;
        }

        // get the player
        Player player = (Player) event.getEntity();
        SurvivalPlayer sPlayer = this.gameManager.getPlayer(player.getName());
        if (sPlayer == null) {
            return;
        }

        // spectators are not hungry
        if (sPlayer.isSpectator()) {
            event.setCancelled(true);
            event.setFoodLevel(20);
            return;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        this.updatePlayerOnDisconnect(event.getPlayer());
    }

    private void updatePlayerOnDisconnect(Player player) {
        // get the player
        SurvivalPlayer sPlayer = this.gameManager.getPlayer(player.getName());
        if (sPlayer == null) {
            return;
        }

        ItemStack[] contents = player.getInventory().getContents();
        for (ItemStack stack : contents) {
            if (stack != null && !stack.getType().equals(Material.AIR)) {
                player.getWorld().dropItemNaturally(player.getLocation(), stack);
            }
        }
        Core.gameManager.playerQuitGame(sPlayer.getPlayerName());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        this.updatePlayerOnDisconnect(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemPickUp(PlayerPickupItemEvent event) {
        // get the player
        SurvivalPlayer sPlayer = this.gameManager.getPlayer(event.getPlayer().getName());
        if (sPlayer == null) {
            return;
        }

        // only vines, mushrooms, wheat and melons and other normal items can be pickedup
        Material type = event.getItem().getItemStack().getType();
        if (type.isBlock()) {
            if (type.equals(Material.VINE) || type.equals(Material.BROWN_MUSHROOM) || type.equals(Material.RED_MUSHROOM) || type.equals(Material.MELON) || type.equals(Material.WHEAT)) {
                return;
            }
            event.setCancelled(true);
            return;
        }

        // disallow spectators to pickup anything
        if (sPlayer.isSpectator()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        // get the player
        SurvivalPlayer sPlayer = this.gameManager.getPlayer(event.getPlayer().getName());
        if (sPlayer == null) {
            return;
        }

        // only vines, mushrooms, wheat and melons and other normal items can be dropped
        Material type = event.getItemDrop().getItemStack().getType();
        if (type.isBlock()) {
            if (type.equals(Material.VINE) || type.equals(Material.BROWN_MUSHROOM) || type.equals(Material.RED_MUSHROOM) || type.equals(Material.MELON) || type.equals(Material.WHEAT)) {
                return;
            }
            event.setCancelled(true);
            return;
        }

        // disallow spectators to pickup anything
        if (sPlayer.isSpectator()) {
            event.setCancelled(true);
            return;
        }

        // add itemdrop
        sPlayer.getCurrentGame().addItemUpdate(event.getItemDrop());
    }

    private void playThunderSound(Entity entity) {
        CraftWorld cWorld = (CraftWorld) entity.getWorld();
        cWorld.getHandle().makeSound(((CraftEntity) entity).getHandle(), "ambient.weather.thunder", 10000.0F, 2.0F);
    }
}
