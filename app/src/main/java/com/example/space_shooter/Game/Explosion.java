package com.example.space_shooter.Game;

public class Explosion {
    float x;
    float y;
    int currentFrame = 0;
    int spriteWidth = 117;
    int spriteHeight = 117;
    int isVissible = true;

    long timer;

    public void Explode(){

        if (timer >= interval)
        {
            currentFrame++;
            timer = 0;
        }
        if(currentFrame == 17)
        {
            isVissible = false;
            currentFrame = 0;
        }

        rectangle = new Rectangle(currentFrame * spriteWidth, 0, spriteWidth, spriteHeight);
        origin = new Vector2(rectangle.Width / 2, rectangle.Height / 2);
    }

    private void Timer(long interval){
        if()
    }
}
