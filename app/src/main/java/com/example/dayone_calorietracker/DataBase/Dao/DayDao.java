package com.example.dayone_calorietracker.DataBase.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dayone_calorietracker.DataBase.Enitities.Day;
import com.example.dayone_calorietracker.DataBase.Enitities.Meal;

import java.util.List;

@Dao
public interface DayDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Day day);

    @Query("SELECT * FROM Day WHERE Id = :DayId")
    LiveData<Day> getDayInfoById(int DayId);

    @Query("SELECT * FROM Day WHERE Date = :Date")
    Day getDayInfoByDate(String Date);

    @Query("SELECT COUNT(*) FROM Day WHERE Date = :date")
    int isDayExists(String date);

    @Query("SELECT * FROM Day")
    LiveData<List<Day>> getAllDay();

    @Query("DELETE FROM Day WHERE Id = :DayId")
    void deleteById(int DayId);

    @Query("DELETE FROM Day")
    void deleteAll();
}
