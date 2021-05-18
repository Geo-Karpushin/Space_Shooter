package com.example.space_shooter.Shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.example.space_shooter.Content;
import com.example.space_shooter.main.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.example.space_shooter.R;
import com.example.space_shooter.Shop.main.SectionsPagerAdapter;

public class ShopActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabs;
    FloatingActionButton fab;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        int index = getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("playerImg", 0);
        tabs.selectTab(tabs.getTabAt(index));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Content.player.playerImg = tabs.getSelectedTabPosition()+1;
                getSharedPreferences("settings", MODE_PRIVATE).edit().putInt("playerImg", Content.player.playerImg - 1).apply();
                i = new Intent(ShopActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Content.player.playerImg = tabs.getSelectedTabPosition()+1;
        getSharedPreferences("settings", MODE_PRIVATE).edit().putInt("playerImg", Content.player.playerImg - 1).apply();
        i = new Intent(ShopActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}