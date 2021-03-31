package com.example.space_shooter;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Player {
    float x = -512f;
    float y = -404f;
    float vx = 0;
    float vy = 0;
    int angle = 0;
    float normal_speed = 25;

    public Bitmap rotate(Bitmap source, float angleR) {
        Matrix matrix = new Matrix();
        matrix.postRotate(Float.valueOf(angleR));
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}