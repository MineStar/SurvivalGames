package de.minestar.survivalgames.manager;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerManager {

    private HashSet<String> spectators, players;

    public void onEnable() {
        this.spectators = new HashSet<String>();
        this.players = new HashSet<String>();
    }

    public void startGame() {
        Player[] playerList = Bukkit.getOnlinePlayers();
        for (Player player : playerList) {
            players.add(player.getName());
        }
    }

    public void endGame() {
        // unhide spectators
        this.unhideSpectators();

        // clear lists
        this.clearLists();
    }

    public void onDisable() {
        this.clearLists();
    }

    public void makeSpectator(String playerName) {
        this.hidePlayer(playerName);
        this.players.remove(playerName);
        this.spectators.add(playerName);
    }

    public boolean isSpecator(String playerName) {
        return this.spectators.contains(playerName);
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
        return this.spectators.contains(playerName);
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

    public void teleportAllToSpawn() {
        // hide player to everyone, except himself
        Player[] playerList = Bukkit.getOnlinePlayers();
        for (Player player : playerList) {
            // TODO: IMPLEMENT TELEPORT
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
