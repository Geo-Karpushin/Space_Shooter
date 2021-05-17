package com.example.space_shooter;

public class GameInfo {
    public boolean dataBaseOn = false;
    public float x = 0f;
    public float y = 0f;
    public int displayWidth;
    public int displayHeight;
    public int pX;
    public int pY;
    public boolean pause = false;
    public boolean end = false;
    public long endGameTimer;

    public void recountCoordinates(){
        pX = (displayWidth/2)-100;
        pY = (displayHeight/2)-500;
    }
}
