package de.minestar.survivalgames.threads;

import org.bukkit.ChatColor;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.utils.Chat;

public class StartDeathmatchThread implements Runnable {

    @Override
    public void run() {
        Core.gameManager.startDeathmatch();
        Chat.broadcast(ChatColor.RED, "Time for deathmatch!");
    }

}
