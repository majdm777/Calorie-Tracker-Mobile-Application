package com.example.dayone_calorietracker.DataBase.Enitities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Meal")
public class Meal {

    @PrimaryKey(autoGenerate = true)
    public int Id;

    @ColumnInfo(name="name",defaultValue = "Meal")
    public String Name;

    @ColumnInfo(name="CaloriePer100")
    public double Calorie;

    @ColumnInfo(name="Type",defaultValue = "Unknown")
    public String Type;

    @ColumnInfo(name = "Carbs",defaultValue = "0")
    public double Carbs;

    @ColumnInfo(name = "Sugar",defaultValue = "0")
    public double Sugar;

    @ColumnInfo(name = "Protein",defaultValue = "0")
    public double Protein;

    @ColumnInfo(name = "fats",defaultValue = "0")
    public double Fats;


}
