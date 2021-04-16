package com.example.space_shooter.Game;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.space_shooter.Game.Game_Draw_Thread;

public class Game_Draw extends SurfaceView implements SurfaceHolder.Callback {
    private Game_Draw_Thread drawThread;

    public Game_Draw(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public Game_Draw(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public Game_Draw(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new Game_Draw_Thread(getContext(),getHolder());
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // изменение размеров SurfaceView
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.requestStop();
        boolean retry = true;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                //
            }
        }
    }
}
