package com.example.space_shooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enter = findViewById(R.id.enter);
        enter.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
            Intent i;
            i = new Intent(MainActivity.this, Game_Activity.class);
            startActivity(i);
    }
}