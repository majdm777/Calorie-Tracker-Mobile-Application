package com.example.dayone_calorietracker.DataBase.Dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dayone_calorietracker.DataBase.Enitities.Meal;
import com.example.dayone_calorietracker.DataBase.Enitities.MealsPerDay;

import java.util.List;

@Dao
public interface MealsPerDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(MealsPerDay MPD);

    @Query("SELECT * FROM MealsPerDay WHERE Id = :MPDId")
    LiveData<MealsPerDay> getMPDInfo(int MPDId);

    @Query("SELECT * FROM MealsPerDay")
    LiveData<List<MealsPerDay>> getAllMPD();

    @Query("DELETE FROM MealsPerDay WHERE Id = :MPDId")
    void deleteById(int MPDId);

    @Query("DELETE FROM MealsPerDay")
    void deleteAll();
}
