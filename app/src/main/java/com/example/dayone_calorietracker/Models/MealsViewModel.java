package com.example.dayone_calorietracker.Models;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.dayone_calorietracker.DataBase.AppDataBase;
import com.example.dayone_calorietracker.DataBase.Enitities.Meal;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MealsViewModel extends AndroidViewModel {

    private LiveData<List<Meal>> meals;
    private final AppDataBase db;


    public MealsViewModel(@NonNull Application application) {
        super(application);


        db =AppDataBase.getInstance(application.getApplicationContext());
        meals = db.mealdao().getAllMeal();
    }

    public LiveData<List<Meal>> getMeals() {
        return meals;
    }

    public void deleteMeal(Meal meal) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            db.mealdao().deleteById(meal.Id);
        });
    }

    public void AddMeal(Meal meal){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            db.mealdao().insert(meal);
        });
    }
}
