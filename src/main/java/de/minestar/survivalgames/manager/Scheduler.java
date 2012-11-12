package de.minestar.survivalgames.manager;

import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
    private Timer timer = new Timer();

    public Scheduler() {
        this.createTimer();
    }

    private void createTimer() {
        this.timer = new Timer();
    }

    public void cancelTasks() {
        this.timer.cancel();
        this.createTimer();
    }

    public void scheduleDelayedTask(TimerTask task, long delay) {
        this.timer.schedule(task, delay);
    }

    public void scheduleDelayedRepeatingTask(TimerTask task, long startDelay, long period) {
        this.timer.scheduleAtFixedRate(task, startDelay, period);
    }

}
