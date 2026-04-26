package com.example.dayone_calorietracker;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayone_calorietracker.Adapters.DaysAdapter;
import com.example.dayone_calorietracker.Adapters.MealsAdapter;
import com.example.dayone_calorietracker.DataBase.Enitities.Day;
import com.example.dayone_calorietracker.DataBase.Enitities.Meal;
import com.example.dayone_calorietracker.Models.DaysViewModel;
import com.example.dayone_calorietracker.Models.MealsViewModel;

import java.util.ArrayList;
import java.util.List;

public class Days extends AppCompatActivity {

    RecyclerView recyclerView;
    DaysAdapter adapter;
    DaysViewModel viewModel;
    SearchView searchView;

    List<Day> days= new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_days);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recDay);
        searchView = findViewById(R.id.searchView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DaysAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(DaysViewModel.class);

        // Observe data
        viewModel.getDays().observe(this, day -> {
            days = day;
            adapter.setDays(day);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMeals(newText);
                return true;
            }
        });
    }

    private void filterMeals(String text) {
        List<Day> filtered = new ArrayList<>();

        for (Day day : days) {
            if (day.date.toLowerCase().contains(text.toLowerCase())) {
                filtered.add(day);
            }
        }

        adapter.setDays(filtered);
    }
}

