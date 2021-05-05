package com.example.space_shooter.Game;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Enemy {
    float normalSpeed = 20;
    int maxBulets = 5;

    float x, y;
    float vx;
    float vy;
    int angle = 0;

    public Enemy(double x, double y, double angle)
    {
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
