package com.example.dayone_calorietracker.Models;

import static com.example.dayone_calorietracker.MainActivity.db;

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
    private LiveData<List<MealsPerDay>> mealsPerDay;

    public MealsPerDayViewModel(@NonNull Application application){
        super(application);
        AppDataBase db = AppDataBase.getInstance(application);
        mealsPerDay = db.mealsperdaydao().getAllMPD();

    }

    public LiveData<List<MealsPerDay>> getMealsForDay(int dayId) {
        return db.mealsperdaydao().getMealsPerDay(dayId);
    }

    public LiveData<List<MealsPerDay>> getMealsPerDay(){return mealsPerDay;}

    public void addMPD(MealsPerDay MPD){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            db.mealsperdaydao().insert(MPD);
        });
    }

}
