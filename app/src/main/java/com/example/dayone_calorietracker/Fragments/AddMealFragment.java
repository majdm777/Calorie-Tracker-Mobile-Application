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
import androidx.room.Room;

import com.example.dayone_calorietracker.DataBase.AppDataBase;
import com.example.dayone_calorietracker.DataBase.Enitities.Meal;
import com.example.dayone_calorietracker.R;


import java.util.Date;
import java.util.Locale;

public class AddMealFragment extends Fragment {

    EditText name, calories, protein, carbs, fat, sugar;
    TextView tv_Calorie;
    String type;
    Button btnSave,btnSaveAddMeal;
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

        isMeal.setOnClickListener(this::setType);
        isDrink.setOnClickListener(this::setType);




        protein = view.findViewById(R.id.input_protein);
        carbs = view.findViewById(R.id.input_carbs);
        fat = view.findViewById(R.id.input_fat);
        sugar = view.findViewById(R.id.input_sugar);
        btnSave = view.findViewById(R.id.btn_save);
        tv_Calorie = view.findViewById(R.id.tv_Calorie);
        btnSaveAddMeal =view.findViewById(R.id.btn_save_add_Meal);




        btnSave.setOnClickListener(v -> saveMeal());
        btnSaveAddMeal.setOnClickListener(v -> SaveAddMeal());




        adjustTextView();
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

    private void SaveAddMeal(){
        String n = name.getText().toString();
        int cal = Integer.parseInt(calories.getText().toString());
        String Type = type;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = sdf.format(new Date());

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

            db.daydao().updateDay(dateString, (int) meal.Calorie,meal.Protein,meal.Carbs,meal.Sugar,meal.Fats);


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
            adjustTextView();
        }else{

            type = "Drink";
            adjustTextView();
        }
    }

    public void adjustTextView(){
        if(type.equals("Meal")){
            tv_Calorie.setText("Calorie /100g");
        }else{
            tv_Calorie.setText("Calories /100ml");
        }
    }
}
