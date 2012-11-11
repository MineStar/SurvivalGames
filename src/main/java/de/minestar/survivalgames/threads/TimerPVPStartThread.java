package de.minestar.survivalgames.threads;

import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.Settings;

public class TimerPVPStartThread extends TimerTask {

    private final long finalTime;

    public TimerPVPStartThread(long finalTime) {
        this.finalTime = finalTime;
    }

    @Override
    public void run() {
        if (Core.gameManager.isInGame()) {
            long restMilli = finalTime - System.currentTimeMillis();
            final int restSeconds = (int) (restMilli / 1000);
            final int restMinutes = (int) (restSeconds / 60);
            if (restMinutes < 1) {
                switch (restSeconds) {
                    case 14 :
                    case 29 :
                    case 44 : {
                        if (Settings.getPrePVPTime() != restSeconds + 1) {
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new MessageTask(ChatColor.GRAY + "PVP will be enabled in " + (restSeconds + 1) + " seconds..."));
                        }
                        break;
                    }
                }
            } else {
                switch (restSeconds) {
                    case 1 : {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new MessageTask(ChatColor.GRAY + "PVP will be enabled in " + restMinutes + " minute..."));
                        break;
                    }
                    case 2 :
                    case 3 :
                    case 4 :
                    case 5 : {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new MessageTask(ChatColor.GRAY + "PVP will be enabled in " + restMinutes + " minutes..."));
                        break;
                    }
                }
            }
            if (restSeconds < 1) {
                this.cancel();
            }
        } else {
            this.cancel();
        }
    }
}
