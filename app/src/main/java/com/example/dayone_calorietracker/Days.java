package com.example.dayone_calorietracker;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayone_calorietracker.Adapters.DaysAdapter;
import com.example.dayone_calorietracker.Adapters.MealsAdapter;
import com.example.dayone_calorietracker.DataBase.Enitities.Day;
import com.example.dayone_calorietracker.DataBase.Enitities.Meal;
import com.example.dayone_calorietracker.Models.DaysViewModel;
import com.example.dayone_calorietracker.Models.MealsViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class Days extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_days);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Main_Drawer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        DrawerLayout drawer = findViewById(R.id.Main_Drawer);
        NavigationView navView = findViewById(R.id.drawer);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        NavController navController =
                ((androidx.navigation.fragment.NavHostFragment)
                        getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment))
                        .getNavController();

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.daysFragment)
                        .setOpenableLayout(drawer)
                        .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

}

