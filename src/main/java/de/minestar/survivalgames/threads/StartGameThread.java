package de.minestar.survivalgames.threads;

import org.bukkit.ChatColor;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.utils.Chat;

public class StartGameThread implements Runnable {

    @Override
    public void run() {
        Core.gameManager.startGame();
        Chat.broadcast(ChatColor.RED, "The games have started!");
    }

}
