package com.example.dayone_calorietracker.Models;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dayone_calorietracker.DataBase.AppDataBase;
import com.example.dayone_calorietracker.DataBase.Enitities.MealsPerDay;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MealsPerDayViewModel extends AndroidViewModel {

    // 1. Declare db as a class member variable
    private final AppDataBase db;
    private final LiveData<List<MealsPerDay>> mealsPerDay;

    // 2. Create a single executor to reuse for background tasks
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public MealsPerDayViewModel(@NonNull Application application){
        super(application);
        // 3. Initialize the class member variable
        db = AppDataBase.getInstance(application);
        mealsPerDay = db.mealsperdaydao().getAllMPD();
    }

    // Now 'db' is accessible here
    public LiveData<List<MealsPerDay>> getMealsForDay(int dayId) {
        return db.mealsperdaydao().getMealsPerDay(dayId);
    }

    public LiveData<List<MealsPerDay>> getMealsPerDay() {
        return mealsPerDay;
    }

    public void addMPD(MealsPerDay MPD) {
        // Use the shared executor instead of creating a new one
        executor.execute(() -> {
            db.mealsperdaydao().insert(MPD);
        });
    }
}