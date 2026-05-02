package com.example.dayone_calorietracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayone_calorietracker.DataBase.Enitities.Meal;
import com.example.dayone_calorietracker.Fragments.AddMealFragment;
import com.example.dayone_calorietracker.R;

import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.ViewHolder> {

    List<Meal> meals;
    private OnItemClickListener Itemlistener;
    private OnButtonEditListener ButtonEditListener;

    private OnButtonDeleteListener ButtonDeleteListener;



    public MealsAdapter(List<Meal> meals) {
        this.meals = meals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_layout, parent, false);
        return new ViewHolder(view, meals, Itemlistener,ButtonDeleteListener,ButtonEditListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);

        holder.Name.setText(meal.Name);
        holder.Calorie.setText(meal.Calorie + " kcal");
        holder.Type.setText(meal.Type);
        holder.Id.setText(""+meal.Id);
        holder.btnEdit.setText("Edit");

    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name, Calorie, Type,Id;
        Button  btnDelete,btnEdit;
        public ViewHolder(@NonNull View view,
                          List<Meal> meals,
                          OnItemClickListener listener,
                          OnButtonDeleteListener OnButtonDeleteListener,
                          OnButtonEditListener OnButtonEditListener) {
            super(view);

            Name = view.findViewById(R.id.meal_name);
            Calorie = view.findViewById(R.id.meal_calories);
            Type = view.findViewById(R.id.meal_type);
            btnDelete = view.findViewById(R.id.meal_delete);
            btnEdit=view.findViewById(R.id.meal_Edit);

            Id=view.findViewById(R.id.meal_id);

            itemView.setOnClickListener(v -> {
                int position =getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(meals.get(position));
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (OnButtonDeleteListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    OnButtonDeleteListener.onButtonDeleteClick(meals.get(getAdapterPosition()));
                }
            });
            btnEdit.setOnClickListener(v -> {
                if (OnButtonEditListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    OnButtonEditListener.onButtonEditClick(meals.get(getAdapterPosition()));
                }
            });


        }
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.Itemlistener=listener;
    }
    public void setOnButtonDeleteListener(OnButtonDeleteListener listener){
        this.ButtonDeleteListener=listener;
    }
    public void setOnButtonEditListener(OnButtonEditListener listener){
        this.ButtonEditListener=listener;
    }


    public interface OnItemClickListener {
        void onItemClick(Meal meal);

    }

    public interface OnButtonDeleteListener{
        void onButtonDeleteClick(Meal meal);
    }

    public interface  OnButtonEditListener{
        void onButtonEditClick(Meal meal);
    }
}




