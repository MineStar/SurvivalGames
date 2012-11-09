package de.minestar.survivalgames.manager;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.GameState;

public class GameManager {

    private GameState gameState = GameState.LOBBY;

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void preGame() {
        this.gameState = GameState.PREGAME;
        Core.playerManager.preGame();
    }

    public void startGame() {
        this.gameState = GameState.PRE_PVP;
        Core.playerManager.startGame();
    }

    public void enablePVP() {
        this.gameState = GameState.SURVIVAL;
    }

    public void startDeathmatch() {
        this.gameState = GameState.DEATHMATCH;
        Core.playerManager.startDeathmatch();
    }

    public void endGame() {
        Core.lootManager.endGame();
        Core.playerManager.endGame();
        this.gameState = GameState.END;
    }

    public boolean isInGame() {
        return !this.gameState.equals(GameState.LOBBY);
    }

    public boolean isInDeathmatch() {
        return this.gameState.equals(GameState.DEATHMATCH);
    }

    public boolean isPVPEnabled() {
        return this.gameState.equals(GameState.SURVIVAL) || this.gameState.equals(GameState.DEATHMATCH);
    }
}
