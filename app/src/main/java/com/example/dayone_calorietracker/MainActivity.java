package com.example.dayone_calorietracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.StaticLayout;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton$InspectionCompanion;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import com.example.dayone_calorietracker.DataBase.AppDataBase;
import com.example.dayone_calorietracker.DataBase.Enitities.Day;
import com.example.dayone_calorietracker.Fragments.AddMealFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {



    SimpleDateFormat sdf;
    String dateString;

    Day today;
    ProgressBar progressBar;
    TextView UWeight;
    TextView UHeight;
    TextView UAge;
    TextView UTarget;
    TextView Remaining_Calories;
    TextView Protein;
    TextView Carbs;
    TextView Sugar;
    TextView Fats;
    FloatingActionButton btnAddMeal;

    View CalorieLayout;


    SharedPreferences sp;

    public static AppDataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // connecting database
        db =AppDataBase.getInstance(this);

        UWeight = findViewById(R.id.User_Weight);
        UHeight = findViewById(R.id.User_Height);
        UAge   = findViewById(R.id.User_Age);
        UTarget = findViewById(R.id.calorie_status);
        progressBar = findViewById(R.id.progress_Bar);
        Remaining_Calories = findViewById(R.id.remaining_calories);
        Protein = findViewById(R.id.Protein);
        Carbs = findViewById(R.id.Carbs);
        Sugar = findViewById(R.id.Sugar);
        Fats =findViewById(R.id.Fats);
        CalorieLayout = findViewById(R.id.Calorie_layout);
        CalorieLayout.setOnClickListener(v->{
            Toast.makeText(this,Remaining_Calories.getText(),Toast.LENGTH_SHORT).show();
        });

        btnAddMeal =findViewById(R.id.add_meal_button);
        btnAddMeal.setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new AddMealFragment())
                    .addToBackStack(null)
                    .commit();
        });



        sp = getSharedPreferences("UserInfo",MODE_PRIVATE);

        if(isFirstTime()){
            Intent i = new Intent(this,WelcomePage.class);
            startActivity(i);
        }

        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateString = sdf.format(new Date());

//        load data from database
        fetchAndLoadData(dateString);



        DrawerLayout drawer = findViewById(R.id.Main_Drawer);
        Toolbar toolbar = findViewById(R.id.home_Toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open, R.string.close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.drawer);
        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_personal) {
                startActivity(new Intent(this, UserInfo.class));

            } else if (id == R.id.nav_meals) {
                startActivity(new Intent(this, Meals.class));

            } else if (id == R.id.nav_about) {
//                startActivity(new Intent(this, AboutActivity.class));
            } else if (id ==R.id.nav_days) {
                startActivity(new Intent(this, Days.class));
            }

            drawer.closeDrawers();

            return true;
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        fetchAndLoadData(sdf.format(new Date()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean isFirstTime(){

        return !sp.contains("User_Target");
    }

    public void loadUserInfo(){

        String weight="Weight: "+sp.getString("User_Weight","0")+"Kg";
        UWeight.setText(weight);

        String Height = "Height: "+sp.getString("User_Height","0")+"cm";
        UHeight.setText(Height);

        String Age = "Age: "+sp.getString("User_Age","0");
        UAge.setText(Age);

        String Calorie_Status = today.calorie+"/"+sp.getString("User_Target","Null")+" Kcal";
        UTarget.setText(Calorie_Status);

        progressBar.setMax(Integer.parseInt(sp.getString("User_Target","2000")));
        progressBar.setProgress(today.calorie);

        int remaining = today.Target - today.calorie;
        if(remaining<0) remaining=0;
        String remaining_calories = remaining+" kcal remaining";
        Remaining_Calories.setText(remaining_calories);

        Protein.setText("Protein: "+today.Protein+" g");
        Carbs.setText("Carbs: "+today.Carbs+" g");
        Sugar.setText("Sugar: "+today.Sugar+" g");
        Fats.setText("Fat: "+today.Fats+" g");

    }

    public void fetchAndLoadData(String dateString) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // 1. Get data from DB in background
            Day _today = db.daydao().getDayInfoByDate(dateString);

            if (_today == null) {
                _today = new Day();
                _today.State ="NotReached";
                _today.date = dateString;
                _today.Target = Integer.parseInt(sp.getString("User_Target", "2000"));
                db.daydao().insert(_today);
            }

            // 2. Pass the data back to the Main Thread to update UI
            final Day result = _today;
            runOnUiThread(() -> {
                this.today = result;
                loadUserInfo(); // Move this here so it waits for the data
            });
        });
    }

}