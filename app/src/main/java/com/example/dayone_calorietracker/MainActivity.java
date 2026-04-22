package com.example.dayone_calorietracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.StaticLayout;
import android.view.Menu;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import com.example.dayone_calorietracker.DataBase.AppDataBase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    public Date date;
    ProgressBar progressBar;

    public static AppDataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if(isFirstTime()){
            Intent i = new Intent(this,UserInfo.class);
            startActivity(i);
        }

        date = new Date();

        //database connection
        db= Room.databaseBuilder(getApplicationContext(),AppDataBase.class,"AppDataBase").build();


        Toolbar HomeToolBar = (Toolbar) findViewById(R.id.home_Toolbar);
        setSupportActionBar(HomeToolBar);

        progressBar = findViewById(R.id.progress_Bar);
        progressBar.setMax(2000);
        progressBar.setProgress(500);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DrawerLayout drawer = findViewById(R.id.Main_Drawer);
        Toolbar toolbar = findViewById(R.id.home_Toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open, R.string.close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean isFirstTime(){
        SharedPreferences sp = getSharedPreferences("UserInfo",MODE_PRIVATE);
        return !sp.contains("User_Target");
    }
}