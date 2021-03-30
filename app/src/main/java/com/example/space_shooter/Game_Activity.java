package com.example.space_shooter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class Game_Activity extends AppCompatActivity {

    Button w,s,a,d;
    io.github.controlwear.virtual.joystick.android.JoystickView joystick;

   // @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_);
        joystick = findViewById(R.id.joystick);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                Content.player.angle = angle;
                Log.d("ANGLE", "Angle: " + String.valueOf(Content.player.angle) + ", player angle: " + String.valueOf(angle));
                Content.player.speed = Content.player.normal_speed*(Float.valueOf(strength)/100);
            }
        });
       /* w = findViewById(R.id.top);
        s = findViewById(R.id.down);
        a = findViewById(R.id.left);
        d = findViewById(R.id.right);
        w.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View w) {
                Content.player.y-=10;
            }
        });
        a.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View w) {
                Content.player.x-=10;
            }
        });
        s.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View w) {
                Content.player.y+=10;
            }
        });
        d.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View w) {
                Content.player.x+=10;
            }
        });*/
    }
}