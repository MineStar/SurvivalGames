package de.minestar.survivalgames.threads;

import de.minestar.survivalgames.utils.Chat;

public class MessageTask implements Runnable {

    private String message;

    public MessageTask(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        Chat.broadcast(message);
    }
}
