package de.minestar.survivalgames.threads;

import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.SurvivalGame;

public class TimerGameStart extends TimerTask {

    private final long finalTime;
    private final SurvivalGame game;

    public TimerGameStart(SurvivalGame game, long finalTime) {
        this.game = game;
        this.finalTime = finalTime;
    }

    @Override
    public void run() {
        if (this.game.isGameInPreGame()) {
            long restMilli = finalTime - System.currentTimeMillis();
            final int restSeconds = (int) (restMilli / 1000);
            final int restMinutes = (int) (restSeconds / 60);
            if (restMinutes < 1) {
                switch (restSeconds) {
                    case 0 : {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new MessageTask(this.game, ChatColor.GRAY + "The game will start in " + (restSeconds + 1) + " second..."));
                        break;
                    }
                    case 1 :
                    case 2 :
                    case 3 :
                    case 4 :
                    case 5 :
                    case 6 :
                    case 7 :
                    case 8 :
                    case 9 :
                    case 14 :
                    case 29 :
                    case 44 : {
                        if (this.game.getSettings().getPreGameTime() != restSeconds + 1) {
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new MessageTask(this.game, ChatColor.GRAY + "The game will start in " + (restSeconds + 1) + " seconds..."));
                        }
                        break;
                    }
                }
            } else {
                switch (restSeconds) {
                    case 1 : {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new MessageTask(this.game, ChatColor.GRAY + "The game will start in " + restMinutes + " minute..."));
                        break;
                    }
                    case 2 :
                    case 3 :
                    case 4 :
                    case 5 :
                    case 10 : {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new MessageTask(this.game, ChatColor.GRAY + "The game will start in " + restMinutes + " minutes..."));
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
