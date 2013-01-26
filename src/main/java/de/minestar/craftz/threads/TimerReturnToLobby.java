package de.minestar.craftz.threads;

import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import de.minestar.craftz.Core;
import de.minestar.craftz.data.SurvivalGame;

public class TimerReturnToLobby extends TimerTask {

    private final long finalTime;
    private final SurvivalGame game;

    public TimerReturnToLobby(SurvivalGame game, long finalTime) {
        this.game = game;
        this.finalTime = finalTime;
    }

    @Override
    public void run() {
        if (game.isGameInEnd()) {
            long restMilli = finalTime - System.currentTimeMillis();
            final int restSeconds = (int) (restMilli / 1000);
            final int restMinutes = (int) (restSeconds / 60);
            if (restMinutes < 1) {
                switch (restSeconds) {
                    case 0 : {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new MessageTask(this.game, ChatColor.GRAY + "The game will return to the lobby in " + (restSeconds + 1) + " second..."));
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
                        if (game.getSettings().getPreDeathmatchTime() != restSeconds + 1) {
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new MessageTask(this.game, ChatColor.GRAY + "The game will return to the lobby in " + (restSeconds + 1) + " seconds..."));
                        }
                        break;
                    }
                }
            } else {
                switch (restSeconds) {
                    case 1 : {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new MessageTask(this.game, ChatColor.GRAY + "The game will return to the lobby in " + restMinutes + " minute..."));
                        break;
                    }
                    case 5 :
                    case 10 :
                    case 15 :
                    case 20 :
                    case 25 :
                    case 30 :
                    case 45 : {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, new MessageTask(this.game, ChatColor.GRAY + "The game will return to the lobby in " + restMinutes + " minutes..."));
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
