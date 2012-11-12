package de.minestar.survivalgames.threads;

import de.minestar.survivalgames.data.SurvivalGame;

public class MessageTask implements Runnable {

    private String message;
    private final SurvivalGame game;

    public MessageTask(SurvivalGame game, String message) {
        this.game = game;
        this.message = message;
    }

    @Override
    public void run() {
        this.game.broadcast(message);
    }
}
