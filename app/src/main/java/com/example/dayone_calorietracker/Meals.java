package com.example.dayone_calorietracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayone_calorietracker.DataBase.Enitities.Meal;

import java.util.ArrayList;
import java.util.List;

public class Meals extends AppCompatActivity {

    RecyclerView recyclerView;
    MealsAdapter adapter;
    MealsViewModel viewModel;
    SearchView searchView;
    Button btnAddMeal;

    List<Meal> fullList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);

        // Init views
        recyclerView = findViewById(R.id.recMeal);
        searchView = findViewById(R.id.searchView);
        btnAddMeal = findViewById(R.id.btnAddMeal);

        // Recycler setup
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MealsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(MealsViewModel.class);

        // Observe data
        viewModel.getMeals().observe(this, meals -> {
            fullList = meals;
            adapter.setMeals(meals);
        });

        // Search functionality
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

        // Add Meal button ****

        btnAddMeal.setOnClickListener(v -> {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new AddMealFragment())
                    .addToBackStack(null) // so user can go back
                    .commit();
        });

    }

    // 🔍 Filter logic
    private void filterMeals(String text) {
        List<Meal> filtered = new ArrayList<>();

        for (Meal meal : fullList) {
            if (meal.Name.toLowerCase().contains(text.toLowerCase())) {
                filtered.add(meal);
            }
        }

        adapter.setMeals(filtered);
    }
}