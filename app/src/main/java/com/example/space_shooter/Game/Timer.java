package com.example.space_shooter.Game;

public class Timer extends Thread {
    private volatile boolean running = true;
    public long delay;

    public void requestStop() {
        running = false;
    }

    public boolean delay(long delay) throws InterruptedException {
        this.delay = delay;
        Thread.sleep(this.delay);
        return true;
    }

    @Override
    public void run() {
        while (running) {
            try{}catch (Exception e){}
        }
    }
}
