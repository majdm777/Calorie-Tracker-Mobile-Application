package com.example.dayone_calorietracker.Fragments;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.dayone_calorietracker.DataBase.AppDataBase;
import com.example.dayone_calorietracker.DataBase.Enitities.Day;
import com.example.dayone_calorietracker.DataBase.Enitities.Meal;
import com.example.dayone_calorietracker.DataBase.Enitities.MealsPerDay;
import com.example.dayone_calorietracker.Models.DaysViewModel;
import com.example.dayone_calorietracker.Models.MealsViewModel;
import com.example.dayone_calorietracker.R;

import java.util.Date;
import java.util.Locale;

public class chooseWeightMeal extends Fragment {

    EditText Amount;
    Button btnAdd;
    Meal meal;

    MealsViewModel mealsViewModel;
    DaysViewModel daysViewModel;
    MealsPerDay MPD;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        View view = inflater.inflate(R.layout.choose_weight_fragment, container, false);

        mealsViewModel = new ViewModelProvider(this).get(MealsViewModel.class);
        daysViewModel = new ViewModelProvider(this).get(DaysViewModel.class);
        mealsViewModel=new ViewModelProvider(this).get(MealsViewModel.class);

        Amount = view.findViewById(R.id.Amount);
        btnAdd = view.findViewById(R.id.btnAddMeal);


        Bundle args = getArguments();

        if (args != null) {

            meal = new Meal();
            meal.Name = args.getString("name");
            meal.Calorie = args.getDouble("calories");
            meal.Protein = args.getDouble("protein");
            meal.Carbs = args.getDouble("carbs");
            meal.Fats = args.getDouble("fat");
            meal.Sugar = args.getDouble("sugar");
            meal.Type = args.getString("type");
        }

        btnAdd.setOnClickListener(v ->AddMeal());


        return view;
    }

    public void AddMeal() {

        if (Amount.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "enter amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(Amount.getText().toString());
        } catch (Exception e) {
            Toast.makeText(getContext(), "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        daysViewModel.addMealToDay(meal, amount);

        Toast.makeText(getContext(), "Meal Added", Toast.LENGTH_SHORT).show();

        NavHostFragment.findNavController(this).popBackStack();
    }
}

