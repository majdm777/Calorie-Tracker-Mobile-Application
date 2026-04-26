package com.example.dayone_calorietracker.Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.dayone_calorietracker.DataBase.AppDataBase;
import com.example.dayone_calorietracker.DataBase.Enitities.Day;

import java.util.List;

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
}
