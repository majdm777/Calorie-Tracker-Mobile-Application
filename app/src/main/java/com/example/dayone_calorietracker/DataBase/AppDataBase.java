package com.example.dayone_calorietracker.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.dayone_calorietracker.DataBase.Dao.DayDao;
import com.example.dayone_calorietracker.DataBase.Dao.MealDao;
import com.example.dayone_calorietracker.DataBase.Dao.MealsPerDayDao;
import com.example.dayone_calorietracker.DataBase.Enitities.Day;
import com.example.dayone_calorietracker.DataBase.Enitities.Meal;
import com.example.dayone_calorietracker.DataBase.Enitities.MealsPerDay;

@Database(entities={Meal.class, Day.class, MealsPerDay.class},version = 1)

public abstract class AppDataBase extends RoomDatabase {
    public abstract MealDao mealdao();

    public abstract DayDao daydao();

    public abstract MealsPerDayDao mealsperdaydao();

}
