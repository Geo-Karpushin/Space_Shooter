package com.example.space_shooter;

public class Start_Front {
    private double speed = 0.1;
    float x1 = 0f;
    float x2 = -2880f;
    float y = 0f;
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