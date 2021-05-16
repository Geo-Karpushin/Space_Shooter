package com.example.space_shooter.Game;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Enemy {
    public float normalSpeed = 20;
    public int maxBulets = 5;
    public float x, y;
    public float vx;
    public float vy;
    public int angle ;
    public int hp = 1000;
    public long bulletTimer;
    public boolean shootNow = false;

    public Enemy(double x, double y, double angle) {
        this.angle = (int) angle;
        this.x = (float) x;
        this.y = (float) y;
    }

    public Bitmap rotate(Bitmap source, float angleR) {
        Matrix matrix = new Matrix();
        matrix.postRotate(Float.valueOf(angleR));
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}