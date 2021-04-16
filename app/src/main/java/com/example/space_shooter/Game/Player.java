package com.example.space_shooter.Game;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Player {
    public float x = -512f;
    public float y = -404f;
    public float vx = 0;
    public float vy = 0;
    public int angle = 0;
    public float normal_speed = 25;

    public Bitmap rotate(Bitmap source, float angleR) {
        Matrix matrix = new Matrix();
        matrix.postRotate(Float.valueOf(angleR));
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}