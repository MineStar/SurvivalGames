package de.minestar.survivalgames.threads;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.utils.Chat;

public class StartPVPTask implements Runnable {

    @Override
    public void run() {
        Chat.broadcastInfo("PVP is now enabled!");
        Core.gameManager.enablePVP();
    }
}
