package com.example.space_shooter.Game;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Player {
    public int shootMode = 0;
    public float x = -512f;
    public float y = -404f;
    public float vx = 0;
    public float vy = 0;
    public int angle = 0;
    public float normalSpeed = 25;
    public boolean canShoot = false;
    public int playerImg = 1;
    public int playerPreImg = 1;

    public Bitmap rotate(Bitmap source, float angleR) {
        Matrix matrix = new Matrix();
        matrix.postRotate(Float.valueOf(angleR));
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}