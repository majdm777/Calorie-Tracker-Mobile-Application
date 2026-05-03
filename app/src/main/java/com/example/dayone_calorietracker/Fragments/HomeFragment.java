package com.example.dayone_calorietracker.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dayone_calorietracker.DataBase.AppDataBase;
import com.example.dayone_calorietracker.DataBase.Enitities.Day;
import com.example.dayone_calorietracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {


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
    AppDataBase db;

    Button viewTodayMeal;
    SharedPreferences sp;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = AppDataBase.getInstance(requireContext());





        UWeight =view.findViewById(R.id.User_Weight);
        UHeight =view.findViewById(R.id.User_Height);
        UAge   = view.findViewById(R.id.User_Age);
        UTarget = view.findViewById(R.id.calorie_status);
        progressBar = view.findViewById(R.id.progress_Bar);
        Remaining_Calories = view.findViewById(R.id.remaining_calories);
        Protein = view.findViewById(R.id.Protein);
        Carbs = view.findViewById(R.id.Carbs);
        Sugar = view.findViewById(R.id.Sugar);
        Fats =view.findViewById(R.id.Fats);
        viewTodayMeal = view.findViewById(R.id.view_Todays_Meal);
        CalorieLayout = view.findViewById(R.id.Calorie_layout);
        CalorieLayout.setOnClickListener(v->{
            Toast.makeText(requireContext(),Remaining_Calories.getText(),Toast.LENGTH_SHORT).show();

        });

        btnAddMeal =view.findViewById(R.id.add_meal_button);
        btnAddMeal.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_homeFragment_to_addMealFragment);
        });
        viewTodayMeal.setOnClickListener(v -> {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateString = sdf.format(new Date());

            new Thread(() -> {
                Day day = db.daydao().getDayInfoByDate(dateString);

                requireActivity().runOnUiThread(() -> {

                    if (day == null) {
                        Toast.makeText(requireContext(), "No data for today", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", day.Id);

                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_homeFragment_to_mealsPerDayFragment, bundle);
                });

            }).start();
        });





        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateString = sdf.format(new Date());

//        load data from database
        fetchAndLoadData(dateString);




        // Inflate the layout for this fragment
        return view;
    }

    public void fetchAndLoadData(String dateString) {    // 1. Initialize SharedPreferences if not already done
        if (sp == null) sp = requireContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        // 2. Immediate UI updates (Safe because it's on the Main Thread)
        UWeight.setText(sp.getString("User_Weight", "0") + " kg");
        UHeight.setText(sp.getString("User_Height", "0") + " cm");
        UAge.setText(sp.getString("User_Age", "0"));

        // 3. Background Database Work
        new Thread(() -> {
            Day day = db.daydao().getDayInfoByDate(dateString);

            if (day == null) {
                day = new Day();
                day.Date = dateString;
                // Get target from SP inside thread is fine, but parsing must be safe
                String targetStr = sp.getString("User_Target", "2000");
                day.Target = Integer.parseInt(targetStr);
                db.daydao().insert(day);
            }

            // Create a final copy for the inner UI thread
            final Day finalDay = day;
            double remaining = finalDay.Target - finalDay.calorie;

            // 4. Update UI on the Main Thread
            if (isAdded()) { // Check if fragment is still attached to prevent crashes
                requireActivity().runOnUiThread(() -> {
                    UTarget.setText(finalDay.calorie + " / " + finalDay.Target + " kcal");
                    Remaining_Calories.setText(String.format(Locale.getDefault(), "%.0f kcal", remaining));

                    Protein.setText(String.format(Locale.getDefault(), "%.1f g", finalDay.Protein));
                    Carbs.setText(String.format(Locale.getDefault(), "%.1f g", finalDay.Carbs));
                    Sugar.setText(String.format(Locale.getDefault(), "%.1f g", finalDay.Sugar));
                    Fats.setText(String.format(Locale.getDefault(), "%.1f g", finalDay.Fats));

                    progressBar.setMax(finalDay.Target);
                    progressBar.setProgress(finalDay.calorie);

                    if(finalDay.calorie > finalDay.Target){
                        progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
                    }else if(finalDay.calorie == finalDay.Target){
                        progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
                    }else{
                        progressBar.setProgressTintList(ColorStateList.valueOf(Color.BLACK));
                    }
                });
            }
        }).start();
    }
}