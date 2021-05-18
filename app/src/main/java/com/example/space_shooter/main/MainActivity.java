package com.example.space_shooter.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.space_shooter.Content;
import com.example.space_shooter.Game.GameActivity;
import com.example.space_shooter.R;
import com.example.space_shooter.SettingsActivity;
import com.example.space_shooter.Shop.ShopActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity /*implements View.OnClickListener*/ {
    Button enter, test;
    TextView hiScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enter = findViewById(R.id.enter);
        test = findViewById(R.id.shop);
        hiScore = findViewById(R.id.hiScore);

        String name = getSharedPreferences("settings", Context.MODE_PRIVATE).getString("playerName", "");
        if (name == "") {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
            finish();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("HiScore")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot d : task.getResult().getDocuments()) {
                            try {
                                hiScore.setText((String) d.get(Content.player.name));
                            } catch (Exception e) {}
                        }
                    }
                });

        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i;
                i = new Intent(MainActivity.this, GameActivity.class);
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

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        openQuitDialog();
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle("Вы уверены что хотите выйти?");

        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });

        quitDialog.show();
    }
}