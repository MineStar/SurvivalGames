package de.minestar.survivalgames.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.PlayerSpawn;
import de.minestar.survivalgames.data.Settings;

public class PlayerManager {

    private HashSet<String> spectators, players;

    public void onEnable() {
        this.spectators = new HashSet<String>();
        this.players = new HashSet<String>();
    }

    public void preGame() {
        Player[] playerList = Bukkit.getOnlinePlayers();
        this.clearLists();
        for (Player player : playerList) {
            player.getInventory().clear();
            player.getInventory().setHelmet(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setBoots(null);
            players.add(player.getName());
        }
        Core.playerManager.teleportAllToGameSpawn();
    }

    public void startGame() {
        // nothing to do here
    }

    public void startDeathmatch() {
        Core.playerManager.teleportAllToGameSpawn();
    }

    public void endGame() {
        // unhide spectators
        this.unhideSpectators();

        // clear lists
        this.clearLists();

        // teleport to lobby spawn
        this.teleportAllToLobbySpawn();
    }

    public void onDisable() {
        this.clearLists();
    }

    public void makeSpectator(String playerName) {
        this.hidePlayer(playerName);
        this.players.remove(playerName);
        this.spectators.add(playerName);
    }

    public boolean isSpectator(String playerName) {
        return this.spectators.contains(playerName);
    }

    public void removeFromSpectatorList(String playerName) {
        this.spectators.remove(playerName);
    }

    public void makePlayer(String playerName) {
        this.showPlayer(playerName);
        this.spectators.remove(playerName);
        this.players.add(playerName);
    }

    public void removeFromPlayerList(String playerName) {
        this.players.remove(playerName);
    }

    public boolean isPlayer(String playerName) {
        return this.players.contains(playerName);
    }

    public void showPlayer(String playerName) {
        // get the player
        Player bukkitPlayer = Bukkit.getPlayerExact(playerName);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        // show player to everyone, except himself
        Player[] playerList = Bukkit.getOnlinePlayers();
        for (Player player : playerList) {
            // check for the player himself
            if (player.getName().equalsIgnoreCase(playerName)) {
                continue;
            }
            // show player
            player.showPlayer(bukkitPlayer);
        }
    }

    public void hidePlayer(String playerName) {
        // get the player
        Player bukkitPlayer = Bukkit.getPlayerExact(playerName);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        // hide player to everyone, except himself
        Player[] playerList = Bukkit.getOnlinePlayers();
        for (Player player : playerList) {
            // check for the player himself
            if (player.getName().equalsIgnoreCase(playerName)) {
                continue;
            }
            // hide player
            player.hidePlayer(bukkitPlayer);
        }
    }

    private void unhideSpectators() {
        // unhide spectators
        for (String playerName : this.spectators) {
            this.showPlayer(playerName);
        }
    }

    private void clearLists() {
        // clear lists
        this.players.clear();
        this.spectators.clear();
    }

    public boolean hasGameAWinner() {
        // we have a winner, if there is only exact one player left
        return this.getPlayerCount() == 1;
    }

    public String getWinner() {
        if (hasGameAWinner()) {
            for (String playerName : this.players) {
                return playerName;
            }
        }
        return "UNKNOWN";
    }

    public void teleportAllToLobbySpawn() {
        // get the actual playerlist
        Player[] playerList = Bukkit.getOnlinePlayers();

        // teleport every player to the spawn
        for (Player player : playerList) {
            player.teleport(Settings.getLobbySpawn().getLocation());
        }
    }

    public void teleportAllToGameSpawn() {
        // get the actual playerlist
        Player[] playerList = Bukkit.getOnlinePlayers();

        // write spawns into arraylist for randomized use
        ArrayList<PlayerSpawn> unusedSpawns = new ArrayList<PlayerSpawn>();
        for (PlayerSpawn spawn : Settings.getPlayerSpawns()) {
            unusedSpawns.add(spawn);
        }

        // teleport every player to the spawn
        Random random = new Random();
        for (Player player : playerList) {
            if (this.isPlayer(player.getName())) {
                // player = teleport to random spawnpoint
                int index = (int) (random.nextDouble() * unusedSpawns.size());
                player.teleport(unusedSpawns.get(index).getLocation());
                unusedSpawns.remove(index);
            } else {
                // spectator = teleport to spectatorspawn
                player.teleport(Settings.getSpectatorSpawn().getLocation());
            }
        }
    }

    public boolean hasGameADraw() {
        // we have a draw, if there is less than one player left
        return this.getPlayerCount() < 1;
    }

    public int getPlayerCount() {
        return this.players.size();
    }

}
