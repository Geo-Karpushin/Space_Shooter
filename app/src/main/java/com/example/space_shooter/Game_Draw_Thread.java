package com.example.space_shooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.LinkedList;
import java.util.Random;

public class Game_Draw_Thread extends Thread {
    private SurfaceHolder surfaceHolder;
    private Matrix matrixSpace;
    Matrix matrix = new Matrix();
    private Paint pt = new Paint();
    Random random = new Random();
    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();
    LinkedList<Enemy> enemyList = new LinkedList<Enemy>();
    private Bitmap enemy, player, space;
    {
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);
        pt.setColor(Color.WHITE);
        pt.setTextSize(50.0f);
    }

    public Game_Draw_Thread(Context context, SurfaceHolder surfaceHolder) {
        player = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        enemy = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        space = BitmapFactory.decodeResource(context.getResources(), R.drawable.space);
        enemy = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        for(int i = 0; i <= 10; i++){
            float rx, ry;
            do{
               rx = (float) ((Math.random()*((2046+2046)+1))-2046);
               ry = (float) ((Math.random()*((1616+1616)+1))-1616);
            }while(Math.sqrt((rx - Content.player.x)*(rx - Content.player.x) + (ry - Content.player.y)*(ry - Content.player.y))<100);
            Enemy enemy = new Enemy(rx, ry, (Math.random()*361));
            enemyList.add(enemy);
        }
        for(int i = 0; i <= 10; i++){
            Log.d("Enemy" + i, "x: " + String.valueOf(enemyList.get(i).x) + " y: " + String.valueOf(enemyList.get(i).y));
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

                    for (int i = 0; i < 10; i++) {
                        Enemy e = enemyList.get(i);

                        float d = (float) Math.sqrt(Math.pow(e.x - Content.player.x, 2) + Math.pow(e.y - Content.player.y, 2));
                        e.vx = e.normal_speed * (Content.player.x - e.x) / d;
                        e.vy = e.normal_speed * (Content.player.y - e.y) / d;

                        enemyList.set(i, e);
                    }

                    for(int i = 0; i <= 10; i++){
                        Enemy e = enemyList.get(i);

                        e.x += -e.vx;
                        e.y += -e.vy;
                        canvas.drawBitmap(e.rotate(enemy, e.angle), e.x, e.y, backgroundPaint);

                        enemyList.set(i, e);
                    }
                    Content.player.x += -Content.player.vx;
                    Content.player.y += -Content.player.vy;
                    canvas.drawBitmap(Content.player.rotate(player, Content.player.angle), 300, 600, backgroundPaint);
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
