package com.example.space_shooter.Game;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.appcompat.app.AlertDialog;

import com.example.space_shooter.Content;
import com.example.space_shooter.R;
import com.example.space_shooter.Shop.ShopActivity;
import com.example.space_shooter.main.MainActivity;

import java.util.ArrayList;
import java.util.Random;

public class GameDrawThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private boolean firstTimeOpen = true;
    private Matrix matrixSpace;
    Matrix matrix = new Matrix();
    private Paint paint = new Paint();
    Random random = new Random();
    private Context contextGlobal;
    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();
    ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
    ArrayList<Bullet> activeBullets = new ArrayList<Bullet>();
    ArrayList<Bitmap> explosionsList = new ArrayList<Bitmap>();
    private Bitmap enemy, player, space, bullet;
    private long explosionTimer, explosionInterval = 20;
    private boolean makeExplosion = false;
    private Bitmap explosionImg;
    private float eX, eY;
    private int explosionState;
    private long bulletTimer, bulletInterval1 = 1500, bulletInterval2 = 1000, bulletInterval3 = 500;

    {
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(50.0f);
    }

    public GameDrawThread(Context context, SurfaceHolder surfaceHolder) {
        player = BitmapFactory.decodeResource(context.getResources(), getResId("player", String.valueOf(Content.player.playerImg), "drawable",context));
        enemy = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        space = BitmapFactory.decodeResource(context.getResources(), R.drawable.space3);
        enemy = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        bullet = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        explosionImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion);
        contextGlobal = context;

        for(int i = 0; i<17; i++){
            explosionsList.add(BitmapFactory.decodeResource(context.getResources(), getResId("e", String.valueOf(i), "drawable",context)));
        }

        Log.d("IMG", String.valueOf(explosionsList.size()));

        if(firstTimeOpen) {
            for (int i = 0; i <= 10; i++) {
                Player p = Content.player;
                float rx, ry;
                do {
                    rx = (float) ((Math.random() * ((2046 + 2046) + 1)) - 2046);
                    ry = (float) ((Math.random() * ((1616 + 1616) + 1)) - 1616);
                } while (Math.sqrt(Math.pow(rx - 300, 2) + Math.pow(ry - 600, 2)) < 500);
                Enemy enemy = new Enemy(rx, ry, (Math.random() * 361));
                enemyList.add(enemy);
            }
            for (int i = 0; i <= 10; i++) {
                Log.d("Enemy" + i, "x: " + String.valueOf(enemyList.get(i).x) + " y: " + String.valueOf(enemyList.get(i).y));
            }
            firstTimeOpen = false;
        }
        this.surfaceHolder = surfaceHolder;
    }

    
    public void requestStop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    int del1 = -1, del2 = -1;

                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
                    canvas.drawBitmap(space, Content.player.x, Content.player.y, backgroundPaint);
                    //matrix.postRotate(Content.player.angle);
                    // player = Bitmap.createBitmap(player, 0, 0, player.getWidth(), player.getHeight(), matrix, true);

                    for (int i = 0; i < enemyList.size(); i++) {
                        Enemy e = enemyList.get(i);

                        float d = (float)(Math.sqrt(Math.pow(e.x - 300f, 2) + Math.pow(e.y - 600f, 2)));

                        if(d>500) {
                            e.vx = e.normalSpeed * (300f - e.x) / d - Content.player.vx;
                            e.vy = e.normalSpeed * (600f - e.y) / d - Content.player.vy;
                        }
                        else {
                            e.vx = 0 - Content.player.vx;
                            e.vy = 0 - Content.player.vy;

                        }

                        if(e.angle - 90 >=0) {
                            e.angle = (int) Math.toDegrees(Math.asin(Math.abs((double) e.y - 600) / (double) d))-90;
                        }
                        else{
                            e.angle = (int) Math.toDegrees(Math.asin(Math.abs((double) e.y - 600) / (double) d))+270;
                        }

                        enemyList.set(i, e);
                    }

                    for(int i = 0; i < enemyList.size(); i++){
                        Enemy e = enemyList.get(i);

                        e.x += e.vx;
                        e.y += e.vy;
                        canvas.drawBitmap(e.rotate(enemy, e.angle), e.x, e.y, backgroundPaint);

                        enemyList.set(i, e);
                    }

                   if(Content.player.shootMode > 0 && !Content.player.canShoot) {
                       if (Content.player.shootMode == 1 && System.currentTimeMillis() - this.bulletTimer > this.bulletInterval1 && !Content.player.canShoot) {
                           this.bulletTimer = System.currentTimeMillis();
                           Bullet bullet = new Bullet(500, Content.player.angle, 300f, 600f);
                           bullet.vx = (float)(bullet.normalSpeed * Math.cos(bullet.angle));
                           bullet.vy = (float)(bullet.normalSpeed * Math.sin(bullet.angle));
                           activeBullets.add(bullet);
                       }
                       if (Content.player.shootMode == 2 && System.currentTimeMillis() - this.bulletTimer > this.bulletInterval2 && !Content.player.canShoot) {
                           this.bulletTimer = System.currentTimeMillis();
                           Bullet bullet = new Bullet(250, Content.player.angle, 300f, 600f);
                           bullet.vx = (float)(bullet.normalSpeed * Math.cos(bullet.angle));
                           bullet.vy = (float)(bullet.normalSpeed * Math.sin(bullet.angle));
                           activeBullets.add(bullet);
                       }
                       if (Content.player.shootMode == 3 && System.currentTimeMillis() - this.bulletTimer > this.bulletInterval3 && !Content.player.canShoot) {
                           this.bulletTimer = System.currentTimeMillis();
                           Bullet bullet = new Bullet(100, Content.player.angle, 300f, 600f);
                           bullet.vx = (float)(bullet.normalSpeed * Math.cos(bullet.angle));
                           bullet.vy = (float)(bullet.normalSpeed * Math.sin(bullet.angle));
                           activeBullets.add(bullet);
                       }
                   }

                    for(int i = 0; i < activeBullets.size(); i++){
                        Bullet b = activeBullets.get(i);

                        b.x += b.vx;
                        b.y += b.vy;

                        canvas.drawBitmap(b.rotate(bullet, b.angle), b.x, b.y, backgroundPaint);

                        activeBullets.set(i, b);
                    }

                    for (int i = 0; i < enemyList.size(); i++) {
                        for (int j = 0; j < enemyList.size(); j++) {
                            if (i == j) { continue; }
                            Enemy enemyThis = enemyList.get(i);
                            Enemy enemyAll = enemyList.get(j);

                            float d = (float)(Math.sqrt(Math.pow(enemyThis.x - enemyAll.x, 2) + Math.pow(enemyThis.y - enemyAll.y, 2)));
                            if (d < 250) {
                                if (!this.makeExplosion) {
                                    this.makeExplosion = true;
                                    this.explosionTimer = System.currentTimeMillis();
                                    this.eX = enemyThis.x;
                                    this.eY = enemyThis.y;
                                    this.explosionState = 0;
                                }
                                del1 = i;
                                del2 = j;
                            }
                        }
                    }

                    if(del1 >= 0 && del2 >= 0) {
                        enemyList.remove(del1);
                        enemyList.remove(del2);
                    }

                    if (this.makeExplosion) {
                        canvas.drawBitmap(explosionsList.get(explosionState), this.eX-Content.player.vx, this.eY-Content.player.vy, backgroundPaint);
                    }

                    if (System.currentTimeMillis() - this.explosionTimer > this.explosionInterval && this.makeExplosion) {
                        this.explosionTimer = System.currentTimeMillis();
                        this.explosionState += 1;
                        if (this.explosionState >= 17) {
                            this.makeExplosion = false;
                        }
                    }

                    Content.player.x += -Content.player.vx;
                    Content.player.y += -Content.player.vy;
                    canvas.drawBitmap(Content.player.rotate(player, Content.player.angle), 300f, 600f, backgroundPaint);
                    canvas.drawText(String.valueOf(Content.player.x), 50, 50, paint);
                    canvas.drawText(String.valueOf(Content.player.y), 50, 100, paint);
                    canvas.drawText(String.valueOf(Content.player.angle), 50, 250, paint);
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private int getResId(String text, String number, String directory, Context context){
        String name = text + number;
        int holderImg = context.getResources().getIdentifier(name, directory,
                context.getPackageName());
        return holderImg;
    }
}
