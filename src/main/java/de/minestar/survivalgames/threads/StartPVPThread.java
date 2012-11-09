package de.minestar.survivalgames.threads;

import java.util.TimerTask;

import org.bukkit.Bukkit;

import de.minestar.survivalgames.Core;

public class StartPVPThread extends TimerTask {

    @Override
    public void run() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new StartPVPTask());
    }

}
