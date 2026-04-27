package com.example.dayone_calorietracker.DataBase.Enitities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "MealsPerDay",
        foreignKeys = @ForeignKey(
                entity = Day.class,
                parentColumns = "Id",
                childColumns = "DayId",
                onDelete = ForeignKey.CASCADE

        ),indices = {@Index("Id")}
)
public class MealsPerDay {
    @PrimaryKey(autoGenerate = true)
    public int Id;

    @ColumnInfo(name = "DayId")
    public int DayId;

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

    @ColumnInfo(name = "Fats",defaultValue = "0")
    public double Fats;

    @ColumnInfo(name = "Amount",defaultValue = "1")
    public int Amount;

    @ColumnInfo(name = "Note",defaultValue = ".")
    public String Note;


}
