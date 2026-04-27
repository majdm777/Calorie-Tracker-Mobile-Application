package com.example.dayone_calorietracker.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dayone_calorietracker.DataBase.Dao.DayDao;
import com.example.dayone_calorietracker.DataBase.Dao.MealDao;
import com.example.dayone_calorietracker.DataBase.Dao.MealsPerDayDao;
import com.example.dayone_calorietracker.DataBase.Enitities.Day;
import com.example.dayone_calorietracker.DataBase.Enitities.Meal;
import com.example.dayone_calorietracker.DataBase.Enitities.MealsPerDay;

@Database(entities={Meal.class, Day.class, MealsPerDay.class},version = 2)

public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase instance;

    public abstract MealDao mealdao();
    public abstract DayDao daydao();
    public abstract MealsPerDayDao mealsperdaydao();

    public static synchronized AppDataBase getInstance(Context context) {if (instance == null) {
        instance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDataBase.class, "AppDataBase")
                // ADD THIS LINE:
                .fallbackToDestructiveMigration()
                .build();
    }
        return instance;
    }
}
