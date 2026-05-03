package com.example.dayone_calorietracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayone_calorietracker.DataBase.Enitities.Day;
import com.example.dayone_calorietracker.DataBase.Enitities.Meal;
import com.example.dayone_calorietracker.R;


import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {

    List<Day> days;
    private OnItemClickListener Itemlistener;

    public DaysAdapter(List<Day> days) {
        this.days = days;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_layout, parent, false);
        return new ViewHolder(view, days, Itemlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Day day = days.get(position);

        holder.Date.setText(day.Date);
        holder.State.setText(day.State);
        holder.Calories_Target.setText(day.calorie + "/" + day.Target + " kcal");
        holder.NumberOfMeals.setText("Number Of Meals: " + day.NumberOfMeals);
        holder.bar.setMax(day.Target);
        holder.bar.setProgress(day.calorie);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView State, Date, Calories_Target, NumberOfMeals;
        ProgressBar bar;

        public ViewHolder(@NonNull View view, List<Day> days, OnItemClickListener listener) {
            super(view);

            NumberOfMeals = view.findViewById(R.id.Number_Of_Meals);
            State = view.findViewById(R.id.State);
            Date = view.findViewById(R.id.Date);
            Calories_Target = view.findViewById(R.id.Calories_Target);
            bar = view.findViewById(R.id.progressCalories);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(days.get(position));
                }
            });
        }
    }

    public void setDays(List<Day> days) {
        this.days = days;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.Itemlistener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Day day);
    }
}
