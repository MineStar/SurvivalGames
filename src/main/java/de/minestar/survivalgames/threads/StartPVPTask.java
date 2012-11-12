package de.minestar.survivalgames.threads;

import org.bukkit.ChatColor;

import de.minestar.survivalgames.data.SurvivalGame;

public class StartPVPTask implements Runnable {

    private final SurvivalGame game;

    public StartPVPTask(SurvivalGame game) {
        this.game = game;
    }

    @Override
    public void run() {
        if (this.game.isGameInPrePVP()) {
            this.game.broadcast(ChatColor.RED + SurvivalGame.LIMITER);
            this.game.broadcastInfo("PVP is now enabled!");
            this.game.broadcast(ChatColor.RED + SurvivalGame.LIMITER);
            this.game.goToSurvival();
        }
    }
}
