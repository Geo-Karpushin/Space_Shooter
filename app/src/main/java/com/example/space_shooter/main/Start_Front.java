package com.example.space_shooter.main;

public class Start_Front {
    private double speed = 0.1;
    public float x1 = 0f;
    public float x2 = -2880f;
    public float y = 0f;
    public float y1 = 0f;
    public float y2 = -1800f;
    public boolean isInMove;

    public void Proverka()
    {
        if(x1>=2880)
            x1 = -2880;
        if(x2>=2880)
            x2 = -2880;
    }

    public Start_Front(boolean move){
        isInMove = move;
    }
}
