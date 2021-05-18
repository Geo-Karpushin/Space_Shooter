package com.example.space_shooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.space_shooter.main.MainActivity;

public class SettingsActivity extends AppCompatActivity {
    Button enter;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        enter = findViewById(R.id.save);
        name = findViewById(R.id.name);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = name.getText().toString();
                if (playerName != "") {
                    Content.player.name = playerName;
                    Intent i;
                    getSharedPreferences("settings", Context.MODE_PRIVATE).edit().putString("playerName", playerName).apply();
                    i = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}