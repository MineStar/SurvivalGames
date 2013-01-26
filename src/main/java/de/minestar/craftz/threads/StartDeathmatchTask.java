package de.minestar.craftz.threads;

import org.bukkit.ChatColor;

import de.minestar.craftz.data.SurvivalGame;

public class StartDeathmatchTask implements Runnable {

    private final SurvivalGame game;

    public StartDeathmatchTask(SurvivalGame game) {
        this.game = game;
    }

    @Override
    public void run() {
        if (this.game.isGameInSurvival()) {
            this.game.broadcast(ChatColor.RED + SurvivalGame.LIMITER);
            this.game.broadcastInfo("Time for deathmatch!");
            this.game.broadcast(ChatColor.RED + SurvivalGame.LIMITER);
            this.game.goToDeathmatch();
        }
    }

}
