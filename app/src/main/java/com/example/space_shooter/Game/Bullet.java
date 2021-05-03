package com.example.space_shooter.Game;

public class Bullet {
    public int damage = 10;
    public int angle;
    public float x = -512f;
    public float y = -404f;
    public float vx = 0;
    public float vy = 0;

    public Bullet(Integer angle, Float x, Float y){
        this.x = x;
        this.y = y;
        if(angle-90>=0) {
            this.angle = angle-90;
        }
        else{
            this.angle = 270+angle;
        }
    }
    public Bullet(Integer damage, Integer angle, Float x, Float y){
        this.x = x;
        this.y = y;
        this.damage = damage;
        if(angle-90>=0) {
            this.angle = angle-90;
        }
        else{
            this.angle = 270+angle;
        }
    }
}
