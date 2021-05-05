package com.example.space_shooter.Game;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Bullet {
    public int damage = 10;
    public int angle;
    public float x = -512f;
    public float y = -404f;
    public float vx = 0;
    public float vy = 0;
    public int normalSpeed = 50;

    public Bullet(Integer angle, Float x, Float y){
        this.x = x;
        this.y = y;
        if(angle-90>=0) {
            this.angle = angle-90;
        }
        else{
            this.angle = 270+angle;
        }
    }
    public Bullet(Integer damage, Integer angle, Float x, Float y){
        this.x = x;
        this.y = y;
        this.damage = damage;
        if(angle-90>=0) {
            this.angle = angle-90;
        }
        else{
            this.angle = 270+angle;
        }
    }

    public Bitmap rotate(Bitmap source, float angleR) {
        Matrix matrix = new Matrix();
        matrix.postRotate(Float.valueOf(angleR));
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
