package com.example.dayone_calorietracker.Fragments;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.dayone_calorietracker.DataBase.AppDataBase;
import com.example.dayone_calorietracker.DataBase.Enitities.Meal;
import com.example.dayone_calorietracker.Models.DaysViewModel;
import com.example.dayone_calorietracker.Models.MealsViewModel;
import com.example.dayone_calorietracker.R;

import java.util.Date;
import java.util.Locale;

public class AddMealFragment extends Fragment {

    EditText name, calories, protein, carbs, fat, sugar;
    TextView tv_Calorie;
    String type = "Meal";
    Button btnSave, btnSaveAddMeal, btnSaveChanges, btnAdd_Meal;
    RadioButton isMeal, isDrink;

    MealsViewModel viewModel;
    DaysViewModel daysViewModel;
    private Meal currentMeal; // To hold the meal being edited

    public AddMealFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_meal_fragment, container, false);
        viewModel = new ViewModelProvider(this).get(MealsViewModel.class);
        daysViewModel = new ViewModelProvider(this).get(DaysViewModel.class);


        // Initialize Views
        name = view.findViewById(R.id.input_name);
        calories = view.findViewById(R.id.input_calories);
        isMeal = view.findViewById(R.id.isMeal);
        isDrink = view.findViewById(R.id.isDrink);
        protein = view.findViewById(R.id.input_protein);
        carbs = view.findViewById(R.id.input_carbs);
        fat = view.findViewById(R.id.input_fat);
        sugar = view.findViewById(R.id.input_sugar);
        btnSave = view.findViewById(R.id.btn_save);
        tv_Calorie = view.findViewById(R.id.tv_Calorie);
        btnSaveAddMeal = view.findViewById(R.id.btn_save_add_Meal);
        btnSaveChanges = view.findViewById(R.id.btnSave_Changes);
        btnAdd_Meal = view.findViewById(R.id.btnAdd_Meal);

        isMeal.setOnClickListener(this::setType);
        isDrink.setOnClickListener(this::setType);

        // --- COMPLETED EDIT LOGIC ---
        if (getArguments() != null) {
            String _Action = getArguments().getString("Action");
            if ("Edit".equals(_Action)) {
                btnSave.setVisibility(View.GONE);
                btnSaveAddMeal.setVisibility(View.GONE);
                btnAdd_Meal.setVisibility(View.GONE);
                btnSaveChanges.setVisibility(View.VISIBLE);

                int _MealId = getArguments().getInt("MealId");

                viewModel.getMeal(_MealId).observe(getViewLifecycleOwner(), meal -> {
                    if (meal != null) {
                        this.currentMeal = meal;
                        name.setText(meal.Name);
                        calories.setText(String.valueOf((int)meal.Calorie));
                        protein.setText(String.valueOf(meal.Protein));
                        carbs.setText(String.valueOf(meal.Carbs));
                        fat.setText(String.valueOf(meal.Fats));
                        sugar.setText(String.valueOf(meal.Sugar));

                        type = meal.Type;
                        if (type.equals("Meal")) isMeal.setChecked(true);
                        else isDrink.setChecked(true);
                        adjustTextView();
                    }else{
                        Toast.makeText(getContext(), "Meal not found", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).popBackStack();

                    }
                });
            }else if("Confirm".equals(_Action)){

                calories.setText(String.valueOf(getArguments().getInt("calories")));
                protein.setText(String.valueOf(getArguments().getInt("protein")));
                carbs.setText(String.valueOf(getArguments().getInt("carbs")));
                fat.setText(String.valueOf(getArguments().getInt("fat")));
                sugar.setText(String.valueOf(getArguments().getInt("sugar")));
            }
        }

        btnSave.setOnClickListener(v -> saveMeal());
        btnSaveAddMeal.setOnClickListener(v -> SaveAddMeal());
        btnAdd_Meal.setOnClickListener(v -> AddMeal());
        btnSaveChanges.setOnClickListener(v -> updateMeal());

        adjustTextView();
        return view;
    }

    private void updateMeal() {
        if (!validateInputs()) return;

        currentMeal.Name = name.getText().toString();
        currentMeal.Calorie = Double.parseDouble(calories.getText().toString());
        currentMeal.Type = type;
        currentMeal.Protein = Double.parseDouble(protein.getText().toString());
        currentMeal.Carbs = Double.parseDouble(carbs.getText().toString());
        currentMeal.Fats = Double.parseDouble(fat.getText().toString());
        currentMeal.Sugar = Double.parseDouble(sugar.getText().toString());

        new Thread(() -> {
            AppDataBase.getInstance(requireContext()).mealdao().update(currentMeal);
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();
            });
        }).start();
    }

    private void saveMeal() {
        if (!validateInputs()) return;

        Meal meal = createMealFromInputs();
        new Thread(() -> {
            AppDataBase.getInstance(requireContext()).mealdao().insert(meal);
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();
            });
        }).start();
    }

    private void SaveAddMeal() {
        if (!validateInputs()) return;

        String dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Meal meal = createMealFromInputs();

        new Thread(() -> {
            AppDataBase db = AppDataBase.getInstance(requireContext());
            db.mealdao().insert(meal);
            daysViewModel.addMealToDay(meal, 1);
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Saved & Added to Day", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();
            });
        }).start();
    }

    private void AddMeal(){
        if (!validateInputs()) return;

        String dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Meal meal = createMealFromInputs();

        new Thread(() -> {
            daysViewModel.addMealToDay(meal, 1);
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), " Added to Day", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();
            });
        }).start();
    }

    // Helper to extract UI data
    private Meal createMealFromInputs() {
        Meal meal = new Meal();
        meal.Name = name.getText().toString();
        meal.Calorie = Double.parseDouble(calories.getText().toString());
        meal.Type = type;
        meal.Protein = Double.parseDouble(protein.getText().toString());
        meal.Carbs = Double.parseDouble(carbs.getText().toString());
        meal.Fats = Double.parseDouble(fat.getText().toString());
        meal.Sugar = Double.parseDouble(sugar.getText().toString());
        return meal;
    }

    // Helper to prevent crashes on empty inputs
    private boolean validateInputs() {
        if (name.getText().toString().isEmpty() || calories.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Name and Calories are required", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Set defaults to 0 if macros are empty to avoid Double.parseDouble crash
        if (protein.getText().toString().isEmpty()) protein.setText("0");
        if (carbs.getText().toString().isEmpty()) carbs.setText("0");
        if (fat.getText().toString().isEmpty()) fat.setText("0");
        if (sugar.getText().toString().isEmpty()) sugar.setText("0");
        return true;
    }

    public void setType(View view) {
        type = (view.getId() == R.id.isMeal) ? "Meal" : "Drink";
        adjustTextView();
    }

    public void adjustTextView() {
        tv_Calorie.setText(type.equals("Meal") ? "Calorie /100g" : "Calories /100ml");
    }
}