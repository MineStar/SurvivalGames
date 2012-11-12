package de.minestar.survivalgames.threads;

import org.bukkit.ChatColor;

import de.minestar.survivalgames.data.SurvivalGame;

public class ReturnToLobbyTask implements Runnable {

    private final SurvivalGame game;

    public ReturnToLobbyTask(SurvivalGame game) {
        this.game = game;
    }

    @Override
    public void run() {
        if (this.game.isGameInEnd()) {
            this.game.broadcast(ChatColor.RED + SurvivalGame.LIMITER);
            this.game.broadcastInfo("Welcome to the lobby!");
            this.game.broadcast(ChatColor.RED + SurvivalGame.LIMITER);
            this.game.goToLobby();
        }
    }
}
