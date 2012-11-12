package de.minestar.survivalgames.data;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class SurvivalPlayer implements Comparable<SurvivalPlayer> {

    private final String playerName;
    private boolean isPlayer = false;
    private boolean isReady = false;
    private Player bukkitPlayer;
    private final SurvivalGame currentGame;

    public SurvivalPlayer(String playerName, SurvivalGame currentGame) {
        this.playerName = playerName;
        this.currentGame = currentGame;
        this.updateBukkitPlayer();
        this.makeSpectator();
    }

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    public boolean toggleReady() {
        this.isReady = !this.isReady;
        return this.isReady;
    }

    public boolean isReady() {
        return isReady;
    }

    public boolean isPlayer() {
        return this.isPlayer;
    }

    public boolean isSpectator() {
        return !this.isPlayer();
    }

    public void makePlayer() {
        this.isPlayer = true;
    }

    public void makeSpectator() {
        this.isPlayer = false;
    }

    public void resetPlayer() {
        // reset player
        this.resetInventory();
        this.resetCommon();
    }

    public void show() {
        // we need a valid bukkitplayer
        this.checkBukkitPlayer();

        // show to everyone on the server
        Player[] playerList = Bukkit.getOnlinePlayers();
        for (Player otherPlayer : playerList) {
            if (!this.bukkitPlayer.getName().equalsIgnoreCase(otherPlayer.getName())) {
                otherPlayer.showPlayer(this.bukkitPlayer);
            }
        }
    }

    public void hide() {
        // we need a valid bukkitplayer
        this.checkBukkitPlayer();

        // hide to everyone on the server
        Player[] playerList = Bukkit.getOnlinePlayers();
        for (Player otherPlayer : playerList) {
            if (!this.bukkitPlayer.getName().equalsIgnoreCase(otherPlayer.getName())) {
                otherPlayer.hidePlayer(this.bukkitPlayer);
            }
        }
    }

    public void resetCommon() {
        // we need a valid bukkitplayer
        this.checkBukkitPlayer();

        // set gamemode
        this.bukkitPlayer.setGameMode(GameMode.SURVIVAL);

        // common things
        this.bukkitPlayer.setFireTicks(0);
        this.bukkitPlayer.setLevel(0);
        this.bukkitPlayer.setExp(0f);
        this.bukkitPlayer.setHealth(20);
        this.bukkitPlayer.setFoodLevel(20);
        this.bukkitPlayer.setAllowFlight(false);
    }

    public void resetInventory() {
        // we need a valid bukkitplayer
        this.checkBukkitPlayer();

        // clear inventories
        this.bukkitPlayer.getInventory().clear();
        this.bukkitPlayer.getInventory().setHelmet(null);
        this.bukkitPlayer.getInventory().setChestplate(null);
        this.bukkitPlayer.getInventory().setLeggings(null);
        this.bukkitPlayer.getInventory().setBoots(null);
    }

    public void broadcast(String message) {
        // we need a valid bukkitplayer
        this.checkBukkitPlayer();
        this.bukkitPlayer.sendMessage(message);
    }

    public void teleport(PlayerSpawn playerSpawn) {
        // we need a valid bukkitplayer
        this.checkBukkitPlayer();

        // teleport
        this.bukkitPlayer.teleport(playerSpawn.getLocation());
    }

    public void updateBukkitPlayer() {
        // update the bukkitplayer
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null || !player.isOnline()) {
            this.bukkitPlayer = null;
        }
        this.bukkitPlayer = player;

        // we need a valid bukkitplayer
        this.checkBukkitPlayer();
    }

    private void checkBukkitPlayer() {
        // we need a valid bukkitplayer
        if (this.bukkitPlayer == null) {
            throw new RuntimeException("BukkitPlayer '" + this.playerName + "' not found!");
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public SurvivalGame getCurrentGame() {
        return currentGame;
    }

    @Override
    public int hashCode() {
        return this.playerName.hashCode();
    }

    @Override
    public int compareTo(SurvivalPlayer otherPlayer) {
        return otherPlayer.playerName.compareTo(this.playerName);
    }
}
