package com.example.dayone_calorietracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayone_calorietracker.DataBase.Enitities.Meal;
import com.example.dayone_calorietracker.DataBase.Enitities.MealsPerDay;
import com.example.dayone_calorietracker.R;


import java.util.List;

public class MealsPerDayAdapter extends RecyclerView.Adapter<MealsPerDayAdapter.ViewHolder>{
    List<MealsPerDay> mealsPerDays;

    public MealsPerDayAdapter(List<MealsPerDay> mealsPerDay) {
        this.mealsPerDays = mealsPerDay;
    }

    @NonNull
    @Override
    public MealsPerDayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mpd_layout, parent, false);
        return new ViewHolder(view,mealsPerDays);
    }

    @Override
    public void onBindViewHolder(@NonNull MealsPerDayAdapter.ViewHolder holder, int position) {
        MealsPerDay MPD = mealsPerDays.get(position);

        holder.Name.setText(MPD.Name);
        holder.Calorie.setText(MPD.Calorie + " kcal");
        holder.Amount.setText("x"+MPD.Amount);
        holder.Type.setText(MPD.Type);

    }

    @Override
    public int getItemCount() {
        return mealsPerDays.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name, Calorie, Type,Amount;

        public ViewHolder(@NonNull View view, List<MealsPerDay> mealsPerDay) {
            super(view);

            Name = view.findViewById(R.id.name);
            Calorie = view.findViewById(R.id.calories);
            Type = view.findViewById(R.id.type);
            Amount = view.findViewById(R.id.Amount);
        }
    }

    public void setMealsPerDay(List<MealsPerDay> mealsPerDay) {
        this.mealsPerDays = mealsPerDay;
        notifyDataSetChanged();
    }

}
