package com.example.space_shooter.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.space_shooter.Game.Game_Activity;
import com.example.space_shooter.R;
import com.example.space_shooter.Shop.ShopActivity;

public class MainActivity extends AppCompatActivity /*implements View.OnClickListener*/ {
    Button enter, test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enter = findViewById(R.id.enter);
        test = findViewById(R.id.test);
        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i;
                i = new Intent(MainActivity.this, Game_Activity.class);
                startActivity(i);
                finish();
            }
        });
        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i;
                i = new Intent(MainActivity.this, ShopActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}