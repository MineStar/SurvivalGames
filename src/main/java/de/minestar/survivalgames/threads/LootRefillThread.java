package de.minestar.survivalgames.threads;

import java.util.TimerTask;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.utils.Chat;

public class LootRefillThread extends TimerTask {

    @Override
    public void run() {
        Core.lootManager.refillChests();
        Chat.broadcastInfo("All chests have been refilled!");
    }
}
