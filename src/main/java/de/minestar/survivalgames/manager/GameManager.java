package de.minestar.survivalgames.manager;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.ChatColor;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.GameState;
import de.minestar.survivalgames.data.Settings;
import de.minestar.survivalgames.threads.StartDeathmatchThread;
import de.minestar.survivalgames.threads.StartGameThread;
import de.minestar.survivalgames.threads.StartPVPThread;
import de.minestar.survivalgames.threads.TimerDeathmatchStartThread;
import de.minestar.survivalgames.threads.TimerGameStartThread;
import de.minestar.survivalgames.threads.TimerPVPStartThread;
import de.minestar.survivalgames.utils.Chat;

public class GameManager {

    private GameState gameState = GameState.LOBBY;
    private static Timer timer = new Timer();

    public void onEnable() {

    }

    public void onDisable() {
        timer.cancel();
        timer = new Timer();
    }

    public void scheduleDelayedTask(TimerTask task, long delay) {
        timer.schedule(task, delay);
    }

    public void scheduleDelayedRepeatingTask(TimerTask task, long startDelay, long period) {
        timer.scheduleAtFixedRate(task, startDelay, period);
    }

    private String secondsToMinutes(long seconds) {
        long mins = seconds / 60;
        long secs = seconds % 60;
        String text = "";
        if (mins > 0) {
            text += mins + " minute";
            if (mins != 1) {
                text += "s";
            }

        }
        if (secs > 0) {
            text += " and ";
            text += secs + " second";
            if (secs != 1) {
                text += "s";
            }
        }

        return text;
    }

    public void preGame() {
        this.gameState = GameState.PREGAME;
        Settings.getSpectatorSpawn().getLocation().getWorld().setTime(2000);
        Settings.getSpectatorSpawn().getLocation().getWorld().setThundering(false);
        Settings.getSpectatorSpawn().getLocation().getWorld().setStorm(false);
        Core.playerManager.preGame();
        Chat.broadcastInfo("The games will start in " + this.secondsToMinutes(Settings.getPreGameTime()) + "! Prepare!");
        this.scheduleDelayedTask(new StartGameThread(), Settings.getPreGameTime() * 1000);
        this.scheduleDelayedRepeatingTask(new TimerGameStartThread(System.currentTimeMillis() + ((Settings.getPreGameTime()) * 1000)), 1000, 1001);
    }

    public void startGame() {
        this.gameState = GameState.PRE_PVP;
        Core.lootManager.startGame();
        Core.playerManager.startGame();
        Chat.broadcastInfo("PVP will be enabled in " + this.secondsToMinutes(Settings.getPrePVPTime()) + "!");
        this.scheduleDelayedTask(new StartPVPThread(), Settings.getPrePVPTime() * 1000);
        this.scheduleDelayedRepeatingTask(new TimerPVPStartThread(System.currentTimeMillis() + ((Settings.getPrePVPTime()) * 1000)), 1000, 1001);
    }

    public void enablePVP() {
        this.gameState = GameState.SURVIVAL;
        Chat.broadcastInfo("Deathmatch will start in " + this.secondsToMinutes(Settings.getPreDeathmatchTime()) + "!");
        this.scheduleDelayedTask(new StartDeathmatchThread(), Settings.getPreDeathmatchTime() * 1000);
        this.scheduleDelayedRepeatingTask(new TimerDeathmatchStartThread(System.currentTimeMillis() + ((Settings.getPreDeathmatchTime()) * 1000)), 1000, 1001);
    }

    public void startDeathmatch() {
        this.gameState = GameState.DEATHMATCH;
        Core.playerManager.startDeathmatch();
    }

    public void endGame() {
        Core.lootManager.clearChests();
        Core.playerManager.endGame();
        this.gameState = GameState.END;
        this.onDisable();
    }

    public void stopGame() {
        Core.lootManager.clearChests();
        Core.playerManager.endGame();
        this.gameState = GameState.LOBBY;
        this.onDisable();
    }

    public boolean isInGame() {
        return !this.gameState.equals(GameState.LOBBY) && !this.gameState.equals(GameState.END);
    }

    public boolean isInPreGame() {
        return this.gameState.equals(GameState.PREGAME);
    }

    public boolean isInDeathmatch() {
        return this.gameState.equals(GameState.DEATHMATCH);
    }

    public boolean isPVPEnabled() {
        return this.gameState.equals(GameState.SURVIVAL) || this.gameState.equals(GameState.DEATHMATCH);
    }

    public void checkForWinner() {
        // send infomessage
        if (Core.playerManager.hasGameAWinner()) {
            Chat.broadcast(ChatColor.RED, "--------------------------------------------");
            Chat.broadcast(ChatColor.RED, "The games have ended!");
            Chat.broadcast(ChatColor.GOLD, "'" + Core.playerManager.getWinner() + "' is the winner!");
            Chat.broadcast(ChatColor.RED, "--------------------------------------------");
            Core.gameManager.endGame();
            return;
        } else if (Core.playerManager.hasGameADraw()) {
            Chat.broadcast(ChatColor.RED, "--------------------------------------------");
            Chat.broadcast(ChatColor.RED, "The games have ended!");
            Chat.broadcast(ChatColor.GOLD, "Noone has survived... :{");
            Chat.broadcast(ChatColor.RED, "--------------------------------------------");
            Core.gameManager.endGame();
            return;
        }
    }
}
