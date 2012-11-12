package de.minestar.survivalgames.threads;

import java.util.TimerTask;

import org.bukkit.Bukkit;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.SurvivalGame;

public class ReturnToLobbyThread extends TimerTask {

    private final SurvivalGame game;

    public ReturnToLobbyThread(SurvivalGame game) {
        this.game = game;
    }

    @Override
    public void run() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new ReturnToLobbyTask(this.game));
    }
}
