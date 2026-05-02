package com.example.dayone_calorietracker.Fragments;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayone_calorietracker.Adapters.MealsAdapter;
import com.example.dayone_calorietracker.DataBase.AppDataBase;
import com.example.dayone_calorietracker.DataBase.Enitities.Meal;
import com.example.dayone_calorietracker.Models.MealsViewModel;
import com.example.dayone_calorietracker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MealsFragment extends Fragment {

    RecyclerView recyclerView;
    MealsAdapter adapter;
    MealsViewModel viewModel;
    List<Meal> fullList = new ArrayList<>();

    public MealsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mealsfragment, container, false);

        recyclerView = view.findViewById(R.id.recMeal);
        Button btnAddMeal = view.findViewById(R.id.btnAddMeal);

        // RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MealsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(MealsViewModel.class);

        viewModel.getMeals().observe(getViewLifecycleOwner(), meals -> {
            fullList = meals;
            adapter.setMeals(meals);
        });

        // ➕ Add Meal button
        btnAddMeal.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_mealsFragment_to_addMealFragment);
        });

        // ❌ Delete button
        adapter.setOnButtonDeleteListener(meal -> {
            viewModel.deleteMeal(meal);
            Toast.makeText(requireContext(), "Meal deleted", Toast.LENGTH_SHORT).show();

        });

        // 📦 Click item → open chooseWeight
        adapter.setOnItemClickListener(meal -> {

            Bundle bundle = new Bundle();
            bundle.putString("type", meal.Type);
            bundle.putString("name", meal.Name);
            bundle.putDouble("calories", meal.Calorie);
            bundle.putDouble("protein", meal.Protein);
            bundle.putDouble("carbs", meal.Carbs);
            bundle.putDouble("fat", meal.Fats);
            bundle.putDouble("sugar", meal.Sugar);

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_mealsFragment_to_chooseWeightFragment, bundle);
        });

        SearchView searchView = view.findViewById(R.id.searchView);

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

        return view;
    }

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
