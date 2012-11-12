package de.minestar.survivalgames.manager;

import java.io.File;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.SurvivalGame;
import de.minestar.survivalgames.data.SurvivalPlayer;

public class GameManager {

    private HashMap<String, SurvivalGame> gameList;
    private HashMap<String, SurvivalPlayer> playerList;

    public GameManager() {
        this.gameList = new HashMap<String, SurvivalGame>();
        this.playerList = new HashMap<String, SurvivalPlayer>();
    }

    public void onEnable() {
        this.initGames();
    }

    private void initGames() {
        File[] dirList = Core.INSTANCE.getDataFolder().listFiles();
        for (File dir : dirList) {
            if (!dir.isDirectory()) {
                continue;
            }
            System.out.println("Creating game: " + dir.getName());
            this.createGame(dir.getName());
        }
    }

    public void onDisable() {
        for (SurvivalGame game : this.gameList.values()) {
            game.closeGame();
        }
        this.gameList.clear();
    }

    // /////////////////////////////////////////////////////////
    //
    // Methods for games
    //
    // /////////////////////////////////////////////////////////

    public void listGames(Player player) {
        for (SurvivalGame game : this.gameList.values()) {
            player.sendMessage(ChatColor.GRAY + game.getGameName());
        }
    }

    public boolean startGame(String gameName) {
        if (!this.gameExists(gameName)) {
            return false;
        }

        SurvivalGame game = this.getGame(gameName);
        game.goToPreGame();
        return true;
    }

    public boolean createGame(String gameName) {
        if (this.gameExists(gameName)) {
            return false;
        }

        SurvivalGame game = new SurvivalGame(gameName);
        this.gameList.put(gameName.toLowerCase(), game);
        return true;
    }

    public boolean stopGame(String gameName) {
        if (!this.gameExists(gameName)) {
            return false;
        }

        SurvivalGame game = this.getGame(gameName);
        game.stopGame();
        return true;
    }

    public boolean closeGame(String gameName) {
        if (!this.gameExists(gameName)) {
            return false;
        }

        SurvivalGame game = this.getGame(gameName);
        game.closeGame();
        this.gameList.remove(gameName.toLowerCase());
        return true;
    }

    public SurvivalGame getGame(String gameName) {
        return this.gameList.get(gameName.toLowerCase());
    }

    public boolean gameExists(String gameName) {
        return this.gameList.containsKey(gameName.toLowerCase());
    }

    // /////////////////////////////////////////////////////////
    //
    // Methods for players
    //
    // /////////////////////////////////////////////////////////

    public boolean playerJoinGame(String gameName, String playerName) {
        // the game must exist, and the player must NOT be in a game
        if (!this.gameExists(gameName) || this.isPlayerInAnyGame(playerName)) {
            return false;
        }

        // join the game ...
        SurvivalGame game = this.getGame(gameName);
        if (!game.joinGame(playerName)) {
            return false;
        }

        // ... and add the player to the list
        this.playerList.put(playerName, game.getPlayer(playerName));
        return true;
    }

    public boolean playerQuitGame(String playerName) {
        SurvivalPlayer player = this.getPlayer(playerName);
        if (player == null) {
            return false;
        }

        if (player.getCurrentGame().quitGame(playerName)) {
            this.playerList.remove(playerName);
            player.getCurrentGame().broadcast(ChatColor.GRAY + "'" + playerName + "' has left the survivalgame!");
            System.out.println("player quit game");
            return true;
        }

        return false;
    }

    public boolean isPlayerInAnyGame(String playerName) {
        return this.playerList.containsKey(playerName);
    }

    public SurvivalPlayer getPlayer(String playerName) {
        return this.playerList.get(playerName);
    }

}
