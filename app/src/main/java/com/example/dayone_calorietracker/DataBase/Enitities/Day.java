package com.example.dayone_calorietracker.DataBase.Enitities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Day")
public class Day {
    @PrimaryKey(autoGenerate = true)
    public int Id;

    @ColumnInfo(name = "Date")
    public String Date;

    @ColumnInfo(name ="calories",defaultValue = "0")
    public int calorie;

    @ColumnInfo(name = "State",defaultValue = "NotReached")
    public String State;

    @ColumnInfo(name = "NumberOfMeals",defaultValue = "0")
    public  int NumberOfMeals;

    @ColumnInfo(name = "Target",defaultValue = "2000")
    public  int Target;

    @ColumnInfo(name = "Carbs",defaultValue = "0")
    public double Carbs;

    @ColumnInfo(name = "Sugar",defaultValue = "0")
    public double Sugar;

    @ColumnInfo(name = "Protein",defaultValue = "0")
    public double Protein;

    @ColumnInfo(name = "Fats",defaultValue = "0")
    public double Fats;


}
