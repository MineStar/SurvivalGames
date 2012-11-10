package de.minestar.survivalgames.manager;

import java.util.Timer;

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
    }

    public void preGame() {
        this.gameState = GameState.PREGAME;
        Settings.getSpectatorSpawn().getLocation().getWorld().setTime(2000);
        Settings.getSpectatorSpawn().getLocation().getWorld().setThundering(false);
        Settings.getSpectatorSpawn().getLocation().getWorld().setStorm(false);
        Core.playerManager.preGame();
        Chat.broadcast(ChatColor.GRAY, "The games will start in " + Settings.getPreGameTime() + " minutes! Prepare!");
        timer.schedule(new StartGameThread(), Settings.getPreGameTime() * 60 * 1000);
        timer.scheduleAtFixedRate(new TimerGameStartThread(System.currentTimeMillis() + (Settings.getPreGameTime() * 60 * 1000)), 1000, 1000);

    }

    public void startGame() {
        this.gameState = GameState.PRE_PVP;
        Core.lootManager.startGame();
        Core.playerManager.startGame();
        Chat.broadcast(ChatColor.GRAY, "PVP will be enabled in " + Settings.getPrePVPTime() + " minutes!");
        timer.schedule(new StartPVPThread(), Settings.getPrePVPTime() * 60 * 1000);
        timer.scheduleAtFixedRate(new TimerPVPStartThread(System.currentTimeMillis() + (Settings.getPrePVPTime() * 60 * 1000)), 1000, 1000);
    }

    public void enablePVP() {
        this.gameState = GameState.SURVIVAL;
        Chat.broadcast(ChatColor.GRAY, "Deathmatch will start in " + Settings.getPreDeathmatchTime() + " minutes!");
        timer.schedule(new StartDeathmatchThread(), Settings.getPreDeathmatchTime() * 60 * 1000);
        timer.scheduleAtFixedRate(new TimerDeathmatchStartThread(System.currentTimeMillis() + (Settings.getPreDeathmatchTime() * 60 * 1000)), 1000, 1000);
    }

    public void startDeathmatch() {
        this.gameState = GameState.DEATHMATCH;
        Core.playerManager.startDeathmatch();
    }

    public void endGame() {
        Core.lootManager.clearChests();
        Core.playerManager.endGame();
        this.gameState = GameState.END;
    }

    public boolean isInGame() {
        return !this.gameState.equals(GameState.LOBBY);
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
}
