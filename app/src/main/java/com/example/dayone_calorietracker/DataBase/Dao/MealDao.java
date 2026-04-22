package com.example.dayone_calorietracker.DataBase.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dayone_calorietracker.DataBase.Enitities.Meal;

import java.util.List;

@Dao
public interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Meal meal);

    @Query("SELECT * FROM Meal WHERE Id = :MealId")
    LiveData<Meal> getMealInfo(int MealId);

    @Query("SELECT * FROM Meal")
    LiveData<List<Meal>> getAllMeal();

    @Query("DELETE FROM Meal WHERE Id = :MealId")
    void deleteById(int MealId);

    @Query("DELETE FROM Meal")
    void deleteAll();


}
