package com.example.space_shooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.Random;

public class Game_Draw_Thread extends Thread {
    private SurfaceHolder surfaceHolder;
    private Matrix matrixSpace;
    Matrix matrix = new Matrix();
    private Paint pt = new Paint();
    Random random = new Random();
    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();
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
        this.surfaceHolder = surfaceHolder;
    }

    public void requestStop() {
        running = false;
    }

    @Override
    public void run() {
        float spaceX = 0;
        float spaceY = 0;
        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
                    canvas.drawBitmap(space, Content.player.x, Content.player.y, backgroundPaint);
                    //matrix.postRotate(Content.player.angle);
                   // player = Bitmap.createBitmap(player, 0, 0, player.getWidth(), player.getHeight(), matrix, true);
                    Content.player.x += Content.player.speed;
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
