package com.example.space_shooter.Game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import com.example.space_shooter.Content;
import com.example.space_shooter.R;
import com.example.space_shooter.main.MainActivity;

import java.util.ArrayList;

public class GameDrawThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private Paint paint = new Paint();
    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();
    ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
    ArrayList<Bullet> activeBullets = new ArrayList<Bullet>();
    ArrayList<Bitmap> explosionsList = new ArrayList<Bitmap>();
    ArrayList<Star> starList = new ArrayList<Star>();
    private Context gContext;
    private Bitmap enemy, player, bullet, star1;
    private long bulletTimer, bulletInterval1 = 1500, bulletInterval2 = 1000, bulletInterval3 = 500;
    private ArrayList<Explosion> exps = new ArrayList<>();
    private int pX = (Content.info.displayWidth/2)-100, pY = (Content.info.displayHeight/2)-500;

    public GameDrawThread(Context context, SurfaceHolder surfaceHolder) {
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(50.0f);

        player = BitmapFactory.decodeResource(context.getResources(), getResId("player", String.valueOf(Content.player.playerImg), "drawable", context));
        enemy = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        bullet = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        star1 =  BitmapFactory.decodeResource(context.getResources(), R.drawable.star1);

        this.gContext = context;

        for (int i = 0; i < 17; i++) {
            explosionsList.add(BitmapFactory.decodeResource(context.getResources(), getResId("e", String.valueOf(i), "drawable", context)));
        }

        for (int i = 0; i <= 10; i++) {
            float rx, ry;
            do {
                rx = (float) ((Math.random() * ((2046 + 2046) + 1)) - 2046);
                ry = (float) ((Math.random() * ((1616 + 1616) + 1)) - 1616);
            } while (Math.sqrt(Math.pow(rx - pX, 2) + Math.pow(ry - pY, 2)) < 500);
            Enemy enemy = new Enemy(rx, ry, (Math.random() * 361));
            enemyList.add(enemy);
        }

        //for(int i = 0; i < ((Math.random() * ((2046 + 2046) + 1)) - 2046)){

       // }
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
                    if (Content.info.end && System.currentTimeMillis() - Content.info.endGameTimer > 3000) {
                        running = false;
                        Intent i = new Intent(gContext, MainActivity.class);
                        gContext.startActivity(i);
                        ((Activity) gContext).finish();
                        break;
                    }

                    ArrayList<Enemy> delEnemies = new ArrayList<>();
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);

                    for (int i = 0; i < enemyList.size(); i++) {
                        Enemy e = enemyList.get(i);

                        float d = (float)(Math.sqrt(Math.pow(e.x - pX, 2) + Math.pow(e.y - pY, 2)));

                        if (d > 500) {
                            e.vx = e.normalSpeed * (pX - e.x) / d - Content.player.vx;
                            e.vy = e.normalSpeed * (pY - e.y) / d - Content.player.vy;

                            e.shootNow = false;
                        } else {
                            e.vx = 0 - Content.player.vx;
                            e.vy = 0 - Content.player.vy;

                            e.shootNow = true;
                        }

                       // e.angle = (int) Math.toDegrees(Math.atan2(Content.player.x - e.x, Content.player.y > e.y ? e.y - Content.player.y : Content.player.y - e.y));

                        if(e.x<pX && e.y < pY)
                          e.angle = (int) -((Math.atan(Math.abs((e.y-pY)/(e.x-pX)))*180)/Math.PI);
                        else if(e.x<pX && e.y >= pY)
                            e.angle = (int) ((Math.atan(Math.abs((e.y-pY)/(e.x-pX)))*180)/Math.PI);
                        else if(e.x>=pX && e.y >= pY)
                            e.angle = (int) -((Math.atan(Math.abs((e.y-pY)/(e.x-pX)))*180)/Math.PI)-180;
                        else if(e.x>=pX && e.y < pY)
                            e.angle = (int) ((Math.atan(Math.abs((e.y-pY)/(e.x-pX)))*180)/Math.PI)-180;
                        //e.angle = -45;

                        if (e.angle < 0){
                            e.angle += 360;
                        }

                        e.angle = (360 - e.angle + 90) % 360;

                        enemyList.set(i, e);
                    }

                    for (int i = 0; i < enemyList.size(); i++){
                        Enemy e = enemyList.get(i);

                        e.x += e.vx;
                        e.y += e.vy;
                        canvas.drawBitmap(e.rotate(enemy, e.angle), e.x, e.y, backgroundPaint);

                        if (System.currentTimeMillis() - e.bulletTimer > 1000 && e.maxBulets > 0 && e.shootNow) {
                            e.bulletTimer = System.currentTimeMillis();
                            e.maxBulets -= 1;
                            activeBullets.add(new Bullet(250, e.angle, e.x, e.y, true));
                        }

                        enemyList.set(i, e);
                    }

                    if (Content.player.shootMode > 0 && !Content.player.canShoot) {
                        if (Content.player.shootMode == 1 && System.currentTimeMillis() - this.bulletTimer > this.bulletInterval1 && !Content.player.canShoot) {
                            this.bulletTimer = System.currentTimeMillis();
                            Bullet bullet = new Bullet(500, Content.player.angle, (float)pX, (float)pY, false);
                            activeBullets.add(bullet);
                        }
                        if (Content.player.shootMode == 2 && System.currentTimeMillis() - this.bulletTimer > this.bulletInterval2 && !Content.player.canShoot) {
                            this.bulletTimer = System.currentTimeMillis();
                            Bullet bullet = new Bullet(250, Content.player.angle, (float)pX, (float)pY, false);
                            activeBullets.add(bullet);
                        }
                        if (Content.player.shootMode == 3 && System.currentTimeMillis() - this.bulletTimer > this.bulletInterval3 && !Content.player.canShoot) {
                            this.bulletTimer = System.currentTimeMillis();
                            Bullet bullet = new Bullet(100, Content.player.angle, (float)pX, (float)pY, false);
                            activeBullets.add(bullet);
                        }
                    }

                    ArrayList<Bullet> delBullets = new ArrayList<>();
                    for (int i = 0; i < activeBullets.size(); i++){
                        Bullet b = activeBullets.get(i);

                        b.x += b.vx - Content.player.vx;
                        b.y += b.vy - Content.player.vy;

                        if (System.currentTimeMillis() - b.createTime > 5000) {
                            delBullets.add(b);
                        }

                        if (Math.abs(Content.info.x - b.x) < 200 && Math.abs(Content.info.y - b.y) < 200) {
                            Content.player.hp -= b.damage;
                            if (Content.player.hp <= 0) {
                                exps.add(new Explosion(Content.info.x, Content.info.y, System.currentTimeMillis()));

                                Content.info.end = true;
                                Content.info.endGameTimer = System.currentTimeMillis();
                            }

                            delBullets.add(b);
                        }

                        canvas.drawBitmap(b.rotate(bullet, b.angle), b.x, b.y, backgroundPaint);
                        activeBullets.set(i, b);
                    }

                    for (int i = 0; i < enemyList.size(); i++) {
                        Enemy e = enemyList.get(i);
                        for (Bullet b : activeBullets) {
                            if (Math.abs(e.x - b.x) < 150 && Math.abs(e.y - b.y) < 150 && !b.enemyBullet) {
                                e.hp -= b.damage;
                                enemyList.set(i, e);

                                if (e.hp <= 0) {
                                    exps.add(new Explosion(e.x, e.y, System.currentTimeMillis()));

                                    delEnemies.add(e);
                                }

                                delBullets.add(b);
                            }
                        }

                        for (int j = 0; j < enemyList.size(); j++) {
                            if (i == j) { continue; }
                            Enemy enemyThis = enemyList.get(i);
                            Enemy enemyAnother = enemyList.get(j);

                            float d = (float)(Math.sqrt(Math.pow(enemyThis.x - enemyAnother.x, 2) + Math.pow(enemyThis.y - enemyAnother.y, 2)));
                            if (d < 250) {
                                exps.add(new Explosion(enemyThis.x, enemyThis.y, System.currentTimeMillis()));

                                delEnemies.add(enemyThis);
                                delEnemies.add(enemyAnother);
                            }
                        }
                    }

                    ArrayList<Explosion> delExps = new ArrayList<>();
                    for (int i = 0; i < exps.size(); i++) {
                        Explosion e = exps.get(i);
                        if (e.update()) {
                            delExps.add(e);
                            continue;
                        }
                        exps.set(i, e);

                        canvas.drawBitmap(explosionsList.get(e.explosionState), e.eX, e.eY, backgroundPaint);
                    }

                    for (Explosion e : delExps) {
                        exps.remove(e);
                    }
                    for (Enemy e : delEnemies) {
                        enemyList.remove(e);
                    }
                    for (Bullet b : delBullets) {
                        activeBullets.remove(b);
                    }

                    Content.info.x -= Content.player.vx;
                    Content.info.y -= Content.player.vy;
                    canvas.drawBitmap(Content.player.rotate(player, Content.player.angle), (float)pX, (float)pY, backgroundPaint);
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