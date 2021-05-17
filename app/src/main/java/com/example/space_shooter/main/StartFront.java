package com.example.space_shooter.main;

public class StartFront {
    public float x1 = 0f;
    public float x2 = -2880f;
    public boolean isInMove;

    public void Check()
    {
        if(x1>=2880)
            x1 = -2880;
        if(x2>=2880)
            x2 = -2880;
    }

    public StartFront(boolean move){
        isInMove = move;
    }
}
