package com.example.dayone_calorietracker.Models;

import static com.example.dayone_calorietracker.MainActivity.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.dayone_calorietracker.DataBase.AppDataBase;
import com.example.dayone_calorietracker.DataBase.Enitities.Day;
import com.example.dayone_calorietracker.DataBase.Enitities.Meal;
import com.example.dayone_calorietracker.DataBase.Enitities.MealsPerDay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DaysViewModel extends AndroidViewModel {

    private LiveData<List<Day>> days;

    public DaysViewModel(@NonNull Application application){
        super(application);

        AppDataBase db = Room.databaseBuilder(application.getApplicationContext(),AppDataBase.class,"AppDataBase").build();

        days=db.daydao().getAllDay();
    }

    public LiveData<List<Day>> getDays(){
        return days;
    }

    public static void UpdateDay(String dayDate,int calorie,double protein,double carbs,double sugar,double fats) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            db.daydao().updateDay(dayDate,calorie,protein,carbs,sugar,fats);
        });
    }

    public void addMealToDay(Meal meal, double amount) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            String dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new Date());

            Day day = db.daydao().getDayInfoByDate(dateString);

            if (day == null) {
                day = new Day();
                day.date = dateString;
                db.daydao().insert(day);
                day = db.daydao().getDayInfoByDate(dateString); // get ID after insert
            }

            double cal = meal.Calorie * amount;
            double protein = meal.Protein * amount;
            double carbs = meal.Carbs * amount;
            double fats = meal.Fats * amount;
            double sugar = meal.Sugar * amount;

            db.daydao().updateDay(dateString, (int) cal, protein, carbs, sugar, fats);

            MealsPerDay mpd = new MealsPerDay();
            mpd.DayId = day.Id;
            mpd.Name = meal.Name;
            mpd.Calorie = cal;
            mpd.Type = meal.Type;
            mpd.Fats = fats;
            mpd.Carbs = carbs;
            mpd.Sugar = sugar;
            mpd.Protein = protein;
            mpd.Amount = (int) amount;

            db.mealsperdaydao().insert(mpd);
        });
    }
}
