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
import androidx.room.Room;

import com.example.dayone_calorietracker.DataBase.AppDataBase;
import com.example.dayone_calorietracker.DataBase.Enitities.Meal;
import com.example.dayone_calorietracker.R;

import java.util.Date;
import java.util.Locale;

public class chooseWeightMeal extends Fragment {

    EditText Amount;
    Button btnAdd;
    Meal meal;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        View view = inflater.inflate(R.layout.choose_weight_fragment, container, false);

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

    public void AddMeal(){
        AppDataBase db = Room.databaseBuilder(requireContext(), AppDataBase.class, "AppDataBase").build();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = sdf.format(new Date());

        if(Amount.getText() == null){
            Toast.makeText(getContext(), "enter amount", Toast.LENGTH_SHORT).show();
            return;
        }
        double _Amount = Double.parseDouble(Amount.getText().toString());

        double _Calorie= meal.Calorie*_Amount;

        new Thread(() -> {
            db.daydao().updateDay(dateString, (int) _Calorie,meal.Protein,meal.Carbs,meal.Sugar,meal.Fats);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();

                // close fragment
                requireActivity().getSupportFragmentManager().popBackStack();
            });
        }).start();
    }
}

