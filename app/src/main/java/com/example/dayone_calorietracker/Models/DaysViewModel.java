package com.example.dayone_calorietracker.Models;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
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

    private final LiveData<List<Day>> days;
    private final AppDataBase db;
    // Define a single executor to reuse for all background tasks
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public DaysViewModel(@NonNull Application application) {
        super(application);
        db = AppDataBase.getInstance(application);
        days = db.daydao().getAllDay();
    }

    public LiveData<List<Day>> getDays() {
        return days;
    }



    // REMOVED 'static' so it can access the 'db' instance variable
    public void updateDay(String dayDate, int calorie, double protein, double carbs, double sugar, double fats) {
        executor.execute(() -> {
            Day day = db.daydao().getDayInfoByDate(dayDate);
            double ratio = ((double)day.calorie / day.Target)*100 ;
            String state = "NotReached";

            if(ratio<75){
                state ="NotEnough";
            }else if(ratio<90){
                state ="NotReached";
            }else if(ratio<110){
                state ="Reached";
            }else{
                state ="Exceeded";
            }

            db.daydao().updateDay(dayDate, calorie, protein, carbs, sugar, fats,state);
        });
    }
    public LiveData<Day> getDay(int dayId) {
        return db.daydao().getDayInfoById(dayId);
    }



    public void addMealToDay(Meal meal, double amount) {
        executor.execute(() -> {
            String dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            Day day = db.daydao().getDayInfoByDate(dateString);

            if (day == null) {
                day = new Day();
                day.Date = dateString;

                // Fetch user target from SharedPreferences so the new day isn't empty
                SharedPreferences sp = getApplication().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                day.Target = Integer.parseInt(sp.getString("User_Target", "2000"));

                db.daydao().insert(day);
                // Refresh day object to get the auto-generated ID
                day = db.daydao().getDayInfoByDate(dateString);
            }

            // Calculation (Assuming 'amount' is a multiplier, e.g., 1.5 for 150g)
            // If your amount is raw grams (e.g. 150), you should divide by 100: (amount / 100.0)
//            double factor = amount / 100.0;     possible update later
            double factor = amount;

            double cal = meal.Calorie * factor;
            double protein = meal.Protein * factor;
            double carbs = meal.Carbs * factor;
            double fats = meal.Fats * factor;
            double sugar = meal.Sugar * factor;

            // Update the daily totals

            this.updateDay(dateString, (int) cal, protein, carbs, sugar, fats);

            // Record this specific meal entry
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
