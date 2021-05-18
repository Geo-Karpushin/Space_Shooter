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

import androidx.annotation.NonNull;

import com.example.space_shooter.Content;
import com.example.space_shooter.R;
import com.example.space_shooter.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameDrawThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private Paint paint = new Paint();
    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();
    ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
    ArrayList<Bullet> activeBullets = new ArrayList<Bullet>();
    ArrayList<Bitmap> explosionsList = new ArrayList<Bitmap>();
    //ArrayList<Star> starList = new ArrayList<Star>(); - будущая разроботка для генерации звёзд
    private Context gContext;
    private Bitmap enemy, player, bullet, star1;
    private long bulletTimer, bulletInterval1 = 1500, bulletInterval2 = 1000, bulletInterval3 = 500;
    private ArrayList<Explosion> exps = new ArrayList<>();
    private int score = 0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
            explosionsList.add(BitmapFactory.decodeResource(context.getResources(), getResId("e", String.valueOf(i), "drawable", context))); //Подгрузка кадров взрывов
        }

        for (int i = 0; i <= 10; i++) {
            float rx, ry;
            do {
                rx = (float) ((Math.random() * ((2046 + 2046) + 1)) - 2046);
                ry = (float) ((Math.random() * ((1616 + 1616) + 1)) - 1616);
            } while (Math.sqrt(Math.pow(rx - Content.info.pX, 2) + Math.pow(ry - Content.info.pY, 2)) < 500);
            Enemy enemy = new Enemy(rx, ry, (Math.random() * 361));
            if(((int)Math.random()*100)%2==0){
                enemy.dodge = true;
            }
            enemyList.add(enemy);
        }

        Content.info.recountCoordinates();

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
                    if (Content.info.end) {
                        this.db.collection("HiScore")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        boolean add = true;
                                        for (DocumentSnapshot d : task.getResult().getDocuments()) {
                                            try {
                                                if (score > ((Integer) d.get(Content.player.name))) {
                                                    add = false;
                                                    Map<String, Integer> scores = new HashMap<>();
                                                    scores.put(Content.player.name, score);
                                                    d.getReference().set(scores);
                                                }
                                            } catch (Exception e) {}
                                        }

                                        if (add) {
                                            Map<String, Integer> scores = new HashMap<>();
                                            scores.put(Content.player.name, score);
                                            db.collection("HiScore")
                                                    .add(scores);
                                        }
                                    }
                                });

                        requestStop();
                        Intent i = new Intent(gContext, MainActivity.class);
                        gContext.startActivity(i);
                        ((Activity) gContext).finish();
                        break;
                    }

                    if(enemyList.size()<10)
                    {
                        float rx, ry;
                        rx = (float) (Math.random() * Content.info.displayWidth);
                        ry = (float) (Math.random() * Content.info.displayHeight);
                        if(Math.sqrt(Math.pow(rx - Content.info.pX, 2) + Math.pow(ry - Content.info.pY, 2)) >= 1000) {
                            Enemy enemy = new Enemy(rx, ry, (Math.random() * 359));
                            enemyList.add(enemy);
                        }
                    }

                    ArrayList<Enemy> delEnemies = new ArrayList<>();
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);

                    for (int i = 0; i < enemyList.size(); i++) {
                        Enemy e = enemyList.get(i);

                        float d = (float)(Math.sqrt(Math.pow(e.x - Content.info.pX, 2) + Math.pow(e.y - Content.info.pY, 2)));

                        if (d > 500) {
                            e.vx = e.normalSpeed * (Content.info.pX - e.x) / d - Content.player.vx;
                            e.vy = e.normalSpeed * (Content.info.pY - e.y) / d - Content.player.vy;
                            e.playerNear = false;

                            e.shootNow = false;
                        } else if(d<=300 && e.dodge) {
                            e.vx = -e.normalSpeed - Content.player.vx;
                            e.vy = -e.normalSpeed - Content.player.vy;
                            e.playerNear = true;

                            e.shootNow = true;
                        } else {
                            e.vx = 0 - Content.player.vx;
                            e.vy = 0 - Content.player.vy;

                            e.shootNow = true;
                        }

                        if(e.x<Content.info.pX && e.y < Content.info.pY)
                          e.angle = (int) -((Math.atan(Math.abs((e.y-Content.info.pY)/(e.x-Content.info.pX)))*180)/Math.PI);
                        else if(e.x<Content.info.pX && e.y >= Content.info.pY)
                            e.angle = (int) ((Math.atan(Math.abs((e.y-Content.info.pY)/(e.x-Content.info.pX)))*180)/Math.PI);
                        else if(e.x>=Content.info.pX && e.y >= Content.info.pY)
                            e.angle = (int) -((Math.atan(Math.abs((e.y-Content.info.pY)/(e.x-Content.info.pX)))*180)/Math.PI)-180;
                        else if(e.x>=Content.info.pX && e.y < Content.info.pY)
                            e.angle = (int) ((Math.atan(Math.abs((e.y-Content.info.pY)/(e.x-Content.info.pX)))*180)/Math.PI)-180;

                        e.angle = (360 - e.angle + 90) % 360;

                        enemyList.set(i, e);
                    }

                    for (int i = 0; i < enemyList.size(); i++){
                        Enemy e = enemyList.get(i);
                        e.x += e.vx;
                        e.y += e.vy;

                        if(positionCheck(e.x, e.y))
                            canvas.drawBitmap(e.rotate(enemy, e.angle), e.x, e.y, backgroundPaint);

                        if (System.currentTimeMillis() - e.bulletTimer > 1000 && e.maxBulets > 0 && e.shootNow) {
                            e.bulletTimer = System.currentTimeMillis();
                            activeBullets.add(new Bullet(250, e.angle, e.x, e.y, i));
                        }

                        if(e.maxBulets<=0){
                            exps.add(new Explosion(e.x, e.y, System.currentTimeMillis()));
                            delEnemies.add(e);
                        }

                        enemyList.set(i, e);
                    }

                    if (Content.player.shootMode > 0 && !Content.player.canShoot) {
                        if (Content.player.shootMode == 1 && System.currentTimeMillis() - this.bulletTimer > this.bulletInterval1 && !Content.player.canShoot) {
                            this.bulletTimer = System.currentTimeMillis();
                            Bullet bullet = new Bullet(500, Content.player.angle, (float)Content.info.pX, (float)Content.info.pY, -1);
                            activeBullets.add(bullet);
                        }
                        if (Content.player.shootMode == 2 && System.currentTimeMillis() - this.bulletTimer > this.bulletInterval2 && !Content.player.canShoot) {
                            this.bulletTimer = System.currentTimeMillis();
                            Bullet bullet = new Bullet(250, Content.player.angle, (float)Content.info.pX, (float)Content.info.pY, -1);
                            activeBullets.add(bullet);
                        }
                        if (Content.player.shootMode == 3 && System.currentTimeMillis() - this.bulletTimer > this.bulletInterval3 && !Content.player.canShoot) {
                            this.bulletTimer = System.currentTimeMillis();
                            Bullet bullet = new Bullet(100, Content.player.angle, (float)Content.info.pX, (float)Content.info.pY, -1);
                            activeBullets.add(bullet);
                        }
                    }

                    ArrayList<Bullet> delBullets = new ArrayList<>();
                    for (int i = 0; i < activeBullets.size(); i++){
                        Bullet b = activeBullets.get(i);

                        b.x += b.vx - Content.player.vx;
                        b.y += b.vy - Content.player.vy;

                        if (Math.abs(Content.info.pX - b.x) <= 100 && Math.abs(Content.info.pY - b.y) <= 100 && b.shooterId != -1) {
                            Content.player.hp -= b.damage;

                            delBullets.add(b);
                        }

                        else if(Math.abs(Content.info.x - b.x) > 1500)
                            delBullets.add(b);

                        if(positionCheck(b.x, b.y))
                            canvas.drawBitmap(b.rotate(bullet, b.angle), b.x+50, b.y, backgroundPaint);

                        activeBullets.set(i, b);
                    }

                    if (Double.valueOf(Content.player.hp)/100.0 <= 0) {
                        exps.add(new Explosion(Content.info.x, Content.info.y, System.currentTimeMillis()));

                        Content.info.end = true;
                    }

                    for (int i = 0; i < enemyList.size(); i++) {
                        Enemy enemyThis = enemyList.get(i);
                        for (Bullet b : activeBullets) {
                            if (Math.abs(enemyThis.x - b.x) < 100 && Math.abs(enemyThis.y - b.y) < 100 && b.shooterId != i) {
                                enemyThis.hp -= b.damage;
                                score+=1;
                                enemyList.set(i, enemyThis);

                                if (enemyThis.hp <= 0) {
                                    exps.add(new Explosion(enemyThis.x, enemyThis.y, System.currentTimeMillis()));

                                    delEnemies.add(enemyThis);
                                    score += 10;
                                }

                                delBullets.add(b);
                            }
                        }

                        for (int j = 0; j < enemyList.size(); j++) {
                            if (i == j) { continue; }
                            Enemy enemyAnother = enemyList.get(j);

                            float d = (float)(Math.sqrt(Math.pow(enemyThis.x - enemyAnother.x, 2) + Math.pow(enemyThis.y - enemyAnother.y, 2)));

                            if(d<300 && enemyThis.dodge && !enemyThis.playerNear)
                            {
                                float dP1 = (float)(Math.sqrt(Math.pow(enemyThis.x - Content.info.pX, 2) + Math.pow(enemyThis.y - Content.info.pY, 2)));
                                float dP2 = (float)(Math.sqrt(Math.pow(enemyAnother.x - Content.info.pX, 2) + Math.pow(enemyAnother.y - Content.info.pY, 2)));

                                if(dP1 > dP2 && enemyThis.dodge){
                                    enemyThis.normalSpeed = 0;
                                    enemyThis.normalSpeed = 0;
                                    enemyThis.obstacleExists = true;
                                    if(d<enemyThis.obstacleMinDistance || enemyThis.obstacleMinDistance==-1){
                                        enemyThis.obstacleId = j;
                                    }
                                }
                            }
                            else if(d<450 && enemyThis.dodge && !enemyThis.playerNear)
                            {
                                float dP1 = (float)(Math.sqrt(Math.pow(enemyThis.x - Content.info.pX, 2) + Math.pow(enemyThis.y - Content.info.pY, 2)));
                                float dP2 = (float)(Math.sqrt(Math.pow(enemyAnother.x - Content.info.pX, 2) + Math.pow(enemyAnother.y - Content.info.pY, 2)));

                                if(dP1 > dP2 && enemyThis.dodge){
                                    enemyThis.normalSpeed = 12;
                                    enemyThis.normalSpeed = 12;
                                    enemyThis.obstacleExists = true;
                                    if(d<enemyThis.obstacleMinDistance || enemyThis.obstacleMinDistance==-1){
                                        enemyThis.obstacleId = j;
                                    }
                                }
                            }
                            else if(d>300 && enemyThis.dodge && !enemyThis.playerNear){
                                if(enemyThis.obstacleExists && enemyThis.obstacleId == j) {
                                    enemyThis.normalSpeed = 25;
                                    enemyThis.normalSpeed = 25;
                                }
                            }

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

                        if(positionCheck(e.eX, e.eY))
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

                    if(positionCheck(Content.info.pX, Content.info.pY))
                        canvas.drawBitmap(Content.player.rotate(player, Content.player.angle), Content.info.pX, Content.info.pY, backgroundPaint);

                    canvas.drawText("Здоровье: "+String.valueOf(Double.valueOf(Content.player.hp)/100.0), Content.info.displayWidth/2-50, 50, paint);
                    canvas.drawText("Счёт: "+String.valueOf(score), 50, 50, paint);
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

    private boolean positionCheck(float x, float y){
        boolean result = false;
        if(x>-100 && x<Content.info.displayWidth+100 && y>-100 && y<Content.info.displayHeight+100)
            result = true;
        return result;
    }
}