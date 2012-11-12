package de.minestar.survivalgames.threads;

import java.util.TimerTask;

import de.minestar.survivalgames.data.SurvivalGame;

public class LootRefillThread extends TimerTask {

    private final SurvivalGame game;

    public LootRefillThread(SurvivalGame game) {
        this.game = game;
    }

    @Override
    public void run() {
        if (this.game.isGameInPrePVP() || game.isGameInSurvival()) {
            this.game.refillLoot();
            this.game.broadcastInfo("All chests have been refilled!");
        }
    }
}
