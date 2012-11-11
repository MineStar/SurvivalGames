package de.minestar.survivalgames.threads;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.utils.Chat;

public class StartGameTask implements Runnable {

    @Override
    public void run() {
        Chat.broadcastInfo("The games have started!");
        Core.gameManager.startGame();
    }

}
