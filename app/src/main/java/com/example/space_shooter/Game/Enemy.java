package com.example.space_shooter.Game;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Enemy {
    float normal_speed = 20;
    float x, y;
    float vx = normal_speed;
    float vy = normal_speed;
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
