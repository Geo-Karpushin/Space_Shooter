package com.example.space_shooter.Game;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.example.space_shooter.Content;

public class Bullet {
    public int damage = 10;
    public int angle;
    public float x = -512f;
    public float y = -404f;
    public float vx = 0;
    public float vy = 0;
    public int normalSpeed = 50;
    public long createTime;
    public boolean enemyBullet;

    public Bullet(Integer damage, Integer angle, Float x, Float y, boolean enemyBullet){
        this.x = x;
        this.y = y;
        this.enemyBullet = enemyBullet;
        this.damage = damage;
        this.vx = (float)(this.normalSpeed * Math.cos(Math.toRadians((360 - angle + 90) % 360)));
        this.vy = -(float)(this.normalSpeed * Math.sin(Math.toRadians((360 - angle + 90) % 360)));
        if(angle-90>=0) {
            this.angle = angle-90;
        }
        else{
            this.angle = 270+angle;
        }
        this.createTime = System.currentTimeMillis();
    }

    public Bitmap rotate(Bitmap source, float angleR) {
        Matrix matrix = new Matrix();
        matrix.postRotate(Float.valueOf(angleR));
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}