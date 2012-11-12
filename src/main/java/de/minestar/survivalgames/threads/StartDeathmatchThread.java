package de.minestar.survivalgames.threads;

import java.util.TimerTask;

import org.bukkit.Bukkit;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.SurvivalGame;

public class StartDeathmatchThread extends TimerTask {

    private final SurvivalGame game;

    public StartDeathmatchThread(SurvivalGame game) {
        this.game = game;
    }

    @Override
    public void run() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new StartDeathmatchTask(this.game));
    }

}
