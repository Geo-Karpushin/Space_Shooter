package com.example.space_shooter;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Player {
    float x = -512f;
    float y = -404f;
    int angle = 90;
    int angle1 = 90;
    float speed = 0;
    float normal_speed = 10;

    public Bitmap rotate(Bitmap source, float angleR) {
        Matrix matrix = new Matrix();
        matrix.postRotate(Float.valueOf(angleR));
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}