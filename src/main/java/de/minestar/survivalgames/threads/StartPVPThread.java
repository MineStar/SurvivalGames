package de.minestar.survivalgames.threads;

import org.bukkit.ChatColor;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.utils.Chat;

public class StartPVPThread implements Runnable {

    @Override
    public void run() {
        Core.gameManager.enablePVP();
        Chat.broadcast(ChatColor.RED, "PVP is now enabled!");
    }

}
