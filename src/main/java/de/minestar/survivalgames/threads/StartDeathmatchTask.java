package de.minestar.survivalgames.threads;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.utils.Chat;

public class StartDeathmatchTask implements Runnable {

    @Override
    public void run() {
        Chat.broadcastInfo("Time for deathmatch!");
        Core.gameManager.startDeathmatch();
    }

}
