package com.example.dayone_calorietracker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.dayone_calorietracker.DataBase.AppDataBase;
import com.example.dayone_calorietracker.DataBase.Enitities.Meal;

import java.util.List;

public class MealsViewModel extends AndroidViewModel {

    private LiveData<List<Meal>> meals;

    public MealsViewModel(@NonNull Application application) {
        super(application);


        AppDataBase db = Room.databaseBuilder(application.getApplicationContext(),AppDataBase.class,"AppDataBase").build();
        meals = db.mealdao().getAllMeal();
    }

    public LiveData<List<Meal>> getMeals() {
        return meals;
    }
}
