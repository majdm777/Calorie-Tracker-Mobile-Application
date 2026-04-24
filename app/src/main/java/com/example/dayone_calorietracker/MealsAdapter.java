package com.example.dayone_calorietracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayone_calorietracker.DataBase.Enitities.Meal;

import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.ViewHolder> {

    List<Meal> meals;


    public MealsAdapter(List<Meal> meals) {
        this.meals = meals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);

        holder.Name.setText(meal.Name);
        holder.Calorie.setText(meal.Calorie + " kcal");
        holder.Type.setText(meal.Type);
        holder.Protein.setText(meal.Protein+" g");
        holder.Carbs.setText(meal.Carbs+" g");
        holder.Fats.setText(meal.Fats+" g");
        holder.Sugar.setText(meal.Sugar+" g");
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name, Calorie, Type, Protein, Carbs, Fats, Sugar;

        public ViewHolder(@NonNull View view) {
            super(view);
            Name = view.findViewById(R.id.meal_name);
            Calorie = view.findViewById(R.id.meal_calories);
            Type = view.findViewById(R.id.meal_type);
            Protein = view.findViewById(R.id.meal_protein);
            Carbs = view.findViewById(R.id.meal_carbs);
            Fats = view.findViewById(R.id.meal_fat);
            Sugar = view.findViewById(R.id.meal_sugar);
        }
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }
}