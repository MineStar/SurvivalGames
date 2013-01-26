package de.minestar.craftz.threads;

import java.util.TimerTask;

import org.bukkit.Bukkit;

import de.minestar.craftz.Core;
import de.minestar.craftz.data.SurvivalGame;

public class StartPVPThread extends TimerTask {

    private final SurvivalGame game;

    public StartPVPThread(SurvivalGame game) {
        this.game = game;
    }

    @Override
    public void run() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new StartPVPTask(this.game));
    }

}
