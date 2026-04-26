package com.example.dayone_calorietracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayone_calorietracker.DataBase.Enitities.Day;
import com.example.dayone_calorietracker.R;


import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {

    List<Day> days;

    public DaysAdapter(List<Day> days) {
        this.days = days;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaysAdapter.ViewHolder holder, int position) {
        Day day = days.get(position);

        String _Calories = String.valueOf(day.calorie);
        String _Target =String.valueOf(day.Target);



        holder.Date.setText(day.date);
        holder.State.setText(day.State);
        holder.Calories_Target.setText(_Calories+"/"+_Target+" kcal");

    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView State,Date,Calories_Target;;

        public ViewHolder(@NonNull View view) {
            super(view);


            State = view.findViewById(R.id.State);
            Date = view.findViewById(R.id.Date);
            Calories_Target = view.findViewById(R.id.Calories_Target);
        }
    }

    public void setDays(List<Day> days) {
        this.days = days;
        notifyDataSetChanged();
    }
}
