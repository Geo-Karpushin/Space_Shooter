package com.example.space_shooter.Game;

import com.example.space_shooter.Content;

public class Explosion {
    public float eX, eY;
    public int explosionState = 0;
    public long explosionTimer;
    private long explosionInterval = 20;

    public Explosion(float x, float y, long t) {
        this.eX = x;
        this.eY = y;
        this.explosionTimer = t;
    }

    public boolean update() {
        this.eX -= Content.player.vx;
        this.eY -= Content.player.vy;
        if (System.currentTimeMillis() - this.explosionTimer > this.explosionInterval) {
            this.explosionTimer = System.currentTimeMillis();
            this.explosionState += 1;
            if (this.explosionState >= 17) {
                return true;
            }
        }
        return false;
    }
}