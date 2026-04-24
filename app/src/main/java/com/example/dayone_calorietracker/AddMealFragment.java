package com.example.dayone_calorietracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.dayone_calorietracker.DataBase.AppDataBase;
import com.example.dayone_calorietracker.DataBase.Enitities.Meal;

public class AddMealFragment extends Fragment {

    EditText name, calories, protein, carbs, fat, sugar;
    String type;
    Button btnSave;
    RadioButton isMeal, isDrink;




    public AddMealFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_meal_fragment, container, false);

        name = view.findViewById(R.id.input_name);
        calories = view.findViewById(R.id.input_calories);
        type = "Meal";

        isMeal = view.findViewById(R.id.isMeal);
        isDrink = view.findViewById(R.id.isDrink);



        protein = view.findViewById(R.id.input_protein);
        carbs = view.findViewById(R.id.input_carbs);
        fat = view.findViewById(R.id.input_fat);
        sugar = view.findViewById(R.id.input_sugar);
        btnSave = view.findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v -> saveMeal());

        return view;
    }

    private void saveMeal() {
        String n = name.getText().toString();
        int cal = Integer.parseInt(calories.getText().toString());
        String Type = type;


        Meal meal = new Meal();

        meal.Name = n;
        meal.Calorie=cal;
        meal.Type=Type;
        meal.Protein=Double.parseDouble(protein.getText().toString());
        meal.Carbs=Double.parseDouble(carbs.getText().toString());
        meal.Fats=Double.parseDouble(fat.getText().toString());
        meal.Sugar=Double.parseDouble(sugar.getText().toString());



        AppDataBase db = Room.databaseBuilder(requireContext(), AppDataBase.class, "AppDataBase").build();

        new Thread(() -> {
            db.mealdao().insert(meal);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();

                // close fragment
                requireActivity().getSupportFragmentManager().popBackStack();
            });

        }).start();
    }

    public void setType(View view) {
        if (view.getId() == R.id.isMeal) {
            type = "Meal";
        }else{
            type = "Drink";
        }
    }
}
