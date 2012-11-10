package de.minestar.survivalgames.threads;

import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import de.minestar.survivalgames.Core;

public class TimerGameStartThread extends TimerTask {

    private final long finalTime;

    public TimerGameStartThread(long finalTime) {
        this.finalTime = finalTime;
    }

    @Override
    public void run() {
        long restMilli = finalTime - System.currentTimeMillis();
        final int restSeconds = (int) (restMilli / 1000);
        final int restMinutes = (int) (restSeconds / 60);
        if (restMinutes < 1) {
            switch (restSeconds) {
                case 1 : {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new MessageTask(ChatColor.GOLD + "[INFO] The games will start in " + restSeconds + " second..."));
                    break;
                }
                case 2 :
                case 3 :
                case 4 :
                case 5 :
                case 6 :
                case 7 :
                case 8 :
                case 9 :
                case 10 :
                case 15 :
                case 30 :
                case 45 : {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new MessageTask(ChatColor.GOLD + "[INFO] The games will start in " + restSeconds + " seconds..."));
                    break;
                }
            }
        } else {
            switch (restSeconds) {
                case 1 : {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new MessageTask(ChatColor.GOLD + "[INFO] The games will start in " + restMinutes + " minute..."));
                    break;
                }
                case 2 :
                case 3 :
                case 4 :
                case 5 :
                case 10 : {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new MessageTask(ChatColor.GOLD + "[INFO] The games will start in " + restMinutes + " minutes..."));
                    break;
                }
            }
        }
        if (restSeconds < 1) {
            this.cancel();
        }
    }

}
