package de.minestar.survivalgames.manager;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.GameState;

public class GameManager {

    private GameState gameState = GameState.NONE;

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void createLobby() {
        this.endGame();
        this.gameState = GameState.LOBBY;
    }

    public void startGame() {
        this.gameState = GameState.PREGAME;
    }
    
    public void startDeathmatch() {
        this.gameState = GameState.DEATHMATCH;
        Core.playerManager.teleportAllToSpawn();
    }

    public void endGame() {
        Core.lootManager.endGame();
        Core.playerManager.endGame();
        this.gameState = GameState.END;
    }

    public boolean isInGame() {
        return !this.gameState.equals(GameState.NONE);
    }

    public boolean isInDeathmatch() {
        return this.gameState.equals(GameState.DEATHMATCH);
    }

    public boolean isPVPEnabled() {
        return this.gameState.equals(GameState.SURVIVAL) || this.gameState.equals(GameState.DEATHMATCH);
    }
}
