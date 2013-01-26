package de.minestar.craftz.threads;

import org.bukkit.ChatColor;

import de.minestar.craftz.data.SurvivalGame;

public class StartGameTask implements Runnable {

    private final SurvivalGame game;

    public StartGameTask(SurvivalGame game) {
        this.game = game;
    }

    @Override
    public void run() {
        if (this.game.isGameInPreGame()) {
            this.game.broadcast(ChatColor.RED + SurvivalGame.LIMITER);
            this.game.broadcastInfo("The game has started!");
            this.game.broadcast(ChatColor.RED + SurvivalGame.LIMITER);
            this.game.goToPrePVP();
        }
    }

}
