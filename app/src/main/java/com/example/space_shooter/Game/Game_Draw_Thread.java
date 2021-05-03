package com.example.space_shooter.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.space_shooter.Content;
import com.example.space_shooter.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Game_Draw_Thread extends Thread {
    private SurfaceHolder surfaceHolder;
    private boolean first_time_open = true;
    private Matrix matrixSpace;
    Matrix matrix = new Matrix();
    private Paint pt = new Paint();
    Random random = new Random();
    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();
    ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
    ArrayList<Bullet> activeBullets = new ArrayList<Bullet>();
    ArrayList<Bitmap> explosionsList = new ArrayList<Bitmap>();
    private Bitmap enemy, player, space, bullet;
    private long explosion_timer, explosion_interval = 20;
    private boolean make_explosion = false;
    private Bitmap explosion_img;
    private float explosion_x, explosion_y;
    private int explosion_state;

    {
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);
        pt.setColor(Color.WHITE);
        pt.setTextSize(50.0f);
    }

    public Game_Draw_Thread(Context context, SurfaceHolder surfaceHolder) {
        player = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        enemy = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        space = BitmapFactory.decodeResource(context.getResources(), R.drawable.space3);
        enemy = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        bullet = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        explosion_img = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion);

        for(int i = 0; i<17; i++){
            String name = "e" + String.valueOf(i);
            int holderint = context.getResources().getIdentifier(name, "drawable",
                    context.getPackageName());
            explosionsList.add(BitmapFactory.decodeResource(context.getResources(), holderint));
            Log.d("IMG", String.valueOf(i));
        }

        Log.d("IMG", String.valueOf(explosionsList.size()));

        if(first_time_open) {
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
            first_time_open = false;
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
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
                    canvas.drawBitmap(space, Content.player.x, Content.player.y, backgroundPaint);
                    //matrix.postRotate(Content.player.angle);
                    // player = Bitmap.createBitmap(player, 0, 0, player.getWidth(), player.getHeight(), matrix, true);

                    for (int i = 0; i < enemyList.size(); i++) {
                        Enemy e = enemyList.get(i);

                        float d = (float)(Math.sqrt(Math.pow(e.x - 300f, 2) + Math.pow(e.y - 600f, 2)));

                        if(d>500) {
                            e.vx = e.normal_speed * (300f - e.x) / d - Content.player.vx;
                            e.vy = e.normal_speed * (600f - e.y) / d - Content.player.vy;
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

                    for (Enemy enemyThis : enemyList) {
                        for (Enemy enemyAll : enemyList) {
                            float d = (float)(Math.sqrt(Math.pow(enemyThis.x - enemyAll.x, 2) + Math.pow(enemyThis.y - enemyAll.y, 2)));
                            if (d <= 100){
                            }
                        }
                    }

                    int del1 = -1, del2 = -1;

                    for (int i = 0; i < enemyList.size(); i++) {
                        for (int j = 0; j < enemyList.size(); j++) {
                            if (i == j) { continue; }
                            Enemy enemyThis = enemyList.get(i);
                            Enemy enemyAll = enemyList.get(j);

                            del1 = i;
                            del2 = j;

                            float d = (float)(Math.sqrt(Math.pow(enemyThis.x - enemyAll.x, 2) + Math.pow(enemyThis.y - enemyAll.y, 2)));
                            if (d < 500) {
                                if (!this.make_explosion) {
                                    this.make_explosion = true;
                                    this.explosion_timer = System.currentTimeMillis();
                                    this.explosion_x = enemyThis.x;
                                    this.explosion_y = enemyThis.y;
                                    this.explosion_state = 0;
                                }
                            }
                        }
                    }

                    if(del1 >= 0 && del2 >= 0) {
                        enemyList.remove(del1);
                        enemyList.remove(del2);
                    }

                    if (this.make_explosion) {
                        canvas.drawBitmap(explosionsList.get(explosion_state), this.explosion_x-Content.player.vx, this.explosion_y-Content.player.vy, backgroundPaint);
                    }

                    for(int i = 0; i < enemyList.size(); i++){
                        Enemy e = enemyList.get(i);

                        e.x += e.vx;
                        e.y += e.vy;
                        canvas.drawBitmap(e.rotate(enemy, e.angle), e.x, e.y, backgroundPaint);

                        enemyList.set(i, e);
                    }

                    if (System.currentTimeMillis() - this.explosion_timer > this.explosion_interval && this.make_explosion) {
                        this.explosion_timer = System.currentTimeMillis();
                        this.explosion_state += 1;
                        if (this.explosion_state >= 17) {
                            this.make_explosion = false;
                        }
                    }

                  /*  if(Content.player.shoot_mode > 0 && !Content.player.canShoot){
                        if(Content.player.shoot_mode == 1){
                            Content.player.canShoot = Content.timer.delay(500);
                        }
                        if(Content.player.shoot_mode == 2){
                            Content.player.canShoot = Content.timer.delay(250);
                        }
                        if(Content.player.shoot_mode == 3){
                            Content.player.canShoot = Content.timer.delay(100);
                        }
                    }

                    if(Content.player.canShoot){
                        Bullet bullet = new Bullet(100, Content.player.angle, 300f, 600f);
                        activeBullets.add(bullet);
                    }

                    for(int i = 0; i < activeBullets.size(); i++){
                        Bullet bullet = activeBullets.get(i);

                        bullet.x +=
                    }*/

                    Content.player.x += -Content.player.vx;
                    Content.player.y += -Content.player.vy;
                    canvas.drawBitmap(Content.player.rotate(player, Content.player.angle), 300f, 600f, backgroundPaint);
                    canvas.drawText(String.valueOf(Content.player.x), 50, 50, pt);
                    canvas.drawText(String.valueOf(Content.player.y), 50, 100, pt);
                    canvas.drawText(String.valueOf(Content.player.angle), 50, 250, pt);
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
